package com.example.ui.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingPage(
    selectedRole: String, // "shopkeeper" or "family"
    onRoleSelected: (String) -> Unit,
    onNavigateToMain: (String) -> Unit, // "shopkeeper_hub" or "family_wallet"
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Setup interactive state to showcase mock setup actions
    var shopkeeperName by remember { mutableStateOf("Ramesh Kirana Store") }
    var shopkeeperSetupAadhaar by remember { mutableStateOf("") }
    var shopkeeperSetupVerified by remember { mutableStateOf(false) }
    var shopkeeperStep1Done by remember { mutableStateOf(true) }
    var shopkeeperStep2Done by remember { mutableStateOf(false) }
    var shopkeeperStep3Done by remember { mutableStateOf(false) }

    var selectedFamilyId by remember { mutableStateOf("verma_12") }
    var enteredPasscode by remember { mutableStateOf("") }
    var passcodeError by remember { mutableStateOf(false) }
    var passcodeVerified by remember { mutableStateOf(false) }
    var familyStep1Done by remember { mutableStateOf(false) }
    var familyStep2Done by remember { mutableStateOf(false) }
    var familyStep3Done by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // BRAND DESIGN HERO BANNER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(TrustNavy, TrustNavy.copy(alpha = 0.95f))
                        )
                    )
                    .padding(top = 48.dp, bottom = 32.dp, start = 20.dp, end = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Logo Accent
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(ActionGreen, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AllInclusive,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "LocalSync",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = (-0.5).sp
                        )
                    }

                    Text(
                        text = "Unified Neighborhood Trust Protocol",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ActionGreen,
                        letterSpacing = 1.5.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Digitalize stock, establish family credit, and issue flash deals in under 60 seconds.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.82f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    // Indian Stack Trust Tag
                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Saffron, White, Green dots mimicking flag colors for authentic trust localization
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Box(modifier = Modifier.size(6.dp).background(Color(0xFFFF9933), CircleShape))
                            Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                            Box(modifier = Modifier.size(6.dp).background(Color(0xFF138808), CircleShape))
                        }
                        Text(
                            text = "India Stack Compliant • ONDC Friendly",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // DUAL-ROLE INTERACTIVE SELECTOR CARD
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Select Your Persona",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Shopkeeper Selector
                    Card(
                        onClick = { onRoleSelected("shopkeeper") },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedRole == "shopkeeper") Color.White else Slate100,
                        ),
                        border = BorderStroke(
                            width = if (selectedRole == "shopkeeper") 2.dp else 1.dp,
                            color = if (selectedRole == "shopkeeper") ActionGreen else Slate200
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("role_shopkeeper_card"),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        if (selectedRole == "shopkeeper") ActionGreen.copy(alpha = 0.15f) else Slate200,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    tint = if (selectedRole == "shopkeeper") ActionGreen else Slate600,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Shopkeeper",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TrustNavy,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Manage stock via voice, ledger & sales",
                                fontSize = 11.sp,
                                color = Slate600,
                                textAlign = TextAlign.Center,
                                lineHeight = 13.sp
                            )
                        }
                    }

                    // Local Family Selector
                    Card(
                        onClick = { onRoleSelected("family") },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedRole == "family") Color.White else Slate100,
                        ),
                        border = BorderStroke(
                            width = if (selectedRole == "family") 2.dp else 1.dp,
                            color = if (selectedRole == "family") ActionGreen else Slate200
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("role_family_card"),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        if (selectedRole == "family") AccentTeal.copy(alpha = 0.15f) else Slate200,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null,
                                    tint = if (selectedRole == "family") AccentTeal else Slate600,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Local Family",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TrustNavy,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Digitalize Khata card & scan nearby maps",
                                fontSize = 11.sp,
                                color = Slate600,
                                textAlign = TextAlign.Center,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }

            // ROLE-SPECIFIC DETAILED ONBOARDING CONTENT
            AnimatedContent(
                targetState = selectedRole,
                transitionSpec = {
                    fadeIn() with fadeOut()
                }
            ) { role ->
                if (role == "shopkeeper") {
                    // SHOPKEEPER ONBOARDING SECTION
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Core Benefit Cards
                        Text(
                            text = "Why Shopkeepers Choose LocalSync",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )

                        // 3 horizontal/compact benefits
                        BenefitRow(
                            icon = Icons.Default.Mic,
                            iconColor = ActionGreen,
                            title = "Voice-Powered Stock Inventory",
                            description = "No typing needed. Just say '20 pockets bread jodo' to update instantly in Hinglish or English."
                        )

                        BenefitRow(
                            icon = Icons.Default.ReceiptLong,
                            iconColor = AccentTeal,
                            title = "Instant Verified Credit Ledgers",
                            description = "Erase trust issues. Local families authorize transactions with secure passcodes, keeping records clear."
                        )

                        BenefitRow(
                            icon = Icons.Default.OfflineBolt,
                            iconColor = Color(0xFFF59E0B),
                            title = "3-Second Flash Promotions",
                            description = "Instantly broadcast timed discounts directly to nearby consumer maps, clearing perishable inventory fast."
                        )

                        // Step-By-Step Setup Wizard Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Slate200),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Initial Shop Setup Status",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TrustNavy
                                )
                                Text(
                                    text = "Complete these 3 quick actions to launch your interactive dashboard.",
                                    fontSize = 11.sp,
                                    color = Slate600,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                // Step 1: Define Shop Name
                                InteractiveStepItem(
                                    stepNum = 1,
                                    text = "Define Store Identity",
                                    isCompleted = shopkeeperStep1Done,
                                    onToggle = { shopkeeperStep1Done = !shopkeeperStep1Done }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 12.dp)
                                    ) {
                                        Text(
                                            text = "Store Trade Name",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate600
                                        )
                                        OutlinedTextField(
                                            value = shopkeeperName,
                                            onValueChange = { shopkeeperName = it },
                                            placeholder = { Text("e.g. Ramesh Kirana") },
                                            textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = ActionGreen,
                                                unfocusedBorderColor = Slate200
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp),
                                            singleLine = true
                                        )
                                    }
                                }

                                Divider(color = Slate100, modifier = Modifier.padding(vertical = 12.dp))

                                // Step 2: KYC / Verified Aadhaar Linkage (Simulated)
                                InteractiveStepItem(
                                    stepNum = 2,
                                    text = "Link India Stack Verification (KYC)",
                                    isCompleted = shopkeeperStep2Done || shopkeeperSetupVerified,
                                    onToggle = { shopkeeperStep2Done = !shopkeeperStep2Done }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 12.dp)
                                    ) {
                                        Text(
                                            text = "Enter 12-Digit Aadhaar or GSTIN identifier",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate600
                                        )
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            OutlinedTextField(
                                                value = shopkeeperSetupAadhaar,
                                                onValueChange = {
                                                    if (it.length <= 12) shopkeeperSetupAadhaar = it
                                                },
                                                placeholder = { Text("6541 3320 1198") },
                                                shape = RoundedCornerShape(8.dp),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = ActionGreen,
                                                    unfocusedBorderColor = Slate200
                                                ),
                                                modifier = Modifier.weight(1f),
                                                singleLine = true
                                            )
                                            Button(
                                                onClick = {
                                                    if (shopkeeperSetupAadhaar.length == 12) {
                                                        shopkeeperSetupVerified = true
                                                        shopkeeperStep2Done = true
                                                        focusManager.clearFocus()
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (shopkeeperSetupVerified) ActionGreen else TrustNavy
                                                ),
                                                shape = RoundedCornerShape(8.dp),
                                                enabled = shopkeeperSetupAadhaar.length == 12 && !shopkeeperSetupVerified,
                                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                            ) {
                                                if (shopkeeperSetupVerified) {
                                                    Icon(Icons.Default.Check, "Done", tint = Color.White, modifier = Modifier.size(16.dp))
                                                } else {
                                                    Text("Verify", fontSize = 11.sp, color = Color.White)
                                                }
                                            }
                                        }
                                        if (shopkeeperSetupVerified) {
                                            Text(
                                                text = "✓ Merchant ID Verified. Unified Digital Consent established.",
                                                fontSize = 10.sp,
                                                color = ActionGreen,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }

                                Divider(color = Slate100, modifier = Modifier.padding(vertical = 12.dp))

                                // Step 3: Invitation of circles
                                InteractiveStepItem(
                                    stepNum = 3,
                                    text = "Send First Family Credit Invitation",
                                    isCompleted = shopkeeperStep3Done,
                                    onToggle = { shopkeeperStep3Done = !shopkeeperStep3Done }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 4.dp)
                                    ) {
                                        Text(
                                            text = "Inviting Verma Family and Sharma Family by default. Tap to simulate dispatch of digital ledger.",
                                            fontSize = 11.sp,
                                            color = Slate600
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { shopkeeperStep3Done = true },
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentTeal),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.align(Alignment.Start)
                                        ) {
                                            Text("Send Invite SMS", fontSize = 11.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }

                        // DIRECT CTA
                        Button(
                            onClick = { onNavigateToMain("shopkeeper_hub") },
                            colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("launch_shopkeeper_hub"),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "Launch Shopkeeper Hub →",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                } else {
                    // LOCAL FAMILY ONBOARDING SECTION
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Core Benefit Cards
                        Text(
                            text = "Family Benefits on LocalSync",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )

                        BenefitRow(
                            icon = Icons.Default.AccountBalanceWallet,
                            iconColor = AccentTeal,
                            title = "Coordinated Family Khata Digital Ledger",
                            description = "Allow parents, grandparents, and children to track purchases on a single trusted neighborhood account."
                        )

                        BenefitRow(
                            icon = Icons.Default.CheckCircleOutline,
                            iconColor = ActionGreen,
                            title = "Zero Dispute Trust Code (Secure PIN)",
                            description = "Authorize ledger purchases at Ramesh Kirana with standard security passcodes, eliminating disputes."
                        )

                        BenefitRow(
                            icon = Icons.Default.CardGiftcard,
                            iconColor = Color(0xFF8B5CF6),
                            title = "Family Loyalty Cashbacks",
                            description = "Fulfill cycles on time and unlock direct ₹150 or ₹400 loyalty coupon certificates automatic credit!"
                        )

                        // Setup Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Slate200),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Secure Family Wallet Onboarding",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TrustNavy
                                )
                                Text(
                                    text = "Claim your credit line by choosing your profile and setting authorization keys.",
                                    fontSize = 11.sp,
                                    color = Slate600,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                // Step 1: Select Profile
                                InteractiveStepItem(
                                    stepNum = 1,
                                    text = "Choose Family Profile",
                                    isCompleted = familyStep1Done,
                                    onToggle = { familyStep1Done = !familyStep1Done }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 12.dp)
                                    ) {
                                        Text(
                                            text = "Select Family Name",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate600
                                        )
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 6.dp)
                                                .horizontalScroll(rememberScrollState()),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            listOf(
                                                "verma_12" to "Verma Family",
                                                "sharma_98" to "Sharma Family",
                                                "gupta_45" to "Gupta Family"
                                            ).forEach { (id, name) ->
                                                val isSel = selectedFamilyId == id
                                                FilterChip(
                                                    selected = isSel,
                                                    onClick = {
                                                        selectedFamilyId = id
                                                        familyStep1Done = true
                                                    },
                                                    label = { Text(name, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                                    colors = FilterChipDefaults.filterChipColors(
                                                        selectedContainerColor = AccentTeal.copy(alpha = 0.15f),
                                                        selectedLabelColor = AccentTeal,
                                                        selectedLeadingIconColor = AccentTeal
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                                Divider(color = Slate100, modifier = Modifier.padding(vertical = 12.dp))

                                // Step 2: Secret PIN Setup
                                InteractiveStepItem(
                                    stepNum = 2,
                                    text = "Authenticate Trust PIN Passcode",
                                    isCompleted = familyStep2Done || passcodeVerified,
                                    onToggle = { familyStep2Done = !familyStep2Done }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 12.dp)
                                    ) {
                                        Text(
                                            text = "Enter Family Passcode PIN to authenticate",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate600
                                        )
                                        Text(
                                            text = "Match passcode: Verma = 123456 | Sharma = 789012 | Gupta = 543210",
                                            fontSize = 9.sp,
                                            color = Slate400,
                                            modifier = Modifier.padding(bottom = 6.dp)
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            OutlinedTextField(
                                                value = enteredPasscode,
                                                onValueChange = {
                                                    enteredPasscode = it
                                                    passcodeError = false
                                                },
                                                placeholder = { Text("******") },
                                                shape = RoundedCornerShape(8.dp),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = AccentTeal,
                                                    unfocusedBorderColor = Slate200
                                                ),
                                                modifier = Modifier.weight(1f),
                                                singleLine = true
                                            )
                                            Button(
                                                onClick = {
                                                    val expected = when(selectedFamilyId) {
                                                        "verma_12" -> "123456"
                                                        "sharma_98" -> "789012"
                                                        else -> "543210"
                                                    }
                                                    if (enteredPasscode == expected) {
                                                        passcodeVerified = true
                                                        familyStep2Done = true
                                                        passcodeError = false
                                                        focusManager.clearFocus()
                                                    } else {
                                                        passcodeError = true
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (passcodeVerified) ActionGreen else TrustNavy
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                if (passcodeVerified) {
                                                    Icon(Icons.Default.Check, "Done", tint = Color.White, modifier = Modifier.size(16.dp))
                                                } else {
                                                    Text("Auth", fontSize = 11.sp, color = Color.White)
                                                }
                                            }
                                        }
                                        if (passcodeError) {
                                            Text(
                                                text = "✗ Incorrect PIN. Check hints.",
                                                color = CrimsonRed,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                        if (passcodeVerified) {
                                            Text(
                                                text = "✓ SECURE LEDGER CONNECTED. Family circle fully authenticated.",
                                                color = ActionGreen,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }

                                Divider(color = Slate100, modifier = Modifier.padding(vertical = 12.dp))

                                // Step 3: Setup Notifications
                                InteractiveStepItem(
                                    stepNum = 3,
                                    text = "Enable Real-Time Balance HUD Alerts",
                                    isCompleted = familyStep3Done,
                                    onToggle = { familyStep3Done = !familyStep3Done }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 6.dp)
                                    ) {
                                        Text(
                                            text = "We will send interactive ledger alerts to registered member mobile phones, ensuring 100% transparency.",
                                            fontSize = 11.sp,
                                            color = Slate600
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { familyStep3Done = true },
                                            colors = ButtonDefaults.buttonColors(containerColor = TrustNavy),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Opt In via SMS Protocol", fontSize = 11.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }

                        // DIRECT CTA
                        Button(
                            onClick = { onNavigateToMain("family_wallet") },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentTeal),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("launch_family_wallet"),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "Launch Family Wallet →",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BenefitRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Slate100, RoundedCornerShape(12.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(iconColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TrustNavy
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = Slate600,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun InteractiveStepItem(
    stepNum: Int,
    text: String,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (isCompleted) ActionGreen else TrustNavy.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                } else {
                    Text(
                        text = stepNum.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TrustNavy
                    )
                }
            }
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted) Slate600 else TrustNavy,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isCompleted) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "Toggle Section",
                tint = Slate400,
                modifier = Modifier.size(18.dp)
            )
        }

        // Expanded/Interactive options if NOT completed
        AnimatedVisibility(
            visible = !isCompleted,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 34.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                content()
            }
        }
    }
}
