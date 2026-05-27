package com.example.ui.family

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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import com.example.ui.demo.StoreDiscoveryTab
import com.example.viewmodel.LocalSyncViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FamilyWalletDashboard(
    viewModel: LocalSyncViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // ViewModel dynamic states
    val families by viewModel.families.collectAsState()
    val paymentSelectedFamily by viewModel.paymentSelectedFamily.collectAsState()
    val paymentState by viewModel.paymentState.collectAsState()
    val paymentProvider by viewModel.paymentProvider.collectAsState()

    var showRepaySlider by remember { mutableStateOf(false) }
    var repayAmount by remember { mutableStateOf("") }
    var manageCircleOpen by remember { mutableStateOf(false) }

    // Mock permissions for Verma / Sharma / Gupta members
    var memberPermissions by remember {
        mutableStateOf(
            mapOf(
                "Anil Verma (Father)" to true,
                "Rita Verma (Mother)" to true,
                "Siddharth Verma (Son)" to false,
                "Rajesh Sharma (Father)" to true,
                "Priya Sharma (Daughter)" to true,
                "Sanjay Gupta (Grandfather)" to true,
                "Vikram Gupta (Son)" to true,
                "Neha Gupta (Daughter-in-law)" to true
            )
        )
    }

    val activeFamily = paymentSelectedFamily ?: families.first()

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
            // FINTECH WALLET BRAND HEADER
            Surface(
                color = AccentTeal,
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
                                text = "FAMILY NEIGHBORHOOD WALLET",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ActionGreen,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            
                            // Select Active Family profile dropdown
                            var familyDropdownOpen by remember { mutableStateOf(false) }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { familyDropdownOpen = true }
                            ) {
                                Text(
                                    text = activeFamily.familyName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Icon(Icons.Default.ArrowDropDown, "select", tint = Color.White)
                            }

                            DropdownMenu(
                                expanded = familyDropdownOpen,
                                onDismissRequest = { familyDropdownOpen = false }
                            ) {
                                families.forEach { fam ->
                                    DropdownMenuItem(
                                        text = { Text(fam.familyName, fontSize = 13.sp) },
                                        onClick = {
                                            viewModel.triggerCheckoutPay(fam.id)
                                            familyDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }

                        // Code Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Lock, "pass", tint = Color.White, modifier = Modifier.size(10.dp))
                            Text(
                                text = "PIN: ${activeFamily.passCode}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // DYNAMIC DIGITAL LEDGER & CREDIT GAUGE
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Khata Credit Breakdown",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                // The master limit card
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
                            Column {
                                Text(text = "CURRENT REPAYMENT DUE", fontSize = 10.sp, color = Slate400, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "₹${(activeFamily.balance * -1).toInt()}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeFamily.balance < 0) CrimsonRed else ActionGreen
                                )
                            }

                            // Pay dues button
                            Button(
                                onClick = { showRepaySlider = !showRepaySlider },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentTeal),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Payment, "pay", modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Settle Due", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress Gauge for credit used
                        val balanceOwed = activeFamily.balance * -1
                        val availableCredit = activeFamily.creditLimit - balanceOwed
                        val creditFraction = if (activeFamily.creditLimit > 0) balanceOwed / activeFamily.creditLimit else 0.0

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Available Limit: ₹${availableCredit.toInt()}", fontSize = 11.sp, color = Slate600)
                            Text(text = "Total Limit: ₹${activeFamily.creditLimit.toInt()}", fontSize = 11.sp, color = Slate400)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = CircleShape,
                            color = Slate100,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (creditFraction > 0) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(creditFraction.toFloat())
                                            .background(CrimsonRed, CircleShape)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight((1.0 - creditFraction).toFloat())
                                )
                            }
                        }

                        if (activeFamily.rewardUnlocked != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ActionGreen.copy(0.08f), RoundedCornerShape(8.dp))
                                    .border(1.dp, ActionGreen.copy(0.2f), RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.CardGiftcard, "Gift", tint = ActionGreen, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Earned Loyal: ${activeFamily.rewardUnlocked}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ActionGreen
                                )
                            }
                        }
                    }
                }

                // COLLAPSIBLE REPAYMENT SECTION (SECURE UPI SIMULATOR)
                AnimatedVisibility(
                    visible = showRepaySlider,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Secure India-Stack UPI Payment",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TrustNavy
                            )
                            Text(
                                text = "Choose simulated app & input settlement amount.",
                                fontSize = 11.sp,
                                color = Slate600,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // Select UPI app
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("GPay", "PhonePe", "Paytm").forEach { upi ->
                                    val isSel = paymentProvider == upi
                                    FilterChip(
                                        selected = isSel,
                                        onClick = { viewModel.updatePaymentProvider(upi) },
                                        label = { Text(upi, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = AccentTeal.copy(0.12f),
                                            selectedLabelColor = AccentTeal
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = repayAmount,
                                    onValueChange = { repayAmount = it },
                                    placeholder = { Text("e.g. 500") },
                                    label = { Text("Amount to pay (₹)") },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AccentTeal,
                                        unfocusedBorderColor = Slate200,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )

                                Button(
                                    onClick = {
                                        val amt = repayAmount.toDoubleOrNull()
                                        if (amt != null && amt > 0) {
                                            viewModel.runUPIPaymentFlow(amt)
                                            repayAmount = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    enabled = paymentState != "in_progress"
                                ) {
                                    Text("Pay", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Payment Progress Overlay
                            AnimatedVisibility(visible = paymentState == "in_progress") {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = AccentTeal)
                                    Text("Verifying NPCI Trust Token...", fontSize = 11.sp, color = Slate600)
                                }
                            }

                            AnimatedVisibility(visible = paymentState == "completed") {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, "success", tint = ActionGreen, modifier = Modifier.size(16.dp))
                                    Text("Repayment Successful! Account ledger updated.", fontSize = 11.sp, color = ActionGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // SEGMENT: MANAGE "FAMILY KHATA" CIRCLE MEMBERS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { manageCircleOpen = !manageCircleOpen },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Manage Family Circle",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    IconButton(onClick = { manageCircleOpen = !manageCircleOpen }) {
                        Icon(
                            imageVector = if (manageCircleOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand",
                            tint = Slate600
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
                        Text(
                            text = "Household Member Permissions",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TrustNavy
                        )
                        Text(
                            text = "Control which registered family members are authorized to add grocery bills onto Ramesh Kirana ledger outstanding.",
                            fontSize = 11.sp,
                            color = Slate600,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Member loop filtered by active family matching
                        activeFamily.registeredMembers.forEach { member ->
                            val isPermitted = memberPermissions[member] ?: true
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Slate50, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Person, "member", tint = Slate400, modifier = Modifier.size(16.dp))
                                    Text(text = member, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TrustNavy)
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isPermitted) "Authorized" else "Revoked",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPermitted) ActionGreen else CrimsonRed
                                    )
                                    Switch(
                                        checked = isPermitted,
                                        onCheckedChange = { newVal ->
                                            val updated = memberPermissions.toMutableMap()
                                            updated[member] = newVal
                                            memberPermissions = updated
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = ActionGreen,
                                            uncheckedThumbColor = Slate400,
                                            uncheckedTrackColor = Slate200
                                        ),
                                        modifier = Modifier.scale(0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SEGMENT: TRANSACTION LEDGER HISTORY
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Transaction History with Ramesh Kirana",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                // Render mock items representing standard groceries
                val ledgerLogs = when (activeFamily.id) {
                    "verma_12" -> listOf(
                        Triple("Wheat Flour Premium 5kg", -260.0, "Yesterday"),
                        Triple("Mustard Oil 1L Fortune", -175.0, "2 days ago"),
                        Triple("Parle-G Biscuit 10 packs", -100.0, "3 days ago"),
                        Triple("Credited via UPI PhonePe Settle", 1200.0, "5 days ago"),
                        Triple("Rice India Gate Staples", -615.0, "1 week ago")
                    )
                    "sharma_98" -> listOf(
                        Triple("Amul Gold Milk 2L", -132.0, "Today"),
                        Triple("Tata Salt 1kg + Maggi Pack", -118.0, "Yesterday"),
                        Triple("Credited via UPI GPay", 500.0, "Last week")
                    )
                    else -> listOf(
                        Triple("Fortune Cooking Oil case", -1050.0, "Last week"),
                        Triple("Bulk Atta bags 2x", -520.0, "2 weeks ago"),
                        Triple("Credited via Cash Settle", 4200.0, "3 weeks ago")
                    )
                }

                ledgerLogs.forEach { (item, amt, date) ->
                    val isCreditPay = amt > 0
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
                                    .background(if (isCreditPay) ActionGreen.copy(0.1f) else TrustNavy.copy(0.08f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isCreditPay) Icons.Default.TrendingUp else Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = if (isCreditPay) ActionGreen else TrustNavy,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Column {
                                Text(text = item, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TrustNavy)
                                Text(text = date, fontSize = 10.sp, color = Slate400)
                            }
                        }

                        Text(
                            text = if (isCreditPay) "+₹${amt.toInt()}" else "-₹${(amt * -1).toInt()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCreditPay) ActionGreen else Slate900
                        )
                    }
                }
            }

            // SEGMENT: LOCAL TRUSTED STORE LOCATOR (NEIGHBORHOOD MAP)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Proximity Shop Discoverer",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                Text(
                    text = "Locate nearby merchants, view active flash deals, and confirm distances directly.",
                    fontSize = 11.sp,
                    color = Slate600,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Slate200),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        StoreDiscoveryTab(viewModel = viewModel, isScrollable = false)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



