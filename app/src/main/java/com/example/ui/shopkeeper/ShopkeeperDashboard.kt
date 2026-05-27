package com.example.ui.shopkeeper

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import com.example.ui.demo.VoiceStockingTab
import com.example.viewmodel.LocalSyncViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShopkeeperDashboard(
    viewModel: LocalSyncViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    // Connect state dynamically
    val inventory by viewModel.inventory.collectAsState()
    val families by viewModel.families.collectAsState()
    val countdownTimer by viewModel.countdownTimer.collectAsState()
    val selectedDiscountPercent by viewModel.discountPercentSelection.collectAsState()

    var showVoiceModule by remember { mutableStateOf(false) }
    var quickKhataAmount by remember { mutableStateOf("") }
    var selectedKhataFamilyId by remember { mutableStateOf("verma_12") }
    var khataSuccessMsg by remember { mutableStateOf("") }

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
            // MERCHANT BUSINESS BRAND HEADER
            Surface(
                color = TrustNavy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "COMMERCE HUB",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ActionGreen,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Ramesh Kirana Store",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Sync Status Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(ActionGreen, CircleShape)
                            )
                            Text(
                                text = "Live Sync Active",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // PRIMARY KPIs SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Key Performance Indicators",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                // Grid of 2x2 main metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // KPI: Today's Sales
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Slate200),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "TODAY'S SALES", fontSize = 9.sp, color = Slate400, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "₹14,580", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TrustNavy)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(Icons.Default.ArrowUpward, "up", tint = ActionGreen, modifier = Modifier.size(12.dp))
                                Text(text = "+12% vs yesterday", fontSize = 10.sp, color = ActionGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // KPI: Outstanding Khata Circle Credit
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Slate200),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "OUTSTANDING CREDIT", fontSize = 9.sp, color = Slate400, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            val totalOutstanding = families.sumOf { it.balance * -1 }
                            Text(text = "₹${totalOutstanding.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CrimsonRed)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Across ${families.size} circles", fontSize = 10.sp, color = Slate600)
                        }
                    }
                }

                // KPI: Weekly Sales target custom gauge
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Slate200),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "WEEKLY REVENUE TARGET", fontSize = 9.sp, color = Slate400, fontWeight = FontWeight.Bold)
                            Text(text = "85% Achieved", fontSize = 11.sp, color = AccentTeal, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "₹85,450 / ₹1,00,000", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TrustNavy)
                        Spacer(modifier = Modifier.height(8.dp))
                        // Progress bar indicator
                        Surface(
                            shape = CircleShape,
                            color = Slate100,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(0.85f)
                                        .background(AccentTeal, CircleShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(0.15f)
                                )
                            }
                        }
                    }
                }

                // ACTION ROW: Voice Stock trigger and Flash promo trigger
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showVoiceModule = !showVoiceModule },
                        colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.Mic, "Voice Input", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (showVoiceModule) "Hide Stocker" else "Quick Voice Stock", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        // Saffron indicator of ONDC compatibility
                        Button(
                            onClick = { /* Just decor / anchor */ },
                            colors = ButtonDefaults.buttonColors(containerColor = TrustNavy),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(Icons.Default.Verified, "Verified", tint = ActionGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "ONDC Sync: Active", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // DYNAMIC VOICE STOCK ACCORDION DISPLAY
            AnimatedVisibility(
                visible = showVoiceModule,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, ActionGreen.copy(0.4f)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎙️ Voice Stock Terminal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TrustNavy
                            )
                            IconButton(onClick = { showVoiceModule = false }) {
                                Icon(Icons.Default.Close, "close", tint = Slate400, modifier = Modifier.size(16.dp))
                            }
                        }
                        
                        Text(
                            text = "Speak in Hinglish or English or tap standard templates below to simulate updating catalog stocks on the fly.",
                            fontSize = 11.sp,
                            color = Slate600,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        VoiceStockingTab(viewModel = viewModel, isScrollable = false)
                    }
                }
            }

            // SEGMENT: LOW STOCK WARNINGS & AI MERCHANDISE COPILOT
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Low Stock Alerts",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                val lowStockItems = inventory.filter { it.quantity < 20 }
                if (lowStockItems.isEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Slate200),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, "ok", tint = ActionGreen)
                            Text(text = "All staples and groceries meet safety stock thresholds.", fontSize = 12.sp, color = Slate600)
                        }
                    }
                } else {
                    lowStockItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(CrimsonRed.copy(0.12f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Warning, "low stock", tint = CrimsonRed, modifier = Modifier.size(16.dp))
                                }
                                Column {
                                    Text(text = item.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TrustNavy)
                                    Text(
                                        text = "${item.category} • Critical stock reserve",
                                        fontSize = 10.sp,
                                        color = Slate600
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Only ${item.quantity} ${item.unit} left",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonRed
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Restock suggested",
                                    fontSize = 9.sp,
                                    color = Slate400,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // SEGMENT: ACTIVE FLASH SALES & DISCOUNT BROADCASTER
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Flash Sales",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )

                    // Countdown timer
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .background(CrimsonRed.copy(0.1f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Schedule, "Time Left", tint = CrimsonRed, modifier = Modifier.size(12.dp))
                        Text(
                            text = countdownTimer,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonRed
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Slate200),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Current Discount Level",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TrustNavy
                            )

                            Text(
                                text = "$selectedDiscountPercent% OFF Staples",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ActionGreen
                            )
                        }

                        Text(
                            text = "Set a temporary discount to clear expiring dairy products or extra rice bags. Families under 1km receive immediate broadcast push alerts.",
                            fontSize = 11.sp,
                            color = Slate600,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        // Selector chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(10, 15, 25, 30, 50).forEach { percent ->
                                val acts = selectedDiscountPercent == percent
                                FilterChip(
                                    selected = acts,
                                    onClick = { viewModel.launchFlashSale(percent) },
                                    label = { Text("$percent%", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ActionGreen.copy(0.12f),
                                        selectedLabelColor = ActionGreen
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                viewModel.launchFlashSale(selectedDiscountPercent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TrustNavy),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Campaign, "Campaign", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Re-broadcast Blast Deal Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // SEGMENT: FAMILY LOYALTY & CREDIT CIRCLES
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Family Loyalty & Credit Ledger",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                // Quick recording form
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Slate200),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Quick Record Multi-Family Purchase",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TrustNavy,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Select Family Spinner Mock
                            var familyExpanded by remember { mutableStateOf(false) }
                            val activeFamily = families.firstOrNull { it.id == selectedKhataFamilyId } ?: families.first()
                            
                            Box(modifier = Modifier.weight(1.3f)) {
                                Button(
                                    onClick = { familyExpanded = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Slate100, contentColor = TrustNavy),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = activeFamily.familyName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(16.dp))
                                }

                                DropdownMenu(
                                    expanded = familyExpanded,
                                    onDismissRequest = { familyExpanded = false }
                                ) {
                                    families.forEach { fam ->
                                        DropdownMenuItem(
                                            text = { Text(fam.familyName, fontSize = 12.sp) },
                                            onClick = {
                                                selectedKhataFamilyId = fam.id
                                                familyExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Enter billing amount
                            OutlinedTextField(
                                value = quickKhataAmount,
                                onValueChange = { quickKhataAmount = it },
                                placeholder = { Text("Amount ₹") },
                                label = { Text("Price ₹", fontSize = 10.sp) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ActionGreen,
                                    unfocusedBorderColor = Slate200
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )

                            // Apply
                            Button(
                                onClick = {
                                    val amt = quickKhataAmount.toDoubleOrNull()
                                    if (amt != null && amt > 0) {
                                        viewModel.addCreditPurchase(selectedKhataFamilyId, "Groceries purchase", amt)
                                        khataSuccessMsg = "✓ Recorded ₹${amt.toInt()} to ${activeFamily.familyName}"
                                        quickKhataAmount = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
                            ) {
                                Text("+ Ledger", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (khataSuccessMsg.isNotEmpty()) {
                            Text(
                                text = khataSuccessMsg,
                                color = ActionGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Table of current families, credit logs, points, passcodes
                        Text(
                            text = "Family Members & Reward Status",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        families.forEach { family ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Slate100, RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = family.familyName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TrustNavy
                                    )
                                    Text(
                                        text = "${family.registeredMembers.size} registered members",
                                        fontSize = 10.sp,
                                        color = Slate600
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Outstanding: ₹${(family.balance * -1).toInt()}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonRed
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Icon(Icons.Default.Star, "points", tint = Color(0xFFF59E0B), modifier = Modifier.size(10.dp))
                                        Text(
                                            text = "${family.loyaltyPoints} Loyalty Pts",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TrustNavy
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
