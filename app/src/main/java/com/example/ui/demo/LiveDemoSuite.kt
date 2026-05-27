package com.example.ui.demo

import kotlinx.coroutines.delay
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import com.example.viewmodel.LocalSyncViewModel
import kotlin.math.sin
import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@Composable
fun LiveDemoSuite(
    viewModel: LocalSyncViewModel,
    activeSubTab: String, // "voice", "khata", "flash", "map"
    modifier: Modifier = Modifier
) {
    Surface(
        color = Slate50,
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Live broadcast overlay notification banner
            val broadcastMsg by viewModel.broadcastNotification.collectAsState()
            AnimatedVisibility(
                visible = broadcastMsg != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                broadcastMsg?.let { text ->
                    Surface(
                        color = TrustNavy,
                        contentColor = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.clearBroadcast() }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = "Notification Broadcast",
                                    tint = ActionGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = text,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 16.sp
                                )
                            }
                            IconButton(onClick = { viewModel.clearBroadcast() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss Notification",
                                    tint = Color.White.copy(0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Outer title banner
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = Slate200,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "LocalSync Showcase Suite",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentTeal,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = when (activeSubTab) {
                            "voice" -> "Voice-Activated Inventory"
                            "khata" -> "Family Khata & UPI Ledger"
                            "flash" -> "Discount Dispatch Alerts"
                            else -> "Store Discoverability Map"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TrustNavy
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                when (activeSubTab) {
                    "voice" -> VoiceStockingTab(viewModel)
                    "khata" -> FamilyKhataTab(viewModel)
                    "flash" -> FlashSalesTab(viewModel)
                    "map" -> StoreDiscoveryTab(viewModel)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 1: VOICE-STOCKING DEMO TERMINAL
// -------------------------------------------------------------
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun VoiceStockingTab(viewModel: LocalSyncViewModel, isScrollable: Boolean = true) {
    val itemsList by viewModel.inventory.collectAsState()
    val isRecording by viewModel.isRecordingVoice.collectAsState()
    val transcript by viewModel.voiceTranscript.collectAsState()
    val parsedResult by viewModel.voiceParsedResult.collectAsState()
    val isProcessed by viewModel.isVoiceProcessed.collectAsState()

    var animatedWaveOffset by remember { mutableStateOf(0f) }

    // Wave tick simulation
    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (true) {
                delay(50)
                animatedWaveOffset += 0.4f
            }
        }
    }

    if (isScrollable) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Explaining helper
            item {
                VoiceStockingHeader()
            }

            // Voice simulator mic controller card
            item {
                VoiceStockingMicConsole(
                    isRecording = isRecording,
                    animatedWaveOffset = animatedWaveOffset,
                    transcript = transcript,
                    parsedResult = parsedResult,
                    isProcessed = isProcessed,
                    viewModel = viewModel
                )
            }

            // Live Inventory Count table title
            item {
                VoiceStockingLedgerHeader(itemsSize = itemsList.size)
            }

            // Items listing grid
            items(itemsList, key = { it.id }) { item ->
                VoiceStockingLedgerItem(item = item)
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VoiceStockingHeader()
            VoiceStockingMicConsole(
                isRecording = isRecording,
                animatedWaveOffset = animatedWaveOffset,
                transcript = transcript,
                parsedResult = parsedResult,
                isProcessed = isProcessed,
                viewModel = viewModel
            )
            VoiceStockingLedgerHeader(itemsSize = itemsList.size)
            itemsList.forEach { item ->
                VoiceStockingLedgerItem(item = item)
            }
        }
    }
}

@Composable
private fun VoiceStockingHeader() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Slate200)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                "Interactive Speech Decoder Terminal",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TrustNavy
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Tap a voice command preset below to simulate a shopkeeper registering inventory by voice. Your voice input is parsed natively, then automatically appended to your stock count list below.",
                fontSize = 11.sp,
                color = Slate600,
                lineHeight = 16.sp
            )
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun VoiceStockingMicConsole(
    isRecording: Boolean,
    animatedWaveOffset: Float,
    transcript: String,
    parsedResult: String,
    isProcessed: Boolean,
    viewModel: LocalSyncViewModel
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Slate200),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Microphone Console",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400
                )

                if (isRecording) {
                    Surface(
                        color = Color(0xFFFEF2F2),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(0.6.dp, Color(0xFFFCA5A5))
                    ) {
                        Text(
                            "LIVE LISTENING",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Slate900, RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
            ) {
                if (isRecording) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val midY = size.height / 2f
                        val width = size.width
                        val strokeColor = ActionGreen
                        val count = 50
                        for (i in 0 until count) {
                            val x = (width / count) * i
                            val amplitude = 12f * sin(i * 0.35f + animatedWaveOffset) + 8f * sin(i * 0.15f - animatedWaveOffset)
                            val heightCorrection = if (i < 10 || i > 40) 0.1f else 1.0f
                            val finalAmp = amplitude * heightCorrection
                            drawLine(
                                color = strokeColor,
                                start = Offset(x, midY - finalAmp),
                                end = Offset(x, midY + finalAmp),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Waveform static. Tap preset to trigger voice input.",
                            color = Slate400,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            var customCommandText by remember { mutableStateOf("") }

            OutlinedTextField(
                value = customCommandText,
                onValueChange = { customCommandText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Or speak/type custom command...", fontSize = 11.sp) },
                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (customCommandText.isNotBlank()) {
                                viewModel.processSpeechInputCombined(customCommandText)
                                customCommandText = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Command",
                            tint = TrustNavy,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                supportingText = {
                    Text(
                        text = "Try: 'give 15% discount' or 'add 10 bottles Amul Milk at 65 rupees'",
                        fontSize = 9.sp,
                        color = Slate400
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TrustNavy,
                    unfocusedBorderColor = Slate200,
                    focusedContainerColor = Slate50,
                    unfocusedContainerColor = Slate50
                ),
                shape = RoundedCornerShape(4.dp)
            )

            Text(
                text = "Voice Presets (Hinglish)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400,
                modifier = Modifier.align(Alignment.Start)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LocalSyncSeeds.voicePresets.forEach { preset ->
                    SuggestionChip(
                        onClick = { viewModel.simulateVoicePreset(preset) },
                        label = {
                            Text(
                                text = preset.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(containerColor = Slate100),
                        shape = RoundedCornerShape(4.dp)
                    )
                }
            }

            AnimatedVisibility(visible = transcript.isNotEmpty()) {
                Surface(
                    color = Slate100,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            "CAPTURED TRANSCRIPT:",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            "\"$transcript\"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TrustNavy
                        )

                        if (parsedResult.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "AUTOMATED DATABASE RESOLUTION:",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = ActionGreen,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                parsedResult,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ActionGreen
                            )
                        }
                    }
                }
            }

            if (isProcessed) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.dismissVoiceResult() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate600),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Discard", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.executeCommittedVoiceResult() },
                        colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.weight(2f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.CloudUpload, contentDescription = "Sync", modifier = Modifier.size(16.dp))
                            Text("Approve and Commit", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VoiceStockingLedgerHeader(itemsSize: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Live Shopkeeper Ledger ($itemsSize SKUs)",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TrustNavy
        )

        Surface(
            color = Color(0xFFE0F2FE),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                "DATABASE DESCRIPTOR",
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = AccentTeal,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun VoiceStockingLedgerItem(item: InventoryItem) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Slate200),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = TrustNavy
                    )
                    Surface(
                        color = Slate100,
                        shape = RoundedCornerShape(3.dp)
                    ) {
                        Text(
                            item.category,
                            fontSize = 8.sp,
                            color = Slate600,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
                Text(
                    text = "Last logged: ${item.lastUpdated}",
                    color = Slate400,
                    fontSize = 10.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${item.quantity} ${item.unit}s",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TrustNavy
                )
                Text(
                    text = "Unit: ₹${item.pricePerUnit}",
                    fontSize = 10.sp,
                    color = Slate600
                )
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: FAMILY-KHATA CREDIT CIRCLE LEDGER & UPI CHANNELS
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyKhataTab(viewModel: LocalSyncViewModel) {
    val familiesList by viewModel.families.collectAsState()
    val activeFamilyPaying by viewModel.paymentSelectedFamily.collectAsState()
    val paymentState by viewModel.paymentState.collectAsState()
    val paymentProvider by viewModel.paymentProvider.collectAsState()

    var activeExpenseAmount by remember { mutableStateOf("") }
    var activeExpenseLabel by remember { mutableStateOf("Wheat Flour Supply") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Direct Description header
        item {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Slate200)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Automated Credit Tracking ('Khata Ledger')",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TrustNavy
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Manage mutual lines of payment credit for local neighborhood families securely. Families receive a 6-digit Ledger Match Code to view active balances, clearing outstandings via secure immediate UPI settlements.",
                        fontSize = 11.sp,
                        color = Slate600,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Active Family Khata Registers
        item {
            Text(
                "Active Neighborhood Khata Lines (${familiesList.size} Families)",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = TrustNavy
            )
        }

        items(familiesList) { family ->
            val isBeingPaid = activeFamilyPaying?.id == family.id
            Surface(
                color = if (isBeingPaid) Color(0xFFF0FDFA) else Color.White,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(if (isBeingPaid) 1.2.dp else 1.dp, if (isBeingPaid) AccentTeal else Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Header title info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = family.familyName,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = TrustNavy
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(color = Slate100, shape = RoundedCornerShape(2.dp)) {
                                    Text(
                                        "Code: ${family.passCode}",
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = Slate600,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    "Members: ${family.registeredMembers.size}",
                                    fontSize = 10.sp,
                                    color = Slate400
                                )
                            }
                        }

                        // Credit balance counter
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (family.balance < 0) "₹${Math.abs(family.balance)}" else "₹${family.balance}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = if (family.balance < 0) CrimsonRed else ActionGreen
                            )
                            Text(
                                text = if (family.balance < 0) "Outstanding Credit" else "In Surplus Balance",
                                fontSize = 9.sp,
                                color = Slate400,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Registered family constituents
                    Text(
                        text = "Participants: " + family.registeredMembers.joinToString(", "),
                        fontSize = 10.sp,
                        color = Slate600
                    )

                    // Unlocked milestones
                    if (family.rewardUnlocked != null) {
                        Surface(
                            color = Color(0xFFECFDF5),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(0.6.dp, ActionGreen.copy(0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CardGiftcard,
                                    contentDescription = "Reward",
                                    tint = ActionGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "FAMILY RETENTION AWARD: ${family.rewardUnlocked}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ActionGreen
                                )
                            }
                        }
                    }

                    // Interactive Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quick credit expense trigger
                        Button(
                            onClick = {
                                viewModel.addCreditPurchase(
                                    familyId = family.id,
                                    description = "Store Groceries Log",
                                    amount = 400.0
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Slate100),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(30.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Add, "add", tint = TrustNavy, modifier = Modifier.size(12.dp))
                                Text("Add buy ₹400", color = TrustNavy, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Settle via UPI launcher
                        Button(
                            onClick = { viewModel.triggerCheckoutPay(family.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isBeingPaid) AccentTeal else TrustNavy),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(30.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Payment, "pay", tint = Color.White, modifier = Modifier.size(12.dp))
                                Text(
                                    if (isBeingPaid) "Checkout Selected" else "Resolve Ledger Balance",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Selected family secure payment box
        activeFamilyPaying?.let { payingFamily ->
            if (payingFamily.balance < 0) {
                val absDebt = Math.abs(payingFamily.balance)
                item {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.2.dp, ActionGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                "Secure UPI Settlement Desk",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TrustNavy
                            )

                            Surface(
                                color = Slate50,
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("PENDING OUTSTANDING BILL DETAILS", fontSize = 9.sp, color = Slate400, fontWeight = FontWeight.Bold)
                                    Text(payingFamily.familyName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TrustNavy)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Balance Settlement Owed:", fontSize = 11.sp, color = Slate600)
                                        Text("₹$absDebt", fontWeight = FontWeight.Black, fontSize = 14.sp, color = CrimsonRed)
                                    }
                                }
                            }

                            // Payment Methods Switchers
                            Text("CHOOSE SETTLEMENT METHOD:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate400)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("GPay", "PhonePe", "Paytm").forEach { provider ->
                                    val isSelected = paymentProvider == provider
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { viewModel.updatePaymentProvider(provider) },
                                        color = if (isSelected) Color(0xFFECFDF5) else Color.White,
                                        border = BorderStroke(1.dp, if (isSelected) ActionGreen else Slate200),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = provider,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (isSelected) ActionGreen else Slate600,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }

                            // Payment execution module
                            when (paymentState) {
                                "idle" -> {
                                    Button(
                                        onClick = { viewModel.runUPIPaymentFlow(absDebt) },
                                        colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Pay Outstanding ₹$absDebt via $paymentProvider",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                "in_progress" -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            color = ActionGreen,
                                            modifier = Modifier.size(20.dp),
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            "Verifying secure ABDM bank lines...",
                                            fontSize = 11.sp,
                                            color = Slate400,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                "completed" -> {
                                    Surface(
                                        color = Color(0xFFECFDF5),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Success",
                                                    tint = ActionGreen,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Text(
                                                    "UPI Settlement Success!",
                                                    color = ActionGreen,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp
                                                )
                                            }
                                            Text(
                                                "Ledger matches synced completely 1:1.",
                                                fontSize = 10.sp,
                                                color = Slate600
                                            )

                                            Button(
                                                onClick = { viewModel.resetPaymentWidget() },
                                                colors = ButtonDefaults.buttonColors(containerColor = Slate100),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                                                shape = RoundedCornerShape(4.dp),
                                                modifier = Modifier.height(24.dp)
                                            ) {
                                                Text("Done", color = TrustNavy, fontSize = 10.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: DISCOUNTS DISPATCH ALERTS (FLASH-SALES)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashSalesTab(viewModel: LocalSyncViewModel) {
    val activePercent by viewModel.discountPercentSelection.collectAsState()
    val countdown by viewModel.countdownTimer.collectAsState()
    val storesList by viewModel.stores.collectAsState()

    var discountSliderValue by remember { mutableStateOf(30f) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Descriptive Header
        item {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Slate200)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Flash Sales: Instant Discount Dispatcher",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TrustNavy
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Broadcast rapid expiration discount alerts (e.g. expiring dairy stock or excess snacks) straight to nearby family Lock Screens. Local families can see deals and rush to clear your inventory.",
                        fontSize = 11.sp,
                        color = Slate600,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Active Discount trigger board
        item {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Configure Rapid Flash Campaign",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TrustNavy
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "Select Discount Level: ${discountSliderValue.toInt()}% OFF",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )

                    Slider(
                        value = discountSliderValue,
                        onValueChange = { discountSliderValue = it },
                        valueRange = 10f..80f,
                        steps = 6,
                        colors = SliderDefaults.colors(
                            thumbColor = ActionGreen,
                            activeTrackColor = ActionGreen,
                            inactiveTrackColor = Slate100
                        )
                    )

                    Button(
                        onClick = { viewModel.launchFlashSale(discountSliderValue.toInt()) },
                        colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Bolt, "Flash", modifier = Modifier.size(16.dp))
                            Text(
                                "Dispatch ${discountSliderValue.toInt()}% Off Campaign!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Lock screen dispatch simulator preview
        item {
            Surface(
                color = Slate900,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "CONSUMER LOCK SCREEN PREVIEW",
                            color = Slate400,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )

                        Surface(
                            color = ActionGreen,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                "ALERT TESTER",
                                fontSize = 7.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Simulated push card
                    Surface(
                        color = Color.White.copy(0.12f),
                        border = BorderStroke(0.8.dp, Color.White.copy(0.2f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(AccentTeal, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Storefront, "shop", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Ramesh Kirana Store: FLASH DEAL ON",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$activePercent% Limited Instant Off Discount is active!",
                                    color = ActionGreen,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "Expires in: $countdown (Tap to Open Navigation Map)",
                                    color = Color.White.copy(0.7f),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    // Aesthetic countdown visual clocks
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Active Deal Counter:", fontSize = 10.sp, color = Slate400)
                        Surface(
                            color = Color(0xFFFEF2F2),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = countdown,
                                color = CrimsonRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 4: STORE DISCOVERY NATIVE NEIGHBORHOOD MAPS
// -------------------------------------------------------------
@Composable
fun StoreDiscoveryTab(viewModel: LocalSyncViewModel, isScrollable: Boolean = true) {
    val query by viewModel.storeSearchQuery.collectAsState()
    val storesList by viewModel.stores.collectAsState()
    val selectedId by viewModel.selectedStoreId.collectAsState()

    val filteredStores = storesList.filter {
        query.isEmpty() || it.name.lowercase().contains(query.lowercase()) ||
                it.popularProducts.any { prod -> prod.lowercase().contains(query.lowercase()) }
    }

    if (isScrollable) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Slate200)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "Consumer Search & Store Discoverability",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TrustNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Families look up specific food stocks (like 'Parle-G' or 'Milk') of local proximity shops. Interactive location grids show exactly which store holds stock with active flash discount markers.",
                            fontSize = 11.sp,
                            color = Slate600,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { viewModel.updateStoreSearchQuery(it) },
                    placeholder = { Text("Search product (e.g. Parle-G, Atta, Milk)...", fontSize = 12.sp, color = Slate400) },
                    prefix = { Icon(Icons.Default.Search, "search", tint = Slate400, modifier = Modifier.padding(end = 4.dp).size(16.dp)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TrustNavy,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                StoreDiscoveryMapContainer(
                    viewModel = viewModel,
                    filteredStores = filteredStores,
                    selectedId = selectedId,
                    query = query
                )
            }

            item {
                Text(
                    "Nearby Merchants (${filteredStores.size} Stores Available)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TrustNavy
                )
            }

            items(filteredStores) { store ->
                val isSelected = selectedId == store.id
                Surface(
                    color = if (isSelected) Color(0xFFECFDF5) else Color.White,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(if (isSelected) 1.2.dp else 1.dp, if (isSelected) ActionGreen else Slate200),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectMapStore(store.id) }
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(if (isSelected) ActionGreen else Slate100, RoundedCornerShape(50)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        store.contactNo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color.White else TrustNavy
                                    )
                                }
                                Column {
                                    Text(
                                        text = store.name,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = TrustNavy
                                    )
                                    Text(
                                        text = store.distance,
                                        fontSize = 10.sp,
                                        color = Slate400
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Icon(Icons.Default.Star, "rating", tint = Color(0xFFEAB308), modifier = Modifier.size(14.dp))
                                Text(store.rating.toString(), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TrustNavy)
                            }
                        }
                        Text(
                            text = "Popular SKUs: " + store.popularProducts.joinToString(" • "),
                            fontSize = 10.sp,
                            color = Slate600
                        )
                        if (store.hasDiscount) {
                            Surface(
                                color = Color(0xFFFEF2F2),
                                border = BorderStroke(0.6.dp, CrimsonRed.copy(0.4f)),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.LocalOffer, "offer", tint = CrimsonRed, modifier = Modifier.size(12.dp))
                                    Text(
                                        "ACTIVE DEAL OUTLET: ${store.discountPercent}% Off Flash Sale is Live!",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Slate200)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Consumer Search & Store Discoverability",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TrustNavy
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Families look up specific food stocks (like 'Parle-G' or 'Milk') of local proximity shops. Interactive location grids show exactly which store holds stock with active flash discount markers.",
                        fontSize = 11.sp,
                        color = Slate600,
                        lineHeight = 16.sp
                    )
                }
            }

            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.updateStoreSearchQuery(it) },
                placeholder = { Text("Search product (e.g. Parle-G, Atta, Milk)...", fontSize = 12.sp, color = Slate400) },
                prefix = { Icon(Icons.Default.Search, "search", tint = Slate400, modifier = Modifier.padding(end = 4.dp).size(16.dp)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TrustNavy,
                    unfocusedBorderColor = Slate200,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth()
            )

            StoreDiscoveryMapContainer(
                viewModel = viewModel,
                filteredStores = filteredStores,
                selectedId = selectedId,
                query = query
            )

            Text(
                "Nearby Merchants (${filteredStores.size} Stores Available)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TrustNavy
            )

            filteredStores.forEach { store ->
                val isSelected = selectedId == store.id
                Surface(
                    color = if (isSelected) Color(0xFFECFDF5) else Color.White,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(if (isSelected) 1.2.dp else 1.dp, if (isSelected) ActionGreen else Slate200),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectMapStore(store.id) }
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(if (isSelected) ActionGreen else Slate100, RoundedCornerShape(50)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        store.contactNo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color.White else TrustNavy
                                    )
                                }
                                Column {
                                    Text(
                                        text = store.name,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = TrustNavy
                                    )
                                    Text(
                                        text = store.distance,
                                        fontSize = 10.sp,
                                        color = Slate400
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Icon(Icons.Default.Star, "rating", tint = Color(0xFFEAB308), modifier = Modifier.size(14.dp))
                                Text(store.rating.toString(), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TrustNavy)
                            }
                        }
                        Text(
                            text = "Popular SKUs: " + store.popularProducts.joinToString(" • "),
                            fontSize = 10.sp,
                            color = Slate600
                        )
                        if (store.hasDiscount) {
                            Surface(
                                color = Color(0xFFFEF2F2),
                                border = BorderStroke(0.6.dp, CrimsonRed.copy(0.4f)),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.LocalOffer, "offer", tint = CrimsonRed, modifier = Modifier.size(12.dp))
                                    Text(
                                        "ACTIVE DEAL OUTLET: ${store.discountPercent}% Off Flash Sale is Live!",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val Slate300 = Color(0xFFCBD5E1)

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GoogleMapWebView(
    stores: List<Store>,
    selectedStoreId: String?,
    searchQuery: String,
    onStoreSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    val htmlContent = remember(stores, searchQuery) {
        val storesJsonArray = stores.map { s ->
            val lat = when (s.id) {
                "1" -> 28.6253
                "2" -> 28.6285
                else -> 28.6212
            }
            val lng = when (s.id) {
                "1" -> 77.2182
                "2" -> 77.2112
                else -> 77.2274
            }
            val escapedName = s.name.replace("'", "\\'")
            val productsList = s.popularProducts.map { p -> "'${p.replace("'", "\\'")}'" }.joinToString(prefix = "[", postfix = "]", separator = ",")
            """
            {
                id: '${s.id}',
                name: '$escapedName',
                distance: '${s.distance}',
                rating: ${s.rating},
                hasDiscount: ${s.hasDiscount},
                discountPercent: ${s.discountPercent},
                lat: $lat,
                lng: $lng,
                products: $productsList
            }
            """.trimIndent()
        }.joinToString(prefix = "[", postfix = "]", separator = ",")

        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                html, body, #map {
                    height: 100%;
                    width: 100%;
                    margin: 0;
                    padding: 0;
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                }
                .custom-popup .leaflet-popup-content-wrapper {
                    background: #ffffff;
                    color: #0f172a;
                    border-radius: 8px;
                    padding: 4px;
                    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                }
                .custom-popup .leaflet-popup-tip {
                    background: #ffffff;
                }
                .store-title {
                    font-size: 14px;
                    font-weight: 800;
                    color: #1e3a8a;
                    margin: 0 0 4px 0;
                }
                .store-meta {
                    font-size: 11px;
                    color: #64748b;
                    margin-bottom: 6px;
                    display: flex;
                    align-items: center;
                    gap: 6px;
                }
                .store-rating {
                    color: #eab308;
                    font-weight: bold;
                }
                .sku-list {
                    background: #f1f5f9;
                    border-radius: 4px;
                    padding: 6px;
                    margin-top: 6px;
                }
                .sku-title {
                    font-size: 10px;
                    text-transform: uppercase;
                    font-weight: bold;
                    color: #475569;
                    margin-bottom: 4px;
                }
                .sku-tag {
                    display: inline-block;
                    background: #e2e8f0;
                    color: #334155;
                    font-size: 10px;
                    padding: 2px 6px;
                    border-radius: 30px;
                    margin: 2px;
                    font-weight: 600;
                }
                .sku-tag.matched {
                    background: #dcfce7;
                    color: #15803d;
                    border: 1px solid #86efac;
                }
                .discount-badge {
                    display: inline-block;
                    background: #fee2e2;
                    color: #ef4444;
                    font-size: 10px;
                    font-weight: bold;
                    padding: 4px 8px;
                    border-radius: 4px;
                    margin-top: 6px;
                    border: 1px solid #fca5a5;
                    width: 100%;
                    box-sizing: border-box;
                    text-align: center;
                }
                .nav-btn {
                    display: block;
                    width: 100%;
                    background: #1e3a8a;
                    color: white;
                    text-align: center;
                    padding: 6px;
                    border-radius: 4px;
                    font-size: 11px;
                    font-weight: bold;
                    text-decoration: none;
                    margin-top: 8px;
                    cursor: pointer;
                    border: none;
                }
                .user-dot {
                    width: 12px;
                    height: 12px;
                    background: #3b82f6;
                    border: 2px solid white;
                    border-radius: 50%;
                    box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.4);
                    animation: pulse 1.5s infinite;
                }
                @keyframes pulse {
                    0% { box-shadow: 0 0 0 0px rgba(59, 130, 246, 0.5); }
                    70% { box-shadow: 0 0 0 8px rgba(59, 130, 246, 0); }
                    100% { box-shadow: 0 0 0 0px rgba(59, 130, 246, 0); }
                }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script>
                var map = L.map('map', {
                    zoomControl: false,
                    attributionControl: false
                }).setView([28.6241, 77.2198], 15);

                L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
                    maxZoom: 20
                }).addTo(map);

                L.control.zoom({ position: 'topright' }).addTo(map);

                var userIcon = L.divIcon({
                    className: 'user-marker',
                    html: '<div class="user-dot"></div>',
                    iconSize: [16, 16],
                    iconAnchor: [8, 8]
                });
                L.marker([28.6241, 77.2198], { icon: userIcon }).addTo(map);

                var stores = $storesJsonArray;
                var markers = {};

                stores.forEach(function(store) {
                    var query = "${searchQuery.lowercase()}";
                    var isMatched = false;
                    
                    var productTags = store.products.map(function(p) {
                        var matchedClass = "";
                        if (query !== "" && p.toLowerCase().indexOf(query) !== -1) {
                            matchedClass = " matched";
                            isMatched = true;
                        }
                        return '<span class="sku-tag' + matchedClass + '">' + p + '</span>';
                    }).join('');

                    var markerColor = store.hasDiscount ? '#10b981' : '#1e3a8a';
                    if (isMatched && query !== "") {
                        markerColor = '#10b981';
                    }

                    var tempIcon = L.divIcon({
                        className: 'store-pin',
                        html: '<div style="background:' + markerColor + '; width:24px; height:24px; border-radius:50%; border:2.5px solid white; display:flex; align-items:center; justify-content:center; color:white; font-size:11px; font-weight:900; box-shadow:0 3px 6px rgba(0,0,0,0.3);">' + store.id + '</div>',
                        iconSize: [24, 24],
                        iconAnchor: [12, 12]
                    });

                    var popupHtml = '<div class="custom-popup">' +
                        '<div class="store-title">' + store.name + '</div>' +
                        '<div class="store-meta">' +
                            '<span class="store-rating">★ ' + store.rating + '</span> • ' +
                            '<span>' + store.distance + '</span>' +
                        '</div>';

                    if (store.hasDiscount) {
                        popupHtml += '<div class="discount-badge">' + store.discountPercent + '% OFF FLASH DEAL LIVE</div>';
                    }

                    popupHtml += '<div class="sku-list">' +
                        '<div class="sku-title">Available Products</div>' +
                        '<div>' + productTags + '</div>' +
                        '</div>';

                    popupHtml += '<button class="nav-btn" onclick="selectStore(' + store.id + ')">Select Store</button>' +
                        '</div>';

                    var marker = L.marker([store.lat, store.lng], { icon: tempIcon })
                        .addTo(map)
                        .bindPopup(popupHtml, { maxWidth: 220, closeButton: false });

                    markers[store.id] = marker;

                    marker.on('click', function() {
                        selectStore(store.id);
                    });
                });

                function selectStore(id) {
                    if (window.AndroidMapsBridge) {
                        window.AndroidMapsBridge.selectStore(id.toString());
                    }
                }

                function highlightStore(id) {
                    var marker = markers[id];
                    var storeInfo = stores.find(function(s) { return s.id === id; });
                    if (marker && storeInfo) {
                        map.setView([storeInfo.lat - 0.001, storeInfo.lng], 16);
                        marker.openPopup();
                    }
                }

                var activeSearchQuery = "${searchQuery.lowercase()}";
                if (activeSearchQuery !== "") {
                    var matchedStore = stores.find(function(s) {
                        return s.name.toLowerCase().indexOf(activeSearchQuery) !== -1 || 
                               s.products.some(function(p) { return p.toLowerCase().indexOf(activeSearchQuery) !== -1; });
                    });
                    if (matchedStore) {
                        setTimeout(function() {
                            highlightStore(matchedStore.id);
                        }, 500);
                    }
                }
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    LaunchedEffect(selectedStoreId) {
        if (selectedStoreId != null) {
            webViewRef.value?.evaluateJavascript("highlightStore('$selectedStoreId')", null)
        }
    }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                }
                
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (selectedStoreId != null) {
                            view?.evaluateJavascript("highlightStore('$selectedStoreId')", null)
                        }
                    }
                }

                addJavascriptInterface(object : Any() {
                    @JavascriptInterface
                    fun selectStore(id: String) {
                        onStoreSelected(id)
                    }
                }, "AndroidMapsBridge")

                webViewRef.value = this
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL("https://unpkg.com", htmlContent, "text/html", "UTF-8", null)
        },
        modifier = modifier
    )
}

@Composable
fun StoreDiscoveryMapContainer(
    viewModel: LocalSyncViewModel,
    filteredStores: List<Store>,
    selectedId: String?,
    query: String,
    modifier: Modifier = Modifier
) {
    var mapType by rememberSaveable { mutableStateOf("google") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        // Dynamically request FINE and COARSE location permissions from the system
    }

    LaunchedEffect(mapType) {
        if (mapType == "google") {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                onClick = { mapType = "google" },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (mapType == "google") TrustNavy else Color.White,
                    contentColor = if (mapType == "google") Color.White else Slate600
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Google Map",
                        modifier = Modifier.size(16.dp),
                        tint = if (mapType == "google") Color.White else TrustNavy
                    )
                    Text(
                        text = "Google Maps LIVE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Surface(
                        color = if (mapType == "google") ActionGreen else Slate100,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "ONLINE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (mapType == "google") Color.White else Slate400,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = { mapType = "vector" },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (mapType == "vector") TrustNavy else Color.White,
                    contentColor = if (mapType == "vector") Color.White else Slate600
                ),
                border = BorderStroke(1.dp, if (mapType == "vector") TrustNavy else Slate200),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = "Vector Grid",
                        modifier = Modifier.size(16.dp),
                        tint = if (mapType == "vector") Color.White else Slate400
                    )
                    Text(
                        text = "Vector Grid (Local)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        if (mapType == "google") {
            Surface(
                color = Color.White,
                border = BorderStroke(1.dp, Slate200),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                GoogleMapWebView(
                    stores = filteredStores,
                    selectedStoreId = selectedId,
                    searchQuery = query,
                    onStoreSelected = { storeId ->
                        viewModel.selectMapStore(storeId)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Surface(
                color = Color(0xFFF1F5F9),
                border = BorderStroke(1.dp, Slate200),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val accentClr = Slate300
                        val gridGap = size.width / 5f
                        for (i in 1..4) {
                            drawLine(
                                color = accentClr,
                                start = Offset(gridGap * i, 0f),
                                end = Offset(gridGap * i, size.height),
                                strokeWidth = 14.dp.toPx()
                            )
                        }
                        drawLine(
                            color = accentClr,
                            start = Offset(0f, size.height * 0.3f),
                            end = Offset(size.width, size.height * 0.3f),
                            strokeWidth = 14.dp.toPx()
                        )
                        drawLine(
                            color = accentClr,
                            start = Offset(0f, size.height * 0.7f),
                            end = Offset(size.width, size.height * 0.7f),
                            strokeWidth = 14.dp.toPx()
                        )
                    }

                    filteredStores.forEachIndexed { idx, store ->
                        val (xFraction, yFraction) = when (idx) {
                            0 -> 0.22f to 0.28f
                            1 -> 0.58f to 0.48f
                            else -> 0.82f to 0.74f
                        }
                        Box(
                            modifier = Modifier
                                .absoluteOffset(
                                    x = (320 * xFraction).dp - 12.dp,
                                    y = (180 * yFraction).dp - 12.dp
                                )
                                .clickable { viewModel.selectMapStore(store.id) }
                        ) {
                            val isSelected = selectedId == store.id
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(if (isSelected) ActionGreen else TrustNavy, RoundedCornerShape(50))
                                    .border(BorderStroke(1.2.dp, Color.White), RoundedCornerShape(50)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = store.contactNo,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .absoluteOffset(x = 130.dp, y = 90.dp)
                    ) {
                        Surface(
                            color = Color(0xFF3B82F6),
                            shape = CircleShape,
                            modifier = Modifier.size(12.dp),
                            border = BorderStroke(2.dp, Color.White)
                        ) {}
                    }
                }
            }
        }
    }
}
