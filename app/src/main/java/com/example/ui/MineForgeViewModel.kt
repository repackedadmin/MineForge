package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class MineForgeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MineForgeDatabase.getDatabase(application)
    private val repository = MiningRepository(db.mineForgeDao())

    // --- Authentication States ---
    val activeUser: StateFlow<UserAccount?> = repository.activeUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // --- Live Child States ---
    private val _activeRigs = MutableStateFlow<List<MiningRig>>(emptyList())
    val activeRigs: StateFlow<List<MiningRig>> = _activeRigs.asStateFlow()

    private val _activeWithdrawals = MutableStateFlow<List<WithdrawalRequest>>(emptyList())
    val activeWithdrawals: StateFlow<List<WithdrawalRequest>> = _activeWithdrawals.asStateFlow()

    // Live continuous token tick accumulator
    private val _livePendingEarnings = MutableStateFlow(0.0)
    val livePendingEarnings: StateFlow<Double> = _livePendingEarnings.asStateFlow()

    // Real-time auto-claim synchronization states
    private val _autoClaimProgress = MutableStateFlow(0f)
    val autoClaimProgress: StateFlow<Float> = _autoClaimProgress.asStateFlow()

    private val _autoClaimCountdown = MutableStateFlow(15)
    val autoClaimCountdown: StateFlow<Int> = _autoClaimCountdown.asStateFlow()

    private val _isAutoClaimEnabled = MutableStateFlow(true)
    val isAutoClaimEnabled: StateFlow<Boolean> = _isAutoClaimEnabled.asStateFlow()

    fun toggleAutoClaim(enabled: Boolean) {
        _isAutoClaimEnabled.value = enabled
    }

    // Total hash power
    val totalHashPowerMhs = _activeRigs.map { rigs ->
        rigs.sumOf { it.hashRateMhs }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Total active rigs
    val totalActiveRigsCount = _activeRigs.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // MGT Live Price State
    private val _tokenPriceUsd = MutableStateFlow(0.185) // Initial fallback
    val tokenPriceUsd: StateFlow<Double> = _tokenPriceUsd.asStateFlow()

    // React state-backed custom interval mining loop states
    private val _customIntervalSeconds = MutableStateFlow(3) // Default interval: 3 seconds
    val customIntervalSeconds: StateFlow<Int> = _customIntervalSeconds.asStateFlow()

    private val _isIntervalMiningActive = MutableStateFlow(true)
    val isIntervalMiningActive: StateFlow<Boolean> = _isIntervalMiningActive.asStateFlow()

    private val _lastIncrementAmount = MutableStateFlow(0.0)
    val lastIncrementAmount: StateFlow<Double> = _lastIncrementAmount.asStateFlow()

    private val _tickPulseTrigger = MutableStateFlow(0)
    val tickPulseTrigger: StateFlow<Int> = _tickPulseTrigger.asStateFlow()

    // Action execution logs
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    private var activeUserCollectJob: Job? = null
    private var liveTickJob: Job? = null
    private var priceFetchJob: Job? = null
    private var customIntervalMiningJob: Job? = null

    init {
        // Observe active user changes to hot-load rigs & withdrawals
        viewModelScope.launch {
            activeUser.collect { user ->
                activeUserCollectJob?.cancel()
                if (user != null) {
                    // Load user arrays
                    activeUserCollectJob = viewModelScope.launch {
                        launch {
                            repository.observeRigsForUser(user.email).collect { rigs ->
                                _activeRigs.value = rigs
                            }
                        }
                        launch {
                            repository.observeWithdrawals(user.email).collect { withdrawals ->
                                _activeWithdrawals.value = withdrawals
                            }
                        }
                    }
                } else {
                    _activeRigs.value = emptyList()
                    _activeWithdrawals.value = emptyList()
                    _livePendingEarnings.value = 0.0
                }
            }
        }

        // Run the high precision 20Hz continuous token earnings counter
        startEarningsGeneratorLoop()

        // Run the custom state-based interval mining loop
        startCustomIntervalMiningLoop()

        // Start Price Ticker Poller
        fetchTokenPriceRate()
    }

    private fun startEarningsGeneratorLoop() {
        liveTickJob?.cancel()
        liveTickJob = viewModelScope.launch {
            var lastAutoClaimTime = System.currentTimeMillis()
            while (true) {
                delay(100) // update every 100ms
                val rigs = _activeRigs.value
                val now = System.currentTimeMillis()
                
                // Calculate current pending from all rigs
                var sumUnclaimed = 0.0
                for (rig in rigs) {
                    val deltaMs = now - rig.lastClaimTimestamp
                    if (deltaMs > 0) {
                        val earned = rig.dailyEarnings * (deltaMs.toDouble() / 86400000.0)
                        sumUnclaimed += earned
                    }
                }
                _livePendingEarnings.value = sumUnclaimed

                // Calculate periodic progress and auto claim triggering
                val cycleDurationMs = 15000L // 15 seconds cycle
                val elapsedMs = now - lastAutoClaimTime
                
                if (elapsedMs >= cycleDurationMs) {
                    // Reset timestamp
                    lastAutoClaimTime = now
                    _autoClaimProgress.value = 1.0f
                    _autoClaimCountdown.value = 0
                    
                    // If user is active and has active rigs, auto claim
                    val user = activeUser.value
                    if (user != null && rigs.isNotEmpty() && _isAutoClaimEnabled.value) {
                        launch {
                            val claimed = repository.claimPendingEarnings(user.email)
                            if (claimed > 0) {
                                triggerToast("Auto-Claimed ${String.format("%.5f", claimed)} MGT to wallet!")
                            }
                        }
                    }
                } else {
                    val fraction = (elapsedMs.toFloat() / cycleDurationMs.toFloat()).coerceIn(0f, 1f)
                    _autoClaimProgress.value = fraction
                    
                    val remainingSeconds = (((cycleDurationMs - elapsedMs) / 1000L) + 1L).toInt().coerceIn(1, 15)
                    _autoClaimCountdown.value = remainingSeconds
                }
            }
        }
    }

    private fun fetchTokenPriceRate() {
        priceFetchJob?.cancel()
        priceFetchJob = viewModelScope.launch {
            while (true) {
                try {
                    val response = PriceApiInstance.apiService.getTickerPrice("ETHUSDT")
                    val ethPrice = response.price.toDoubleOrNull() ?: 3500.0
                    // Peg MGT Price to 0.00005 ETH (approx $0.175 - $0.20 based on markets)
                    val mgtPrice = ethPrice * 0.000051
                    _tokenPriceUsd.value = String.format("%.5f", mgtPrice).toDouble()
                } catch (e: Exception) {
                    // Fallback to slight fluctuation
                    val base = 0.178
                    val variance = Random.nextDouble(-0.012, 0.015)
                    _tokenPriceUsd.value = String.format("%.5f", base + variance).toDouble()
                }
                delay(30000) // Poll every 30 seconds
            }
        }
    }

    private fun startCustomIntervalMiningLoop() {
        customIntervalMiningJob?.cancel()
        customIntervalMiningJob = viewModelScope.launch {
            while (true) {
                val seconds = _customIntervalSeconds.value
                delay(seconds * 1000L)

                val user = activeUser.value
                val rigs = _activeRigs.value
                val isActive = _isIntervalMiningActive.value

                if (user != null && rigs.isNotEmpty() && isActive) {
                    val totalHash = rigs.sumOf { it.hashRateMhs }
                    if (totalHash > 0.0) {
                        // Rate formula:
                        // 10 MH/s earns 5.0 MGT per day (86400s).
                        // 5 / (10 * 86400) = 0.000005787 MGT per MH/s per second.
                        val baseMultiplier = 0.000005787
                        val increment = totalHash * baseMultiplier * seconds
                        if (increment > 0.0) {
                            val success = repository.incrementUserBalance(user.email, increment)
                            if (success) {
                                _lastIncrementAmount.value = increment
                                _tickPulseTrigger.value = _tickPulseTrigger.value + 1
                            }
                        }
                    }
                }
            }
        }
    }

    fun setCustomInterval(seconds: Int) {
        _customIntervalSeconds.value = seconds
        startCustomIntervalMiningLoop()
        triggerToast("Mining tick interval updated to ${seconds} s")
    }

    fun toggleIntervalMining(enabled: Boolean) {
        _isIntervalMiningActive.value = enabled
        if (enabled) {
            startCustomIntervalMiningLoop()
            triggerToast("Mining engine resumed")
        } else {
            customIntervalMiningJob?.cancel()
            _lastIncrementAmount.value = 0.0
            triggerToast("Mining engine paused")
        }
    }

    // --- Action Methods ---

    fun triggerToast(msg: String) {
        viewModelScope.launch {
            _toastMessage.emit(msg)
        }
    }

    // Email/Password Signup and Login
    fun registerWithEmail(email: String, name: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || !email.contains("@")) {
                triggerToast("Invalid email address format.")
                return@launch
            }
            if (name.isBlank()) {
                triggerToast("Please enter a display name.")
                return@launch
            }
            if (pass.length < 6) {
                triggerToast("Password must be at least 6 characters.")
                return@launch
            }

            val newUser = UserAccount(
                email = email.trim(),
                displayName = name.trim(),
                passwordHash = pass // Mock hash, simple for gameplay
            )

            val success = repository.registerUser(newUser)
            if (success) {
                triggerToast("Account created successfully!")
                // Automatically login
                repository.loginUser(email.trim(), pass)
                onSuccess()
            } else {
                triggerToast("Email already registered.")
            }
        }
    }

    fun loginWithEmail(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (email.isBlank()) {
                triggerToast("Enter email address.")
                return@launch
            }
            val success = repository.loginUser(email.trim(), pass)
            if (success) {
                triggerToast("Auth successful!")
                onSuccess()
            } else {
                triggerToast("Invalid email or password.")
            }
        }
    }

    fun handleGoogleOneTapLogin(email: String, name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Check if user already exists
            val existing = repository.findUserByEmail(email)
            if (existing == null) {
                // Register a mock user with random password
                val guestUser = UserAccount(
                    email = email,
                    displayName = name,
                    passwordHash = (100000..999999).random().toString(),
                    isLoggedIn = true
                )
                repository.registerUser(guestUser)
            } else {
                repository.loginUser(email, existing.passwordHash)
            }
            triggerToast("Connected via Google OAuth")
            onSuccess()
        }
    }

    fun requestPasswordReset(email: String) {
        viewModelScope.launch {
            val user = repository.findUserByEmail(email.trim())
            if (user != null) {
                triggerToast("Password reset link dispatched to ${user.email}!")
            } else {
                triggerToast("No account associated with this email.")
            }
        }
    }

    // Buying mining power & virtual rigs
    fun purchaseRigTier(tierName: String, priceUsd: Double, hashRateMhs: Double, dailyEarnings: Double) {
        viewModelScope.launch {
            val user = activeUser.value
            if (user == null) {
                triggerToast("Must be logged in to purchase.")
                return@launch
            }

            val success = repository.buyRig(
                email = user.email,
                tierName = tierName,
                priceUsd = priceUsd,
                hashRateMhs = hashRateMhs,
                dailyEarnings = dailyEarnings
            )

            if (success) {
                triggerToast("Successfully purchased: $tierName Mini Rig!")
            } else {
                triggerToast("Purchase transaction failed.")
            }
        }
    }

    // Claim pending earnings to main balance
    fun claimPendingMines() {
        viewModelScope.launch {
            val user = activeUser.value ?: return@launch
            val claimed = repository.claimPendingEarnings(user.email)
            if (claimed > 0) {
                val formattedStr = String.format("%.4f", claimed)
                triggerToast("Successfully claimed $formattedStr MGT tokens!")
            } else {
                triggerToast("No pending mining rewards to claim yet.")
            }
        }
    }

    // Simulated Web3 / WalletConnect connection
    fun connectSimulatedWallet(address: String, network: String) {
        viewModelScope.launch {
            val user = activeUser.value ?: return@launch
            repository.updateWallet(user.email, address, network)
            triggerToast("Wallet connected on $network!")
        }
    }

    fun disconnectWallet() {
        viewModelScope.launch {
            val user = activeUser.value ?: return@launch
            repository.updateWallet(user.email, null, null)
            triggerToast("Wallet disconnected.")
        }
    }

    // Requesting withdrawals
    fun requestWithdrawal(amount: Double, walletAddress: String, network: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = activeUser.value ?: return@launch
            if (amount < 100.0) {
                triggerToast("Minimum withdrawal requirement is 100 MGT.")
                return@launch
            }
            if (user.mainBalance < amount) {
                triggerToast("Insufficient MGT balance.")
                return@launch
            }

            val success = repository.executeWithdrawal(user.email, amount, walletAddress, network)
            if (success) {
                triggerToast("Withdrawal of ${amount.toInt()} MGT submitted successfully!")
                onSuccess()
            } else {
                triggerToast("Withdrawal failed.")
            }
        }
    }

    // Profile updates
    fun updateProfile(displayName: String, currency: String, language: String, hasBiometric: Boolean) {
        viewModelScope.launch {
            val user = activeUser.value ?: return@launch
            repository.updateUserSettings(user.email, displayName, currency, language, hasBiometric)
            triggerToast("Settings saved successfully!")
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = activeUser.value ?: return@launch
            repository.deleteUserAccount(user.email)
            triggerToast("Your account and database metrics were fully cleared.")
            onSuccess()
        }
    }

    fun stakeMgt(amount: Double) {
        viewModelScope.launch {
            val user = activeUser.value ?: return@launch
            if (amount <= 0.0) {
                triggerToast("Please enter a valid positive staking amount.")
                return@launch
            }
            if (user.mainBalance < amount) {
                triggerToast("Insufficient MGT liquid balance for staking.")
                return@launch
            }
            val success = repository.stakeTokens(user.email, amount)
            if (success) {
                triggerToast("Successfully staked ${String.format("%.2f", amount)} MGT node!")
            } else {
                triggerToast("Staking transaction failed.")
            }
        }
    }

    fun unstakeMgt() {
        viewModelScope.launch {
            val user = activeUser.value ?: return@launch
            if (user.stakedBalance <= 0) {
                triggerToast("No active staked balance found.")
                return@launch
            }
            val success = repository.claimStakedPayout(user.email)
            if (success) {
                triggerToast("Claimed Principal + Accrued Passive Staking Yields!")
            } else {
                triggerToast("Unstaking operation failed.")
            }
        }
    }

    fun swapMgt(amount: Double, receiveSymbol: String, convertedAmount: Double) {
        viewModelScope.launch {
            val user = activeUser.value ?: return@launch
            if (amount <= 0.0) {
                triggerToast("Please enter a valid swap amount.")
                return@launch
            }
            if (user.mainBalance < amount) {
                triggerToast("Insufficient liquid MGT balance.")
                return@launch
            }
            val success = repository.simulateSwapTokens(user.email, amount)
            if (success) {
                triggerToast("Swapped ${String.format("%.2f", amount)} MGT to receive ${String.format("%.4f", convertedAmount)} $receiveSymbol!")
            } else {
                triggerToast("Swap execution failed.")
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.logoutActiveUser()
            triggerToast("Session logged out.")
            onSuccess()
        }
    }
}
