package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.landing.LandingPage
import com.example.ui.demo.LiveDemoSuite
import com.example.ui.onboarding.OnboardingPage
import com.example.ui.shopkeeper.ShopkeeperDashboard
import com.example.ui.family.FamilyWalletDashboard
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TrustNavy
import com.example.viewmodel.LocalSyncViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: LocalSyncViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Support edge-to-edge drawing, including transparent navigation lines
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                var currentMainTab by remember { mutableStateOf("onboarding") } // "onboarding", "shopkeeper_hub", "family_wallet", "landing"
                var onboardingRole by remember { mutableStateOf("shopkeeper") } // "shopkeeper" or "family"

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color.White,
                            tonalElevation = 4.dp
                        ) {
                            // 1. Onboarding / Welcome Tour Tab
                            NavigationBarItem(
                                selected = currentMainTab == "onboarding",
                                onClick = { currentMainTab = "onboarding" },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Explore,
                                        contentDescription = "Onboarding Flow"
                                    )
                                },
                                label = { Text("Welcome Guide", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TrustNavy,
                                    selectedTextColor = TrustNavy,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray
                                )
                            )

                            // 2. Shopkeeper Dashboard Hub Tab
                            NavigationBarItem(
                                selected = currentMainTab == "shopkeeper_hub",
                                onClick = { currentMainTab = "shopkeeper_hub" },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Storefront,
                                        contentDescription = "Shopkeeper Hub"
                                    )
                                },
                                label = { Text("Shopkeeper Hub", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TrustNavy,
                                    selectedTextColor = TrustNavy,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray
                                )
                            )

                            // 3. Family Wallet Credit Tab
                            NavigationBarItem(
                                selected = currentMainTab == "family_wallet",
                                onClick = { currentMainTab = "family_wallet" },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = "Family Wallet"
                                    )
                                },
                                label = { Text("Family Wallet", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TrustNavy,
                                    selectedTextColor = TrustNavy,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray
                                )
                            )

                            // 4. Web Information Hub Tab
                            NavigationBarItem(
                                selected = currentMainTab == "landing",
                                onClick = { currentMainTab = "landing" },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = "Marketing Website"
                                    )
                                },
                                label = { Text("Web Hub", fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TrustNavy,
                                    selectedTextColor = TrustNavy,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentMainTab) {
                            "onboarding" -> {
                                OnboardingPage(
                                    selectedRole = onboardingRole,
                                    onRoleSelected = { onboardingRole = it },
                                    onNavigateToMain = { target ->
                                        currentMainTab = target
                                    }
                                )
                            }
                            "shopkeeper_hub" -> {
                                ShopkeeperDashboard(
                                    viewModel = viewModel
                                )
                            }
                            "family_wallet" -> {
                                FamilyWalletDashboard(
                                    viewModel = viewModel
                                )
                            }
                            "landing" -> {
                                LandingPage(
                                    viewModel = viewModel,
                                    onNavigateToDemoTab = { targetSubTab ->
                                        when (targetSubTab) {
                                            "voice", "flash" -> {
                                                currentMainTab = "shopkeeper_hub"
                                            }
                                            "khata", "map" -> {
                                                currentMainTab = "family_wallet"
                                            }
                                            else -> {
                                                currentMainTab = "onboarding"
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
