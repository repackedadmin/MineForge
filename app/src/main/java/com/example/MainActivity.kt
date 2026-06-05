package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val vm: MineForgeViewModel = viewModel()
                var currentTab by remember { mutableIntStateOf(0) }
                var showStoreDialog by remember { mutableStateOf(false) }

                val activeUser by vm.activeUser.collectAsStateWithLifecycle()
                val context = LocalContext.current

                // Listen to central Toast event broadcasts
                LaunchedEffect(Unit) {
                    vm.toastMessage.collect { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }

                if (activeUser == null) {
                    AuthScreen(vm = vm, onAuthSuccess = { currentTab = 0 })
                } else {
                    val user = activeUser!!
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CarbonBlack),
                        topBar = {
                            OptBar(
                                userName = user.displayName,
                                onOpenStore = { showStoreDialog = true }
                            )
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                currentTab = currentTab,
                                onTabSelect = { currentTab = it }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(CarbonBlack)
                        ) {
                            when (currentTab) {
                                0 -> DashboardScreen(
                                    user = user,
                                    vm = vm,
                                    onNavigateToTab = { currentTab = it }
                                )
                                1 -> MiningScreen(
                                    user = user,
                                    vm = vm,
                                    onOpenRigStore = { showStoreDialog = true }
                                )
                                2 -> BalanceScreen(
                                    user = user,
                                    vm = vm
                                )
                                3 -> SettingsScreen(
                                    user = user,
                                    vm = vm,
                                    onLogout = { currentTab = 0 }
                                )
                            }
                        }
                    }

                    // Floating/Overlay Rig Shop Dialog
                    if (showStoreDialog) {
                        RigStoreDialog(
                            user = user,
                            vm = vm,
                            onDismiss = { showStoreDialog = false }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptBar(
    userName: String,
    onOpenStore: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SurfaceDark,
            titleContentColor = Color.White
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MINEFORGE CLIENT",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.5.sp
                )
            }
        },
        actions = {
            // Quick purchase shortcut icon button
            IconButton(
                onClick = onOpenStore,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .background(DeepCyanBg, RoundedCornerShape(8.dp))
                    .size(36.dp)
                    .testTag("appbar_shop_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Buy",
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    )
}

@Composable
fun BottomNavigationBar(
    currentTab: Int,
    onTabSelect: (Int) -> Unit
) {
    // M3 Navigation Bar with proper insets and neon-cyan themed active indicator colors
    NavigationBar(
        containerColor = SurfaceDark,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars // Ensure no bottom layout clipping in gesture pill screen mode
    ) {
        val items = listOf(
            Triple(0, "Home", Icons.Default.Dashboard),
            Triple(1, "Mining", Icons.Default.Memory),
            Triple(2, "Vault", Icons.Default.AccountBalanceWallet),
            Triple(3, "Settings", Icons.Default.Settings)
        )

        items.forEach { (index, label, icon) ->
            val active = (currentTab == index)
            NavigationBarItem(
                selected = active,
                onClick = { onTabSelect(index) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (active) CarbonBlack else Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        color = if (active) NeonCyan else Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = NeonCyan,
                    selectedIconColor = CarbonBlack,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = NeonCyan,
                    unselectedTextColor = Color.Gray
                ),
                modifier = Modifier.testTag("nav_item_${label.lowercase()}")
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

