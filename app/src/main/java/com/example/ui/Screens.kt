package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.theme.*
import kotlin.random.Random
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// --- Multi-Language Translation System ---
object I18n {
    private val translations = mapOf(
        "EN" to mapOf(
            "greeting_morning" to "Good Morning,",
            "greeting_afternoon" to "Good Afternoon,",
            "greeting_evening" to "Good Evening,",
            "mining_power" to "ACTIVE MINING POWER",
            "today_mined" to "Mined Today",
            "balance_pending" to "Live Mining Yield",
            "withdrawable" to "Withdrawable Balance",
            "claim" to "CLAIM TO WALLET",
            "buy_power" to "Buy Power",
            "withdraw" to "Withdraw",
            "miners" to "Active Miners",
            "no_miners" to "No Miners Active",
            "buy_first_rig" to "Unlock your first mining rig card to generate passive crypto rewards!",
            "comb_hash" to "COMBINED HASH RATE",
            "earning_rate" to "Earning Rate",
            "per_day" to "per day",
            "per_min" to "per minute",
            "block_cycle" to "BLOCK TIMING CYCLE",
            "wallet_addr" to "Wallet Address",
            "connect_wallet" to "Connect Wallet",
            "disconnect" to "Disconnect Wallet",
            "network" to "Network",
            "min_withdraw" to "Min withdrawal requirement",
            "net_fee" to "Network Fee",
            "amt_receive" to "You will receive",
            "tx_history" to "Transaction History",
            "settings" to "Settings",
            "save" to "Save Profile",
            "profile" to "Profile Summary",
            "username" to "Username",
            "biometric" to "Enable Biometric Security",
            "def_currency" to "Display Currency",
            "def_lang" to "Application Language",
            "terms" to "Terms of Service & Privacy Policy",
            "logout" to "Secure Logout",
            "delete_acc" to "Delete Account",
            "confirm_del" to "Irreversible account deletion. Proceed?",
            "cancel" to "Cancel",
            "submit" to "Submit"
        ),
        "RU" to mapOf(
            "greeting_morning" to "Доброе утро,",
            "greeting_afternoon" to "Добрый день,",
            "greeting_evening" to "Добрый вечер,",
            "mining_power" to "АКТИВНЫЙ ХЕШРЕЙТ",
            "today_mined" to "Добыто сегодня",
            "balance_pending" to "Текущая доходность",
            "withdrawable" to "Доступно к выводу",
            "claim" to "ЗАБРАТЬ НА БАЛАНС",
            "buy_power" to "Купить мощность",
            "withdraw" to "Вывести",
            "miners" to "Активные майнеры",
            "no_miners" to "Нет активных майнеров",
            "buy_first_rig" to "Приобретайте оборудование, чтобы начать добычу токенов!",
            "comb_hash" to "ОБЩАЯ МОЩНОСТЬ СЕТИ",
            "earning_rate" to "Прогноз доходности",
            "per_day" to "в сутки",
            "per_min" to "в минуту",
            "block_cycle" to "ВРЕМЯ СЛЕДУЮЩЕГО БЛОКА",
            "wallet_addr" to "Адрес криптокошелька",
            "connect_wallet" to "Подключить кошелек",
            "disconnect" to "Отключить кошелек",
            "network" to "Криптосеть",
            "min_withdraw" to "Минимум к выводу",
            "net_fee" to "Комиссия блокчейна",
            "amt_receive" to "Вы получите",
            "tx_history" to "История транзакций",
            "settings" to "Настройки",
            "save" to "Сохранить профиль",
            "profile" to "Данные пользователя",
            "username" to "Имя профиля",
            "biometric" to "Разблокировка по биометрии",
            "def_currency" to "Валюта отображения",
            "def_lang" to "Язык интерфейса",
            "terms" to "Условия обслуживания и конфиденциальность",
            "logout" to "Выйти из системы",
            "delete_acc" to "Удалить аккаунт",
            "confirm_del" to "Это полностью удалит ваши данные. Продолжить?",
            "cancel" to "Отмена",
            "submit" to "Подтвердить"
        ),
        "ES" to mapOf(
            "greeting_morning" to "Buenos Días,",
            "greeting_afternoon" to "Buenas Tardes,",
            "greeting_evening" to "Buenas Noches,",
            "mining_power" to "POTENCIA DE MINERÍA",
            "today_mined" to "Minado Hoy",
            "balance_pending" to "Rendimiento en Vivo",
            "withdrawable" to "Saldo Retirable",
            "claim" to "RECLAMAR AL SALDO",
            "buy_power" to "Comprar Rigs",
            "withdraw" to "Retirar",
            "miners" to "Mineros Activos",
            "no_miners" to "Sin Mineros Activos",
            "buy_first_rig" to "¡Adquiere tu primer rig de minería para obtener dividendos pasivos!",
            "comb_hash" to "TASA DE HASH DE RED",
            "earning_rate" to "Tasa de Rendimiento",
            "per_day" to "por día",
            "per_min" to "por minuto",
            "block_cycle" to "TIEMPO PARA PRÓXIMO BLOQUE",
            "wallet_addr" to "Dirección del Monedero",
            "connect_wallet" to "Conectar Monedero",
            "disconnect" to "Desconectar Monedero",
            "network" to "Red de Blockchain",
            "min_withdraw" to "Retiro mínimo requerido",
            "net_fee" to "Comisión de Red",
            "amt_receive" to "Usted Recibirá",
            "tx_history" to "Historial de Transacciones",
            "settings" to "Configuraciones",
            "save" to "Guardar Configuración",
            "profile" to "Detalle del Perfil",
            "username" to "Nombre de Usuario",
            "biometric" to "Habilitar Acceso Biométrico",
            "def_currency" to "Moneda Predeterminada",
            "def_lang" to "Idioma del Aplicativo",
            "terms" to "Términos del Servicio y Privacidad",
            "logout" to "Cerrar Sesión",
            "delete_acc" to "Eliminar Cuenta",
            "confirm_del" to "¿Está seguro de eliminar su cuenta permanentemente?",
            "cancel" to "Cancelar",
            "submit" to "Enviar"
        )
    )

    fun get(key: String, lang: String): String {
        return translations[lang]?.get(key) ?: translations["EN"]?.get(key) ?: key
    }
}

// Helper to format currency conversion based on user preferences
fun formatCurrency(amountMgt: Double, mgtPriceInUsd: Double, currency: String): String {
    val amountUsd = amountMgt * mgtPriceInUsd
    return when (currency) {
        "EUR" -> String.format("€%.2f", amountUsd * 0.92)
        "GBP" -> String.format("£%.2f", amountUsd * 0.78)
        "RUB" -> String.format("%.2f ₽", amountUsd * 90.5)
        else -> String.format("$%.2f", amountUsd)
    }
}

// UI Wrapper Card
@Composable
fun GlowCard(
    modifier: Modifier = Modifier,
    borderWidth: Double = 1.0,
    glowColor: Color = NeonCyan.copy(alpha = 0.3f),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(borderWidth.dp, glowColor),
        modifier = modifier.drawBehind {
            // Subtle ambient neon blur
            drawRoundRect(
                color = glowColor.copy(alpha = 0.05f),
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx())
            )
        },
        content = content
    )
}

// --- SCREEN 1: AUTHENTICATION AND REGISTRATION ---
@Composable
fun AuthScreen(
    vm: MineForgeViewModel,
    onAuthSuccess: () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(true) }
    var emailInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    // Floating glowing elements logic
    val infiniteTransition = rememberInfiniteTransition(label = "authGlow")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cyanPulsate"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonBlack)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Futuristic background gradients
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonCyan.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(size.width * 0.1f, size.height * 0.2f),
                    radius = size.width * 0.6f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(CyberPurple.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(size.width * 0.9f, size.height * 0.8f),
                    radius = size.width * 0.6f
                )
            )
        }

        Column(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Core Branding App Logo Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Logo",
                    tint = NeonCyan,
                    modifier = Modifier
                        .size(44.dp)
                        .rotate(alphaAnim * 360f) // continuous rotation
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "MINEFORGE",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    color = Color.White,
                    style = LocalTextStyle.current.copy(
                        shadow = Shadow(
                            color = NeonCyan,
                            offset = Offset(0f, 0f),
                            blurRadius = alphaAnim * 12f
                        )
                    )
                )
            }

            Text(
                text = "NEXT-GEN CLOUD MINING ACCELERATOR",
                color = NeonCyan.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Auth Input Panel Wrapper
            GlowCard(
                glowColor = NeonCyan.copy(alpha = alphaAnim * 0.4f),
                borderWidth = 1.2,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isLoginMode) "SYSTEM ENTRY" else "SECURE REGISTRATION",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (!isLoginMode) {
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Display Name", color = Color.Gray) },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = CardBorderDark,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("auth_name_input")
                        )
                    }

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Crypto Email Address", color = Color.Gray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CardBorderDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("auth_email_input")
                    )

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Security Access Key", color = Color.Gray) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CardBorderDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("auth_pass_input")
                    )

                    // Actions block
                    if (isLoginMode) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "Forgot access key?",
                                color = NeonCyan.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .clickable { showForgotPasswordDialog = true }
                                    .padding(vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (isLoginMode) {
                                vm.loginWithEmail(emailInput, passwordInput, onAuthSuccess)
                            } else {
                                vm.registerWithEmail(emailInput, nameInput, passwordInput, onAuthSuccess)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("auth_submit_button"),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text(
                            text = if (isLoginMode) "INITIALIZE SENSOR" else "FORGE SECURITY MATRIX",
                            color = CarbonBlack,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    }

                    // Toggle mode link
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isLoginMode) "New miner node in sector?" else "Already authorized operator?",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isLoginMode) "Sign Up" else "Log In",
                            fontSize = 13.sp,
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { isLoginMode = !isLoginMode }
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }

            // Divider styled divider limit line
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = CardBorderDark)
                Text(
                    text = " SECURED REPLICA DECK ",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = CardBorderDark)
            }

            // Simulated Google OAuth Authentication Action
            OutlinedButton(
                onClick = {
                    vm.handleGoogleOneTapLogin(
                        email = "google.partner@gmail.com",
                        name = "Google Agent",
                        onSuccess = onAuthSuccess
                    )
                },
                border = BorderStroke(1.dp, CyberPurple.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("google_login_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    tint = CyberPurple,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "AUTHENTICATE WITH GOOGLE ONE-TAP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }

    // Forgot password modal
    if (showForgotPasswordDialog) {
        Dialog(onDismissRequest = { showForgotPasswordDialog = false }) {
            GlowCard(
                glowColor = CyberPurple,
                borderWidth = 1.5,
                modifier = Modifier.widthIn(max = 380.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "RECOVER OPERATOR ACCESS KEY",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Enter your registered crypto email address to transmit a simulation password recovery link.",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )

                    var recoveryMail by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = recoveryMail,
                        onValueChange = { recoveryMail = it },
                        label = { Text("Email address", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CardBorderDark,
                            focusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showForgotPasswordDialog = false }) {
                            Text("CANCEL", color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                if (recoveryMail.isNotBlank()) {
                                    vm.requestPasswordReset(recoveryMail)
                                    showForgotPasswordDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                        ) {
                            Text("TRANSMIT Link", color = CarbonBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


// --- SCREEN 2: HOME SCREEN (DASHBOARD) ---
@Composable
fun DashboardScreen(
    user: UserAccount,
    vm: MineForgeViewModel,
    onNavigateToTab: (Int) -> Unit
) {
    val totalHashPower by vm.totalHashPowerMhs.collectAsStateWithLifecycle()
    val livePending by vm.livePendingEarnings.collectAsStateWithLifecycle()
    val totalRigs by vm.totalActiveRigsCount.collectAsStateWithLifecycle()
    val tokenPrice by vm.tokenPriceUsd.collectAsStateWithLifecycle()
    
    val autoClaimProgress by vm.autoClaimProgress.collectAsStateWithLifecycle()
    val autoClaimCountdown by vm.autoClaimCountdown.collectAsStateWithLifecycle()
    val isAutoClaimEnabled by vm.isAutoClaimEnabled.collectAsStateWithLifecycle()

    val lang = user.preferredLanguage
    val currency = user.preferredCurrency

    // Dynamic greeting based on UTC Hour
    val hour = Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.HOUR_OF_DAY)
    val greetingPrefix = when (hour) {
        in 5..11 -> I18n.get("greeting_morning", lang)
        in 12..17 -> I18n.get("greeting_afternoon", lang)
        else -> I18n.get("greeting_evening", lang)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulseGlow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonBlack)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and User Profile Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "$greetingPrefix ${user.displayName}!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Sector node address: ${user.email}",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(DeepCyanBg)
                    .border(1.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ManageAccounts,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Live Market peg dashboard summary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CardBorderDark.copy(alpha = 0.4f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("MGT Token Index (Pegged ETH 0.000051)", fontSize = 11.sp, color = Color.LightGray, fontWeight = FontWeight.Bold)
            }
            Text("$$tokenPrice USD", fontSize = 11.sp, color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.ExtraBold)
        }

        // Mining Hash Power Banner
        GlowCard(
            glowColor = NeonCyan,
            borderWidth = 1.5,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = I18n.get("mining_power", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = NeonCyan
                )

                // Large Animated Power Count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Memory,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier
                            .size(36.dp)
                            .rotate(pulseScale * 360f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (totalHashPower >= 1000.0) {
                            String.format("%.2f GH/s", totalHashPower / 1000.0)
                        } else {
                            String.format("%.0f MH/s", totalHashPower)
                        },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Text(
                    text = "${I18n.get("miners", lang)}: $totalRigs Nodes Operational",
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            }
        }

        // Real-Time Token Accumulation Auto-Sync Progress Card
        GlowCard(
            glowColor = NeonCyan,
            borderWidth = 1.0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AUTOMATED BALANCE SYNCHRONIZER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (isAutoClaimEnabled) "Syncing accumulated tokens to Vault..." else "Auto-Sync Paused",
                            fontSize = 11.sp,
                            color = Color.LightGray
                        )
                    }
                    
                    Switch(
                        checked = isAutoClaimEnabled,
                        onCheckedChange = { vm.toggleAutoClaim(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CarbonBlack,
                            checkedTrackColor = NeonCyan,
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = SurfaceDark
                        ),
                        modifier = Modifier.scale(0.85f).testTag("auto_claim_switch")
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(CardBorderDark)
                ) {
                    LinearProgressIndicator(
                        progress = { autoClaimProgress },
                        color = NeonCyan,
                        trackColor = Color.Transparent,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Next Wallet Update: ${autoClaimCountdown}s",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Accumulated: +${String.format("%.5f", livePending)} MGT",
                        fontSize = 11.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Main Wallet withdrawable card vs daily accrued mines
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Accrued (Pending Claim) Box
            GlowCard(
                modifier = Modifier.weight(1f),
                glowColor = CyberPurple,
                borderWidth = 1.0
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = I18n.get("balance_pending", lang).uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPurple,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = String.format("%.5f", livePending),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = formatCurrency(livePending, tokenPrice, currency),
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )

                    Button(
                        onClick = { vm.claimPendingMines() },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("dashboard_claim_button")
                    ) {
                        Text("CLAIM TO SECURE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            // Main Wallet Balance Box
            GlowCard(
                modifier = Modifier.weight(1f),
                glowColor = NeonCyan,
                borderWidth = 1.0
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = I18n.get("withdrawable", lang).uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = String.format("%.2f MGT", user.mainBalance),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = formatCurrency(user.mainBalance, tokenPrice, currency),
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )

                    Button(
                        onClick = { onNavigateToTab(2) }, // Tab 2 -> Balance & Withdraw
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("dashboard_withdraw_link")
                    ) {
                        Text(I18n.get("withdraw", lang).uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CarbonBlack)
                    }
                }
            }
        }

        // Stats Ledger Dashboard List
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PORTFOLIO TRACKER",
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            HorizontalDivider(modifier = Modifier.weight(1f), color = CardBorderDark)
        }

        // Aggregate statistics details
        GlowCard(borderWidth = 0.5, glowColor = CardBorderDark) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Mined Lifetime:", color = Color.Gray, fontSize = 13.sp)
                    Text(
                        text = String.format("%.2f MGT", user.totalMinedLifetime),
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Connected Wallet Node:", color = Color.Gray, fontSize = 13.sp)
                    val walletStr = user.connectedWalletAddress?.let {
                        if (it.length > 12) "${it.take(6)}...${it.takeLast(4)}" else it
                    } ?: "None connected"
                    Text(
                        text = walletStr,
                        color = if (user.connectedWalletAddress != null) NeonCyan else Color.Red,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Block Validation State:", color = Color.Gray, fontSize = 13.sp)
                    Text(
                        text = "SYNCHRONIZED ACTIVE",
                        color = Color.Green,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bottom Action Launcher Quick Access
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onNavigateToTab(1) }, // Navigation tab 1 -> Mining / Store
                colors = ButtonDefaults.buttonColors(containerColor = DeepCyanBg),
                border = BorderStroke(1.dp, NeonCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = NeonCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text("BUY RIG MATRIX", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }
    }
}


// --- SCREEN 3: MINING MANAGEMENT DECK ---
@Composable
fun MiningScreen(
    user: UserAccount,
    vm: MineForgeViewModel,
    onOpenRigStore: () -> Unit
) {
    val rigs by vm.activeRigs.collectAsStateWithLifecycle()
    val totalHashPower by vm.totalHashPowerMhs.collectAsStateWithLifecycle()
    val livePending by vm.livePendingEarnings.collectAsStateWithLifecycle()
    val tokenPrice by vm.tokenPriceUsd.collectAsStateWithLifecycle()

    val customInterval by vm.customIntervalSeconds.collectAsStateWithLifecycle()
    val isIntervalActive by vm.isIntervalMiningActive.collectAsStateWithLifecycle()
    val lastIncrement by vm.lastIncrementAmount.collectAsStateWithLifecycle()
    val tickPulse by vm.tickPulseTrigger.collectAsStateWithLifecycle()

    val lang = user.preferredLanguage
    val currency = user.preferredCurrency

    // Compute combining rates
    val totalDailyMgt = rigs.sumOf { it.dailyEarnings }
    val mgtPerMinute = totalDailyMgt / 1440.0

    val autoClaimProgress by vm.autoClaimProgress.collectAsStateWithLifecycle()
    val autoClaimCountdown by vm.autoClaimCountdown.collectAsStateWithLifecycle()

    // Rotator infinite animation parameters
    val transition = rememberInfiniteTransition(label = "fanRotator")
    val rotAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ),
        label = "rotationAngle"
    )

    var selectedMiningSubTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonBlack)
            .padding(16.dp)
    ) {
        // Tab Row switcher between ACTIVE NODES and STORE catalog storefront
        TabRow(
            selectedTabIndex = selectedMiningSubTab,
            containerColor = SurfaceDark,
            contentColor = NeonCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedMiningSubTab]),
                    color = NeonCyan
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, CardBorderDark, RoundedCornerShape(8.dp))
        ) {
            Tab(
                selected = (selectedMiningSubTab == 0),
                onClick = { selectedMiningSubTab = 0 },
                text = {
                    Text(
                        "OPERATIONAL NODES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = if (selectedMiningSubTab == 0) NeonCyan else Color.Gray
                    )
                },
                modifier = Modifier.testTag("mining_nodes_tab")
            )
            Tab(
                selected = (selectedMiningSubTab == 1),
                onClick = { selectedMiningSubTab = 1 },
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = if (selectedMiningSubTab == 1) NeonCyan else Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            "RIG STORE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = if (selectedMiningSubTab == 1) NeonCyan else Color.Gray
                        )
                    }
                },
                modifier = Modifier.testTag("mining_store_tab")
            )
        }

        if (selectedMiningSubTab == 0) {
        // Upper stats board
        GlowCard(
            glowColor = NeonCyan,
            borderWidth = 1.0,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = I18n.get("comb_hash", lang),
                    fontSize = 11.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Memory, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (totalHashPower >= 1000.0) {
                            String.format("%.2f GH/s", totalHashPower / 1000.0)
                        } else {
                            String.format("%.0f MH/s", totalHashPower)
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }

                HorizontalDivider(color = CardBorderDark, modifier = Modifier.padding(vertical = 4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "ACCUMULATED MINED DAILY", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(
                            text = String.format("%.2f MGT", totalDailyMgt),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "MGT REWARD / MINUTE", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(
                            text = String.format("%.5f MGT", mgtPerMinute),
                            color = NeonCyan,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Live Yield Block Cycle progress display
        GlowCard(
            glowColor = CyberPurple,
            borderWidth = 1.0,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(60.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { autoClaimProgress },
                        color = CyberPurple,
                        trackColor = CardBorderDark,
                        strokeWidth = 6.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "${autoClaimCountdown}s",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = I18n.get("block_cycle", lang),
                        fontSize = 11.sp,
                        color = CyberPurple,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Validating next block rewards sequence containing in-app MGT coins.",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pending Claims: ${String.format("%.5f", livePending)} MGT",
                        fontSize = 13.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                IconButton(
                    onClick = { vm.claimPendingMines() },
                    modifier = Modifier
                        .size(44.dp)
                        .background(CyberPurple, CircleShape)
                        .testTag("mining_claim_badge")
                ) {
                    Icon(Icons.Default.DownloadDone, contentDescription = null, tint = Color.White)
                }
            }
        }

        // REACT-STATE INTERVAL MINING LOOP PANEL (satisfying real-time incremental system)
        GlowCard(
            glowColor = if (isIntervalActive) NeonCyan else Color.Gray,
            borderWidth = 1.0,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Title element with a glowing/pulsating dot
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pulsing status LED dot
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isIntervalActive) Color.Green else Color.Red)
                        )
                        Text(
                            text = "REACT-STATE MINING LOOP",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (isIntervalActive) NeonCyan else Color.Gray,
                            letterSpacing = 1.sp
                        )
                    }

                    // A badge showing "ACTIVE" or "PAUSED"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isIntervalActive) NeonCyan.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isIntervalActive) "ACTIVE" else "PAUSED",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.sp,
                            color = if (isIntervalActive) NeonCyan else Color.Red
                        )
                    }
                }

                Text(
                    text = "A reactive dynamic background loop that automatically increments your primary wallet balance in real-time, matching your rig speed over configurable intervals.",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    lineHeight = 13.sp
                )

                // Selectable intervals slider-row
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "SET TIME INTERVAL / REFRESH VELOCITY:",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.LightGray,
                        fontFamily = FontFamily.Monospace
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val intervalOptions = listOf(1, 3, 5, 10)
                        intervalOptions.forEach { seconds ->
                            val isSelected = (customInterval == seconds)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) NeonCyan else CardBorderDark)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) NeonCyan else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        vm.setCustomInterval(seconds)
                                    }
                                    .testTag("interval_btn_${seconds}s")
                            ) {
                                Text(
                                    text = "${seconds}s",
                                    color = if (isSelected) CarbonBlack else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                // Dynamic formula and feedback indicators
                val baseMultiplier = 0.000005787
                val tickYield = totalHashPower * baseMultiplier * customInterval

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceDark)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "EST. RATE REWARD / TICK",
                            fontSize = 8.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = String.format("+%.6f MGT", tickYield),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Pulse status block animation lines
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) { index ->
                            // Dynamically illuminate blocks based on tick pulse trigger
                            val isLit = isIntervalActive && (tickPulse % 5 == index)
                            Box(
                                modifier = Modifier
                                    .size(width = 4.dp, height = 12.dp)
                                    .clip(RoundedCornerShape(1.dp))
                                    .background(
                                        if (isLit) NeonCyan.copy(alpha = 1f)
                                        else NeonCyan.copy(alpha = 0.15f)
                                    )
                            )
                        }
                    }
                }

                // Interaction action button and dynamic real-time label
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { vm.toggleIntervalMining(!isIntervalActive) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isIntervalActive) Color.Red.copy(alpha = 0.15f) else DeepCyanBg
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isIntervalActive) Color.Red.copy(alpha = 0.5f) else NeonCyan
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("interval_toggle_btn")
                    ) {
                        Text(
                            text = if (isIntervalActive) "PAUSE LOOP" else "RESUME LOOP",
                            color = if (isIntervalActive) Color.Red else NeonCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    if (isIntervalActive && lastIncrement > 0.0) {
                        Text(
                            text = String.format("Last: +%.5f MGT", lastIncrement),
                            color = Color.Green,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        Text(
                            text = "Idle (Awaiting cycle)",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Live visual mining rate tracer
        HashRateVisualizer(
            totalHashPower = totalHashPower,
            userRigsCount = rigs.size,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Header for Owned Miners
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${I18n.get("miners", lang).uppercase()} IN OPERATION (${rigs.size})",
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "+ ADD NODE",
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.clickable { selectedMiningSubTab = 1 }
            )
        }

        // Lazy scrolling active rig grid or empty state alert
        if (rigs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceDark)
                    .border(1.dp, CardBorderDark, RoundedCornerShape(16.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Devices,
                        contentDescription = null,
                        tint = NeonCyan.copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = I18n.get("no_miners", lang),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = I18n.get("buy_first_rig", lang),
                        color = Color.Gray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { selectedMiningSubTab = 1 },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                    ) {
                        Text("OPEN SECURE STORE", color = CarbonBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rigs) { rig ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, CardBorderDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // spinning fan simulation representation
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(DeepCyanBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = NeonCyan,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .rotate(rotAngle) // automated continuous rotation
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = rig.name,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "Speed: ${rig.hashRateMhs.toInt()} MH/s",
                                            color = NeonCyan,
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "Yield: ${rig.dailyEarnings.toInt()} MGT/day",
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                // Static details of date
                                val simpleFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
                                Text(
                                    text = simpleFormat.format(Date(rig.purchaseTimestamp)),
                                    color = Color.DarkGray,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            // Dynamic Mining Progress: Starter (10s), Advanced (8s), Pro (5s), Elite (3s)
                            val blockCycleDurationMs = when (rig.tierName.uppercase()) {
                                "STARTER" -> 10000L
                                "ADVANCED" -> 8000L
                                "PRO" -> 5000L
                                "ELITE" -> 3000L
                                else -> 6000L
                            }

                            // Create an animation state representing sub-cycle progress
                            var rigProgress by remember { mutableStateOf(0f) }
                            LaunchedEffect(key1 = rig.purchaseTimestamp) {
                                while (true) {
                                    delay(100)
                                    val elapsed = (System.currentTimeMillis() - rig.purchaseTimestamp) % blockCycleDurationMs
                                    rigProgress = elapsed.toFloat() / blockCycleDurationMs.toFloat()
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "MINING BLOCK CYCLE PROGRESS",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = String.format("%.0f%%", rigProgress * 100f),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(CardBorderDark)
                                ) {
                                    LinearProgressIndicator(
                                        progress = { rigProgress },
                                        color = NeonCyan,
                                        trackColor = Color.Transparent,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        } else {
            // RIGS STORE SYSTEM STOREFRONT TAB (incorporating rich descriptions and specs!)
            val storeTiers = remember {
                listOf(
                    StoreRigSpecs("STARTER PACKAGE", "10 MH/s", 9.99, 5.0, Icons.Default.Hardware, "Basic operational node module. Perfect for beginner cryptocurrency enthusiasts.", "128 CUDA Cores", "Single 120mm Hydraulic Fan", "35W TDP", "15-Day Est. Profit Threshold"),
                    StoreRigSpecs("ADVANCED BLOCKER", "50 MH/s", 49.99, 30.0, Icons.Default.DeveloperBoard, "Accelerated processor card. Upgraded cores build hashes rapidly.", "512 Tensor Cores", "Dual-Bearing Extreme Vapor Chamber", "95W TDP", "10-Day Est. Profit Threshold"),
                    StoreRigSpecs("PRO HASHING ENGINE", "200 MH/s", 199.99, 140.0, Icons.Default.Memory, "Premium host shelf containing array stacks. Unmatched liquid hash speed.", "2048 Intelligent Compute Pipelines", "Triple Smart Fluid Jets", "240W TDP", "7-Day Est. Profit Threshold", isPopular = true),
                    StoreRigSpecs("ELITE TITAN RIG", "1 GH/s", 999.99, 800.0, Icons.Default.Computer, "Powerhouse datacenter server matrix. Extreme corporate-grade volumes.", "8400 Superconducting Core Chips", "In-line Cryogenic Cooling Block", "800W TDP", "5-Day Est. Profit Threshold")
                )
            }

            // Dynamic hashrate project slider simulator
            var sliderRigsCount by remember { mutableStateOf(1f) }

            Text(
                text = "MINING RIG MATRIX MARKET",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Simulating Yield Widget
            GlowCard(
                glowColor = NeonCyan,
                borderWidth = 0.5,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "MULTIPLIER ROI SIMULATOR",
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "Forecast scaling yields by adjusting simulated miners:",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(11.dp)
                    ) {
                        Text(
                            text = "Miners: ${sliderRigsCount.toInt()}",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonCyan,
                            fontSize = 13.sp
                        )
                        Slider(
                            value = sliderRigsCount,
                            onValueChange = { sliderRigsCount = it },
                            valueRange = 1f..10f,
                            steps = 8,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonCyan,
                                activeTrackColor = NeonCyan,
                                inactiveTrackColor = Color.DarkGray
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Simulated MGT Daily yield:", color = Color.Gray, fontSize = 11.sp)
                        Text(
                            text = "${(sliderRigsCount.toInt() * 30.0).toInt()} MGT/day",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(storeTiers) { t ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (t.isPopular) DeepCyanBg else CardBorderDark.copy(alpha = 0.3f))
                            .border(
                                width = 1.dp,
                                color = if (t.isPopular) NeonCyan else CardBorderDark,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (t.isPopular) CyberPurple.copy(alpha = 0.2f) else CardBorderDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = t.icon,
                                contentDescription = null,
                                tint = if (t.isPopular) CyberPurple else NeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = t.title,
                                    fontWeight = FontWeight.Bold,
                                    color = if (t.isPopular) NeonCyan else Color.White,
                                    fontSize = 12.sp
                                )
                                if (t.isPopular) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(CyberPurple)
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text("BEST ROI", fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Text(text = t.desc, color = Color.Gray, fontSize = 10.sp, lineHeight = 12.sp)
                            
                            // Specs block (using standard row layouts for maximum compile protection)
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Black.copy(alpha = 0.4f))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "Cores: ${t.cores}", fontSize = 8.sp, color = Color.LightGray, fontFamily = FontFamily.Monospace)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Black.copy(alpha = 0.4f))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "TDP: ${t.power}", fontSize = 8.sp, color = Color.LightGray, fontFamily = FontFamily.Monospace)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Black.copy(alpha = 0.4f))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "Est. Profit: ${t.roi}", fontSize = 8.sp, color = NeonCyan, fontFamily = FontFamily.Monospace)
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Speed: ${t.speed}", color = NeonCyan, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                Text("Yield: ${t.dailyReward.toInt()} MGT/d", color = Color.LightGray, fontSize = 11.sp)
                            }
                        }

                        // Buy action button
                        Button(
                            onClick = {
                                val mhsValue = if (t.speed.contains("GH/s")) 1000.0 else t.speed.replace(" MH/s", "").toDouble()
                                vm.purchaseRigTier(
                                    tierName = t.title.replace(" TIER", "").replace(" PACKAGE", "").replace(" ENGINE", "").replace(" BLOCKER", "").replace(" RIG", ""),
                                    priceUsd = t.price,
                                    hashRateMhs = mhsValue,
                                    dailyEarnings = t.dailyReward
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("buy_market_btn_${t.title.lowercase().replace(" ", "_")}")
                        ) {
                            Text("$${t.price}", color = CarbonBlack, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}

data class StoreRigSpecs(
    val title: String,
    val speed: String,
    val price: Double,
    val dailyReward: Double,
    val icon: ImageVector,
    val desc: String,
    val cores: String,
    val fanSpeed: String,
    val power: String,
    val roi: String,
    val isPopular: Boolean = false
)


// --- SCREEN 4: BALANCE, WALLET MATRIX AND WITHDRAWALS ---
@Composable
fun BalanceScreen(
    user: UserAccount,
    vm: MineForgeViewModel
) {
    val withdrawals by vm.activeWithdrawals.collectAsStateWithLifecycle()
    val tokenPrice by vm.tokenPriceUsd.collectAsStateWithLifecycle()
    val lang = user.preferredLanguage
    val currency = user.preferredCurrency

    var showWalletModal by remember { mutableStateOf(false) }
    var withdrawalAmountInput by remember { mutableStateOf("") }
    var networkSelected by remember { mutableStateOf("Ethereum Base") }

    // Interactive custom WithdrawalDialog stats and states
    var showWithdrawModal by remember { mutableStateOf(false) }
    var modalWithdrawAmount by remember { mutableStateOf(0.0) }
    var selectedFeePriority by remember { mutableStateOf("Standard") }
    var isWithdrawalSimulating by remember { mutableStateOf(false) }
    var withdrawalSimulationProgress by remember { mutableStateOf(0f) }
    var withdrawalSimulationPhase by remember { mutableStateOf("") }
    var withdrawalSuccessState by remember { mutableStateOf(false) }
    var confirmedTxHash by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonBlack)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Vault summary Header
        GlowCard(
            glowColor = NeonCyan,
            borderWidth = 1.2,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = I18n.get("withdrawable", lang).uppercase(),
                    color = Color.Gray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = String.format("%.2f MGT", user.mainBalance),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "≈ " + formatCurrency(user.mainBalance, tokenPrice, currency),
                    color = NeonCyan,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Web3 Wallet connector segment
        GlowCard(
            glowColor = if (user.connectedWalletAddress != null) NeonCyan else Color.Red,
            borderWidth = 0.8
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = null,
                            tint = if (user.connectedWalletAddress != null) NeonCyan else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WEB3 COLD WALLET DECK",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    if (user.connectedWalletAddress != null) {
                        Text(
                            text = "[CONNECTED]",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Green,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                if (user.connectedWalletAddress != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Active address Link:", color = Color.Gray, fontSize = 11.sp)
                        Text(
                            text = user.connectedWalletAddress,
                            color = NeonCyan,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Active Network: ${user.walletNetwork ?: networkSelected}",
                                color = Color.LightGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = I18n.get("disconnect", lang),
                                color = Color.Red,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable { vm.disconnectWallet() }
                                    .padding(4.dp)
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = { showWalletModal = true },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("connect_wallet_button")
                    ) {
                        Text(I18n.get("connect_wallet", lang).uppercase(), color = CarbonBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        var activeBalanceSubTab by remember { mutableStateOf(0) }

        TabRow(
            selectedTabIndex = activeBalanceSubTab,
            containerColor = SurfaceDark,
            contentColor = NeonCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeBalanceSubTab]),
                    color = NeonCyan
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, CardBorderDark, RoundedCornerShape(8.dp))
        ) {
            Tab(
                selected = (activeBalanceSubTab == 0),
                onClick = { activeBalanceSubTab = 0 },
                text = { Text("WITHDRAWAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
                modifier = Modifier.testTag("balance_tab_withdraw")
            )
            Tab(
                selected = (activeBalanceSubTab == 1),
                onClick = { activeBalanceSubTab = 1 },
                text = { Text("DEFI STAKE", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
                modifier = Modifier.testTag("balance_tab_stake")
            )
            Tab(
                selected = (activeBalanceSubTab == 2),
                onClick = { activeBalanceSubTab = 2 },
                text = { Text("TOKEN SWAP", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
                modifier = Modifier.testTag("balance_tab_swap")
            )
        }

        if (activeBalanceSubTab == 0) {
            // Withdrawal request panel
        GlowCard(
            glowColor = NeonCyan.copy(alpha = 0.5f),
            borderWidth = 0.5
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "LAUNCH WITHDRAW TRANSIT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )

                OutlinedTextField(
                    value = withdrawalAmountInput,
                    onValueChange = { withdrawalAmountInput = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Transfer amount (MGT)", color = Color.Gray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CardBorderDark,
                        focusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("withdraw_amount_input")
                )

                // Computed metrics
                val amountVal = withdrawalAmountInput.toDoubleOrNull() ?: 0.0
                val networkFee = 1.25 // Standard validation network fee peg
                val outReceive = if (amountVal > networkFee) amountVal - networkFee else 0.0

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${I18n.get("net_fee", lang)}:", color = Color.Gray, fontSize = 12.sp)
                    Text(text = "$networkFee MGT", color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${I18n.get("amt_receive", lang)}:", color = Color.Gray, fontSize = 12.sp)
                    Text(text = String.format("%.2f MGT", outReceive), color = NeonCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val addr = user.connectedWalletAddress
                        if (addr == null) {
                            vm.triggerToast("Please deploy and connect Web3 Cold Wallet first.")
                            return@Button
                        }
                        if (amountVal < 100.0) {
                            vm.triggerToast("Minimum withdrawal transfer limit is 100 MGT.")
                            return@Button
                        }
                        if (user.mainBalance < amountVal) {
                            vm.triggerToast("Insufficient MGT liquid balance.")
                            return@Button
                        }
                        // Initialize states and open the custom interactive simulated modal
                        modalWithdrawAmount = amountVal
                        selectedFeePriority = "Standard"
                        isWithdrawalSimulating = false
                        withdrawalSimulationProgress = 0f
                        withdrawalSimulationPhase = ""
                        withdrawalSuccessState = false
                        confirmedTxHash = ""
                        showWithdrawModal = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanBorderOrGlow(user.connectedWalletAddress != null)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("withdraw_submit_button")
                ) {
                    Text("TRANSMIT COINS TO COLD BLOCK", color = CarbonBlack, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "* ${I18n.get("min_withdraw", lang)}: 100.00 MGT Tokens.",
                    color = Color.DarkGray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Historical listings
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = I18n.get("tx_history", lang).uppercase(),
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(12.dp))
            HorizontalDivider(modifier = Modifier.weight(1f), color = CardBorderDark)
        }

        if (withdrawals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark)
                    .border(1.dp, CardBorderDark, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("No past withdrawal metrics logged.", color = Color.Gray, fontSize = 12.sp)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                withdrawals.forEach { tx ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceDark)
                            .border(1.dp, CardBorderDark, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${tx.amount.toInt()} MGT",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = tx.network,
                                    color = NeonCyan,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = "TX: ${tx.txHash.take(18)}...",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // Status Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (tx.status) {
                                        "COMPLETED" -> Color.Green.copy(alpha = 0.15f)
                                        "PROCESSING" -> NeonCyan.copy(alpha = 0.15f)
                                        else -> Color.Yellow.copy(alpha = 0.15f)
                                    }
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tx.status,
                                color = when (tx.status) {
                                    "COMPLETED" -> Color.Green
                                    "PROCESSING" -> NeonCyan
                                    else -> Color.Yellow
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        } else if (activeBalanceSubTab == 1) {
            // DEFI STAKING NODE CARD
            GlowCard(glowColor = CyberPurple, borderWidth = 1.0) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DEFI NODE STAKING POOL",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = CyberPurple,
                            letterSpacing = 1.2.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("18.0% APY", color = NeonCyan, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    // Dynamic live interest counter
                    var liveInterest by remember { mutableStateOf(0.0) }
                    LaunchedEffect(user.lastStakeTimestamp, user.stakedBalance) {
                        if (user.stakedBalance > 0 && user.lastStakeTimestamp > 0) {
                            while (true) {
                                val elapsed = System.currentTimeMillis() - user.lastStakeTimestamp
                                val yearMs = 31536000000.0
                                liveInterest = user.stakedBalance * (elapsed.toDouble() / yearMs) * 0.18
                                delay(100)
                            }
                        } else {
                            liveInterest = 0.0
                        }
                    }

                    Column {
                        Text("Total Staked Principle", color = Color.Gray, fontSize = 11.sp)
                        Text(
                            text = String.format("%.2f MGT", user.stakedBalance),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    if (user.stakedBalance > 0) {
                        Column {
                            Text("Accrued Passive Reward Yield (Live)", color = CyberPurple, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = String.format("+%.6f MGT", liveInterest),
                                color = Color.Green,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    HorizontalDivider(color = CardBorderDark, thickness = 0.5.dp)

                    // Stake field
                    var stakeAmountInput by remember { mutableStateOf("") }
                    
                    OutlinedTextField(
                        value = stakeAmountInput,
                        onValueChange = { stakeAmountInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Stake Amount (MGT)", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberPurple,
                            unfocusedBorderColor = CardBorderDark,
                            focusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("stake_amount_input")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val v = stakeAmountInput.toDoubleOrNull() ?: 0.0
                                if (v <= 0.0) {
                                    vm.triggerToast("Please enter a valid amount to stake.")
                                    return@Button
                                }
                                vm.stakeMgt(v)
                                stakeAmountInput = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("stake_now_btn")
                        ) {
                            Text("LOCK STAKE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        if (user.stakedBalance > 0) {
                            Button(
                                onClick = { vm.unstakeMgt() },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).testTag("unstake_now_btn")
                            ) {
                                Text("UNSTAKE PRINCIPAL", color = CarbonBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Staking Info Board Card
            GlowCard(glowColor = NeonCyan.copy(alpha = 0.3f), borderWidth = 0.5) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("SMART CONTRACT VERIFICATION", fontSize = 10.sp, color = NeonCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Text(
                        text = "Staking utilizes algorithmic smart contract mechanics to lock and dispatch liquidity mining rewards back. Passive rewards calculate at a persistent 18.00% annual percentage yield, accrued in live real-time cycles.",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        } else {
            // --- CYBER SWAP ENGINE ---
            GlowCard(glowColor = NeonCyan, borderWidth = 0.8) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "LIQUID SWAP SWIFT-ENGINE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan,
                        letterSpacing = 1.2.sp
                    )

                    var swapAmountInput by remember { mutableStateOf("") }
                    var targetCoin by remember { mutableStateOf("USDT") } // choices "USDT", "ETH", "BNB"

                    val availableCoins = listOf("USDT", "ETH", "BNB")

                    // Currency switcher Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Receive Coin:", color = Color.Gray, fontSize = 11.sp)
                        availableCoins.forEach { c ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (targetCoin == c) DeepCyanBg else CardBorderDark)
                                    .border(1.dp, if (targetCoin == c) NeonCyan else Color.Transparent, RoundedCornerShape(6.dp))
                                    .clickable { targetCoin = c }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(c, color = if (targetCoin == c) NeonCyan else Color.LightGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = swapAmountInput,
                        onValueChange = { swapAmountInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Swap Amount (MGT)", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CardBorderDark,
                            focusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("swap_amount_input")
                    )

                    // Swap rate computation based on virtual pricing
                    val mgtIn = swapAmountInput.toDoubleOrNull() ?: 0.0
                    val totalValueUsd = mgtIn * tokenPrice
                    val receiveVal = when (targetCoin) {
                        "USDT" -> totalValueUsd
                        "ETH" -> totalValueUsd / 3400.0 // simulated ETH price
                        "BNB" -> totalValueUsd / 560.0 // simulated BNB price
                        else -> totalValueUsd
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.4f))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("MGT Price Peg:", color = Color.Gray, fontSize = 10.sp)
                            Text(String.format("$%.4f USD", tokenPrice), color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Est. Network Slip Fee:", color = Color.Gray, fontSize = 10.sp)
                            Text("0.25% (Gas Protected)", color = NeonCyan, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                        HorizontalDivider(color = CardBorderDark, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("You Will Receive:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = String.format("%.5f %s", receiveVal, targetCoin),
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (mgtIn <= 0.0) {
                                vm.triggerToast("Please enter a valid swap amount.")
                                return@Button
                            }
                            if (user.mainBalance < mgtIn) {
                                vm.triggerToast("Insufficient MGT liquid balance for swap.")
                                return@Button
                            }
                            vm.swapMgt(mgtIn, targetCoin, receiveVal)
                            swapAmountInput = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("swap_submit_btn")
                    ) {
                        Text("EXECUTE BLOCK SWAP", color = CarbonBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }

    // Connect wallet dialog block
    if (showWalletModal) {
        Dialog(onDismissRequest = { showWalletModal = false }) {
            GlowCard(
                glowColor = NeonCyan,
                borderWidth = 1.5,
                modifier = Modifier.widthIn(max = 380.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "CHOOSE COLD WALLET COUPLING",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    // Wallet Network radio Toggles
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CardBorderDark)
                            .padding(4.dp)
                    ) {
                        listOf("Ethereum Base", "BSC Smart Chain").forEach { net ->
                            val active = (net == networkSelected)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (active) SurfaceDark else Color.Transparent)
                                    .clickable { networkSelected = net }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = net,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (active) NeonCyan else Color.Gray,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    // Available Simulated wallet anchors
                    val wallets = listOf(
                        "MetaMask Secured" to Icons.Default.Security,
                        "Trust Crypto Node" to Icons.Default.CloudQueue,
                        "Rainbow Multi-Chain" to Icons.Default.FilterVintage,
                        "Coinbase Custody" to Icons.Default.OfflineBolt
                    )

                    wallets.forEach { (wName, icon) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, CardBorderDark, RoundedCornerShape(8.dp))
                                .clickable {
                                    // Generate mock genuine ERC20 address
                                    val chars = "0123456789abcdef"
                                    val randAddr = "0x" + (1..40).map { chars[Random.nextInt(chars.length)] }.joinToString("")
                                    vm.connectSimulatedWallet(randAddr, networkSelected)
                                    showWalletModal = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
                            Text(text = wName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    TextButton(
                        onClick = { showWalletModal = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("CANCEL", color = Color.Gray)
                    }
                }
            }
        }

        // Interactive simulated WithdrawalDialog modal
        if (showWithdrawModal) {
            Dialog(onDismissRequest = {
                if (!isWithdrawalSimulating && !withdrawalSuccessState) {
                    showWithdrawModal = false
                }
            }) {
                GlowCard(
                    glowColor = if (withdrawalSuccessState) Color.Green else (if (isWithdrawalSimulating) NeonCyan else CyberPurple),
                    borderWidth = 1.5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .testTag("withdraw_modal_container")
                ) {
                    Column(
                        modifier = Modifier
                            .background(SurfaceDark)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (withdrawalSuccessState) {
                            // Success confirmation page!
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = Color.Green,
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(
                                    "PAYOUT DISPATCH CONFIRMED",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    "Your digital hash tokens have successfully routed through smart node transmitters into your cold Web3 vault.",
                                    color = Color.Gray,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 14.sp
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black.copy(alpha = 0.5f))
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("SETTLED AMOUNT:", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        Text("${modalWithdrawAmount.toInt()} MGT", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("FEE DEDUCTED:", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        val feeAmt = when(selectedFeePriority) {
                                            "Eco" -> 0.50
                                            "Standard" -> 1.25
                                            else -> 3.50
                                        }
                                        Text("$feeAmt MGT", color = Color.Red, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("RECEIVING WALLET:", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        val walletLabel = (user.connectedWalletAddress ?: "").run {
                                            if (length > 14) take(8) + "..." + takeLast(6) else this
                                        }
                                        Text(walletLabel, color = NeonCyan, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("NETWORK ROUTED:", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        Text(user.walletNetwork ?: networkSelected, color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                    }
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text("TRANSACTION HASH:", color = Color.Gray, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                        Text(confirmedTxHash.take(24) + "...", color = Color.Green, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    }
                                }

                                Button(
                                    onClick = {
                                        showWithdrawModal = false
                                        withdrawalSuccessState = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth().testTag("withdraw_success_done")
                                ) {
                                    Text("DISMISS PROTOCOL", color = CarbonBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        } else if (isWithdrawalSimulating) {
                            // Processing progress screen!
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "BLOCK TRANSIT SIGNING...",
                                    color = NeonCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.2.sp
                                )

                                // Linear animated index
                                LinearProgressIndicator(
                                    progress = { withdrawalSimulationProgress },
                                    color = NeonCyan,
                                    trackColor = CardBorderDark,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                )

                                Text(
                                    text = withdrawalSimulationPhase,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    textAlign = TextAlign.Center,
                                    minLines = 2,
                                    maxLines = 2,
                                    lineHeight = 14.sp
                                )

                                CircularProgressIndicator(
                                    color = NeonCyan,
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        } else {
                            // Review details screen!
                            Text(
                                text = "SECURE OUTBOUND PAYOUT DIRECTIVE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = CyberPurple,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = "Establish and configure digital withdrawal dispatch rules prior to broadcasting payload contract.",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                lineHeight = 14.sp
                            )

                            // Selector priority block!
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "SELECT NETWORK GAS DHP SPEED:",
                                    color = Color.LightGray,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val priorityOptions = listOf("Eco", "Standard", "Turbo")
                                    priorityOptions.forEach { p ->
                                        val isSelected = (selectedFeePriority == p)
                                        val feeText = when(p) {
                                            "Eco" -> "0.50 MGT"
                                            "Standard" -> "1.25 MGT"
                                            else -> "3.50 MGT"
                                        }
                                        val descText = when(p) {
                                            "Eco" -> "4-6 min"
                                            "Standard" -> "1-2 min"
                                            else -> "Instant"
                                        }
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isSelected) CyberPurple else CardBorderDark)
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isSelected) CyberPurple else Color.Transparent,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .clickable { selectedFeePriority = p }
                                                .padding(vertical = 8.dp)
                                                .testTag("withdraw_fee_btn_${p}")
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(p.uppercase(), color = if (isSelected) Color.White else Color.LightGray, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                                Text(feeText, color = if (isSelected) Color.White else Color.Gray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                                Text(descText, color = if (isSelected) NeonCyan else Color.DarkGray, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                            }
                                        }
                                    }
                                }
                            }

                            // Detailed balance calculations
                            val selectedFeeVal = when(selectedFeePriority) {
                                "Eco" -> 0.50
                                "Standard" -> 1.25
                                else -> 3.50
                            }
                            val finalReceive = if (modalWithdrawAmount > selectedFeeVal) modalWithdrawAmount - selectedFeeVal else 0.0

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Black.copy(alpha = 0.4f))
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Total Payout Requested:", color = Color.Gray, fontSize = 11.sp)
                                    Text("${modalWithdrawAmount.toInt()} MGT", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Network Speed Fee:", color = Color.Gray, fontSize = 11.sp)
                                    Text("-$selectedFeeVal MGT", color = Color.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                                HorizontalDivider(color = CardBorderDark, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Liquid Net Settlement:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                    Text(String.format("%.2f MGT", finalReceive), color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
                                }
                            }

                            // Danger warning notice!
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.Red.copy(alpha = 0.1f))
                                    .border(0.5.dp, Color.Red.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "ATTENTION: Blockchain outbound transfers are final. Verify recipient address before signing block transit commands.",
                                    color = Color.LightGray,
                                    fontSize = 9.sp,
                                    lineHeight = 12.sp
                                )
                            }

                            // Action buttons row!
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { showWithdrawModal = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = CardBorderDark),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).testTag("withdraw_modal_cancel")
                                ) {
                                    Text("ABORT", color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                                Button(
                                    onClick = { isWithdrawalSimulating = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1.5f).testTag("withdraw_modal_confirm")
                                ) {
                                    Text("SIGN CONTRACT", color = CarbonBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Coroutine effect for simulating the payout sequence!
        LaunchedEffect(isWithdrawalSimulating) {
            if (isWithdrawalSimulating) {
                withdrawalSimulationProgress = 0f
                withdrawalSimulationPhase = "Phase 1/4: Launching blockchain handshake protocol..."
                delay(800)
                withdrawalSimulationProgress = 0.28f
                withdrawalSimulationPhase = "Phase 2/4: Assembling decentralized GAS signature key..."
                delay(800)
                withdrawalSimulationProgress = 0.58f
                withdrawalSimulationPhase = "Phase 3/4: Broadcaster dispatching payload to miner pools..."
                delay(1000)
                withdrawalSimulationProgress = 0.88f
                withdrawalSimulationPhase = "Phase 4/4: Confirming ledger verification cycles..."
                delay(800)
                withdrawalSimulationProgress = 1.0f
                withdrawalSimulationPhase = "Transit Complete. Sealing immutable contract block..."
                delay(500)

                val addr = user.connectedWalletAddress ?: ""
                val finalNetwork = user.walletNetwork ?: networkSelected
                vm.requestWithdrawal(
                    amount = modalWithdrawAmount,
                    walletAddress = addr,
                    network = finalNetwork,
                    onSuccess = {
                        val chars = "0123456789abcdef"
                        confirmedTxHash = "0x" + (1..64).map { chars.random() }.joinToString("")
                        withdrawalSuccessState = true
                        isWithdrawalSimulating = false
                        withdrawalAmountInput = ""
                    }
                )
            }
        }
    }
}

fun CyanBorderOrGlow(enabled: Boolean): Color {
    return if (enabled) NeonCyan else Color.DarkGray
}

@Composable
fun HashRateVisualizer(
    totalHashPower: Double,
    userRigsCount: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_phase")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phaseSpec"
    )

    GlowCard(
        glowColor = if (totalHashPower > 0) NeonCyan else Color.Gray,
        borderWidth = 0.8,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (totalHashPower > 0) Color.Green else Color.Gray)
                    )
                    Text(
                        text = "REAL-TIME HASH CRYPTOSTREAM PULSE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (totalHashPower > 0) NeonCyan else Color.Gray,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = if (totalHashPower > 0) "STREAMING" else "DORMANT",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (totalHashPower > 0) NeonCyan else Color.Gray,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (totalHashPower > 0) NeonCyan.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .border(0.5.dp, CardBorderDark, RoundedCornerShape(8.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val centerY = height / 2f

                    // Fine tech coordinate grid lines
                    val gridSpacing = 16.dp.toPx()
                    val columns = (width / gridSpacing).toInt()
                    val rows = (height / gridSpacing).toInt()
                    for (i in 0..columns) {
                        val x = i * gridSpacing
                        drawLine(
                            color = Color.White.copy(alpha = 0.04f),
                            start = androidx.compose.ui.geometry.Offset(x, 0f),
                            end = androidx.compose.ui.geometry.Offset(x, height),
                            strokeWidth = 1f
                        )
                    }
                    for (i in 0..rows) {
                        val y = i * gridSpacing
                        drawLine(
                            color = Color.White.copy(alpha = 0.04f),
                            start = androidx.compose.ui.geometry.Offset(0f, y),
                            end = androidx.compose.ui.geometry.Offset(width, y),
                            strokeWidth = 1f
                        )
                    }

                    val hasPower = totalHashPower > 0
                    val wavePath = androidx.compose.ui.graphics.Path()
                    val secondaryPath = androidx.compose.ui.graphics.Path()

                    val amplitude = if (hasPower) {
                        (10.dp.toPx() + (totalHashPower / 12.0).coerceAtMost(30.0).dp.toPx())
                    } else {
                        1.5.dp.toPx()
                    }
                    val frequency = if (hasPower) 0.015f else 0.005f

                    for (x in 0..width.toInt() step 2) {
                        val xFloat = x.toFloat()
                        val sineValue1 = kotlin.math.sin(xFloat * frequency - phase)
                        val sineValue2 = kotlin.math.cos(xFloat * (frequency * 0.8f) + phase)
                        
                        // Micro noise peaks
                        val noise = if (hasPower) {
                            (kotlin.math.sin(xFloat * 0.1f) * 1.5f)
                        } else {
                            (kotlin.math.sin(xFloat * 0.5f) * 0.2f)
                        }

                        val y1 = centerY + (sineValue1 * amplitude) + noise
                        val y2 = centerY + (sineValue2 * amplitude * 0.5f) - noise

                        if (x == 0) {
                            wavePath.moveTo(xFloat, y1)
                            secondaryPath.moveTo(xFloat, y2)
                        } else {
                            wavePath.lineTo(xFloat, y1)
                            secondaryPath.lineTo(xFloat, y2)
                        }
                    }

                    // Stroke styles
                    drawPath(
                        path = wavePath,
                        color = NeonCyan,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                    drawPath(
                        path = secondaryPath,
                        color = CyberPurple.copy(alpha = 0.4f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                }

                // Mini overlay stats text for cyber feel
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "JITTER: 0.8ms | PACKET LOSS: 0.00%",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp,
                        color = Color.Gray
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "LATENCY: 14ms",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp,
                        color = if (totalHashPower > 0) Color.Green else Color.Gray
                    )
                }
            }

            // Descriptive metrics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ACTIVE COMPUTE ENGINES", fontSize = 8.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
                    Text("$userRigsCount RIGS ONLINE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("PULSE VELOCITY", fontSize = 8.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
                    Text(
                        text = if (totalHashPower > 0) String.format("%.1f Hz/tick", 60.0 + (totalHashPower * 0.05).coerceAtMost(40.0)) else "0.0 Hz/tick",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}


// --- SCREEN 5: SETTINGS & PREFERENCES DECK ---
@Composable
fun SettingsScreen(
    user: UserAccount,
    vm: MineForgeViewModel,
    onLogout: () -> Unit
) {
    val lang = user.preferredLanguage

    var editDisplayName by remember { mutableStateOf(user.displayName) }
    var biometricState by remember { mutableStateOf(user.hasBiometric) }
    var currencySelected by remember { mutableStateOf(user.preferredCurrency) }
    var languageSelected by remember { mutableStateOf(user.preferredLanguage) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonBlack)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Upper edit profile panel
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = I18n.get("profile", lang).uppercase(),
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(12.dp))
            HorizontalDivider(modifier = Modifier.weight(1f), color = CardBorderDark)
        }

        GlowCard(borderWidth = 0.5, glowColor = CardBorderDark) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = editDisplayName,
                    onValueChange = { editDisplayName = it },
                    label = { Text(I18n.get("username", lang), color = Color.Gray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CardBorderDark,
                        focusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("settings_username_input")
                )

                // Biometrics toggle row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = I18n.get("biometric", lang), color = Color.LightGray, fontSize = 13.sp)
                    Switch(
                        checked = biometricState,
                        onCheckedChange = { biometricState = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CarbonBlack,
                            checkedTrackColor = NeonCyan,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = SurfaceDark
                        ),
                        modifier = Modifier.testTag("biometric_switch")
                    )
                }

                // Currency selector
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = I18n.get("def_currency", lang), color = Color.Gray, fontSize = 12.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CardBorderDark)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("USD", "EUR", "RUB").forEach { cur ->
                            val act = (currencySelected == cur)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (act) SurfaceDark else Color.Transparent)
                                    .clickable { currencySelected = cur }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cur,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (act) NeonCyan else Color.Gray,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                // Language Selector
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = I18n.get("def_lang", lang), color = Color.Gray, fontSize = 12.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CardBorderDark)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("EN" to "English", "RU" to "Русский", "ES" to "Español").forEach { (code, name) ->
                            val act = (languageSelected == code)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (act) SurfaceDark else Color.Transparent)
                                    .clickable { languageSelected = code }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (act) NeonCyan else Color.Gray,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                // Save triggers
                Button(
                    onClick = {
                        vm.updateProfile(editDisplayName, currencySelected, languageSelected, biometricState)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(44.dp).testTag("save_settings_button")
                ) {
                    Text(I18n.get("save", lang).uppercase(), color = CarbonBlack, fontWeight = FontWeight.Bold)
                }
            }
        }

        // About Block
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ABOUT APPARATUS",
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(12.dp))
            HorizontalDivider(modifier = Modifier.weight(1f), color = CardBorderDark)
        }

        GlowCard(borderWidth = 0.5, glowColor = CardBorderDark) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("App Version", color = Color.Gray, fontSize = 13.sp)
                    Text("MineForge v3.5.2-Flash", color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Validation Framework", color = Color.Gray, fontSize = 13.sp)
                    Text("Google Play Sandbox", color = Color.White, fontSize = 13.sp)
                }

                HorizontalDivider(color = CardBorderDark, modifier = Modifier.padding(vertical = 4.dp))

                Text(
                    text = I18n.get("terms", lang),
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vm.triggerToast("Showing encrypted license agreement...") }
                )
            }
        }

        // Logout and Delete Action rows
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { vm.logout(onLogout) },
                colors = ButtonDefaults.buttonColors(containerColor = DeepCyanBg),
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("logout_button")
            ) {
                Icon(Icons.Default.PowerSettingsNew, contentDescription = null, tint = Color.LightGray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(I18n.get("logout", lang).uppercase(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showDeleteConfirmDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                border = BorderStroke(1.dp, Color.Red),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("delete_account_button")
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text(I18n.get("delete_acc", lang).uppercase(), color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    // Deletion prompt Modal dialog
    if (showDeleteConfirmDialog) {
        Dialog(onDismissRequest = { showDeleteConfirmDialog = false }) {
            GlowCard(
                glowColor = Color.Red,
                borderWidth = 1.5,
                modifier = Modifier.widthIn(max = 380.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "DELETION AUTHORIZATION CODES",
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Warning: This triggers an irreversible wipe sweep of all mining power rigs, transaction ledgers, and credentials. Are you sure?",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showDeleteConfirmDialog = false }) {
                            Text("ABORT", color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                vm.deleteAccount(onLogout)
                                showDeleteConfirmDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("WIPE METRICS", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


// --- EXPANSION MODAL: BUY RIG TIERS STORE ---
@Composable
fun RigStoreDialog(
    user: UserAccount,
    vm: MineForgeViewModel,
    onDismiss: () -> Unit
) {
    // Rigid Tier Definition Cards
    val tiers = listOf(
        RigTier("STARTER PACKAGE", "10 MH/s", 9.99, 5.0, Icons.Default.Hardware, "Basic operational node"),
        RigTier("ADVANCED BLOCKER", "50 MH/s", 49.99, 30.0, Icons.Default.DeveloperBoard, "Accelerated processor card"),
        RigTier("PRO HASHING ENGINE", "200 MH/s", 199.99, 140.0, Icons.Default.Memory, "Deep-rack carbon mining shelf", isPopular = true),
        RigTier("ELITE TITAN RIG", "1 GH/s", 999.99, 800.0, Icons.Default.Computer, "Datacenter server cluster array")
    )

    Dialog(onDismissRequest = onDismiss) {
        GlowCard(
            glowColor = NeonCyan,
            borderWidth = 1.5,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BUY POWER TIERS Store",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray)
                    }
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(tiers) { t ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (t.isPopular) DeepCyanBg else CardBorderDark.copy(alpha = 0.4f))
                                .border(
                                    width = 1.2.dp,
                                    color = if (t.isPopular) NeonCyan else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = t.icon,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(28.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = t.title,
                                        fontWeight = FontWeight.Bold,
                                        color = if (t.isPopular) NeonCyan else Color.White,
                                        fontSize = 13.sp
                                    )
                                    if (t.isPopular) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(CyberPurple)
                                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                        ) {
                                            Text("BEST VALUE", fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Text(text = t.desc, color = Color.Gray, fontSize = 11.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text("Speed: ${t.speed}", color = NeonCyan, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                    Text("Yield: ${t.dailyReward.toInt()} MGT/day", color = Color.LightGray, fontSize = 12.sp)
                                }
                            }

                            // Buy button displaying simulated price
                            Button(
                                onClick = {
                                    // Parse speeds
                                    val mhsValue = if (t.speed.contains("GH/s")) 1000.0 else t.speed.replace(" MH/s", "").toDouble()
                                    vm.purchaseRigTier(
                                        tierName = t.title,
                                        priceUsd = t.price,
                                        hashRateMhs = mhsValue,
                                        dailyEarnings = t.dailyReward
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(32.dp)
                                    .testTag("buy_tier_btn_${t.title.lowercase().replace(" ", "_")}")
                            ) {
                                Text("$${t.price}", color = CarbonBlack, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class RigTier(
    val title: String,
    val speed: String,
    val price: Double,
    val dailyReward: Double,
    val icon: ImageVector,
    val desc: String,
    val isPopular: Boolean = false
)
