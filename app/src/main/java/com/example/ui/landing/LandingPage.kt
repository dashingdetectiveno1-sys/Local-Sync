package com.example.ui.landing

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LocalSyncSeeds
import com.example.data.Testimonial
import com.example.ui.theme.*
import com.example.viewmodel.LocalSyncViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LandingPage(
    viewModel: LocalSyncViewModel,
    onNavigateToDemoTab: (String) -> Unit, // "voice", "khata", "flash", "map"
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

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
            // 1. WEB HEADER MOCKUP
            WebHeaderSection(onNavigateToDemoTab)

            // 2. HERO SPLIT-SCREEN LAYOUT
            HeroSplitSection(onNavigateToDemoTab)

            // 3. TRUST BANNER (India Stack & ABDM compliance)
            TrustComplianceBanner()

            // 4. LIVE HERO VIDEO PLACEHOLDER / SPEECH INTERACTIVE DEMO ACCENT
            InteractiveSpeechExplainer(viewModel, onNavigateToDemoTab)

            // 5. FOUR-CARD SHARP FEATURE SHOWCASE GRID
            FeatureGridSection(onNavigateToDemoTab)

            // 6. TESTIMONIALS / MERCHANTS SUCCESS STORIES
            MerchantSuccessStories()

            // Spacer for sticky CTA overlap
            Spacer(modifier = Modifier.height(100.dp))
        }

        // 7. STICKY DUAL CTA FLOATING BAR
        StickyCTAActionBar(onNavigateToDemoTab)
    }
}

@Composable
fun WebHeaderSection(onNavigateToDemoTab: (String) -> Unit) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                // Sharp border bottom for high-end feel
                drawLine(
                    color = Slate200,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo with leaf/arrow icon in Teal/Green
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SyncAlt,
                    contentDescription = "Sync Icon",
                    tint = AccentTeal,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "LocalSync",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TrustNavy,
                        letterSpacing = (-0.5).sp
                    )
                )
            }

            // Web Navigation Links (Hidden on small screens or styled elegantly)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Pricing",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate600,
                    modifier = Modifier.clickable { }
                )
                Text(
                    text = "Resources",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate600,
                    modifier = Modifier.clickable { }
                )
                Text(
                    text = "Sandbox Trial",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ActionGreen,
                    modifier = Modifier.clickable { onNavigateToDemoTab("voice") }
                )
            }
        }
    }
}

@Composable
fun HeroSplitSection(onNavigateToDemoTab: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Left Side: Copy, Buttons, Trust Badges
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tag label
            Surface(
                color = Slate100,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = "NEIGHBORHOOD TECH",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TrustNavy,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    letterSpacing = 1.sp
                )
            }

            // High impact headline
            Text(
                text = "Digitalize Your Shop.\nEmpower Your Family.",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TrustNavy,
                    lineHeight = 38.sp,
                    letterSpacing = (-0.8).sp
                )
            )

            // Sub-headline
            Text(
                text = "The trusted neighborhood network for real-time inventory tracking, Family Khata credit circles, and rapid local flash discounts.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    color = Slate600,
                    lineHeight = 22.sp
                )
            )

            // Direct Call to Action triggers in the Hero block
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onNavigateToDemoTab("voice") },
                    colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "For Shopkeepers",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                OutlinedButton(
                    onClick = { onNavigateToDemoTab("khata") },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TrustNavy),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, TrustNavy),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "For Families",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Right Side: High fidelity flat stylized shopkeeper smartphone mockup (Native Vector Mockup instead of AI photo)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 0.dp,
            border = BorderStroke(1.2.dp, Slate200),
            color = Slate50
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mock smartphone header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = ActionGreen,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Text(
                            text = "Live Shopkeeper Terminal",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate600
                        )
                    }
                    Text(
                        text = "9:41 AM",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate400,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Inner mock interface representing the Shopkeeper layout
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Slate200),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Ramesh Kirana Store",
                                fontWeight = FontWeight.Bold,
                                color = TrustNavy,
                                fontSize = 13.sp
                            )
                            Surface(
                                color = Slate100,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    "STABLE ACTIVE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentTeal,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Horizontal status boxes
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Slate50, RoundedCornerShape(4.dp))
                                    .padding(8.dp)
                            ) {
                                Text("SKUs Stocked", fontSize = 9.sp, color = Slate400)
                                Text("184 Items", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TrustNavy)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Slate50, RoundedCornerShape(4.dp))
                                    .padding(8.dp)
                            ) {
                                Text("Khata Balance", fontSize = 9.sp, color = Slate400)
                                Text("₹18,450.00", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CrimsonRed)
                            }
                        }

                        // Simulated voice stock log row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFECFDF5), RoundedCornerShape(4.dp))
                                .border(BorderStroke(0.8.dp, ActionGreen.copy(0.4f)), RoundedCornerShape(4.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice",
                                tint = ActionGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "Speak '20 packets of Parle-G added'",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Text(
                                    text = "Auto-parsed: +20 Parle-G added to database",
                                    fontSize = 9.sp,
                                    color = Slate600
                                )
                            }
                        }
                    }
                }

                // CTA to trigger Demo
                TextButton(
                    onClick = { onNavigateToDemoTab("voice") },
                    modifier = Modifier.align(Alignment.End),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        "Try Interactive Voice Deck →",
                        fontSize = 12.sp,
                        color = AccentTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Global visual color fallback for liabilities
val CrimsonRed = Color(0xFFDC2626)

@Composable
fun TrustComplianceBanner() {
    Surface(
        color = Slate100,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "COMMITTED TO DIGITAL TRUST & SECURITY",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ABHA Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Verified ABDM Compliance",
                        tint = TrustNavy,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "ABDM / India Stack",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TrustNavy
                    )
                }

                // UPI Integration
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Instant Settlement Secure",
                        tint = ActionGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Instant UPI Settlement",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TrustNavy
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveSpeechExplainer(
    viewModel: LocalSyncViewModel,
    onNavigateToDemoTab: (String) -> Unit
) {
    val isRecording by viewModel.isRecordingVoice.collectAsState()
    val transcript by viewModel.voiceTranscript.collectAsState()
    val parsedResult by viewModel.voiceParsedResult.collectAsState()
    val isProcessed by viewModel.isVoiceProcessed.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Illiterate-Friendly UI: Simple Voice Input",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TrustNavy,
                letterSpacing = (-0.5).sp
            )
        )

        Text(
            text = "To serve non-tech-savvy or illiterate shopkeepers, LocalSync eliminates raw keyboard entry. Simply tap, talk, and let our voice processing engine log the ledger. Play with the live simulator below:",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Slate600,
                lineHeight = 20.sp
            )
        )

        // Simulated screen interface card
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Slate200),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Voice-Stocking Deck",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TrustNavy
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = Color(0xFFFFB020), // Amber
                            shape = RoundedCornerShape(50)
                        ) {
                            Spacer(modifier = Modifier.size(6.dp))
                        }
                        Text("Interactive Trial", fontSize = 10.sp, color = Slate400, fontWeight = FontWeight.Bold)
                    }
                }

                // Interactive Presets Buttons
                Text(
                    "Select a sample verbal command to simulate speech detection:",
                    fontSize = 11.sp,
                    color = Slate600,
                    modifier = Modifier.align(Alignment.Start)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LocalSyncSeeds.voicePresets.forEach { preset ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Slate50,
                            border = BorderStroke(1.dp, Slate200),
                            onClick = { viewModel.simulateVoicePreset(preset) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Voice Preset",
                                    tint = AccentTeal,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column {
                                    Text(preset.label, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TrustNavy)
                                    Text(preset.transcript, fontSize = 11.sp, color = Slate600)
                                }
                            }
                        }
                    }
                }

                // Output Screen Mock
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    color = Slate900,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "MOCK MIC RECEIVER",
                                color = Slate400,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            if (isRecording) {
                                Text(
                                    "RECEIVING...",
                                    color = ActionGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                if (transcript.isNotEmpty()) {
                                    Text(
                                        text = "\"$transcript\"",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = "Tap a verbal command preset above to test voice mapping...",
                                        color = Slate400,
                                        fontSize = 12.sp
                                    )
                                }

                                if (parsedResult.isNotEmpty()) {
                                    Text(
                                        text = "✔ $parsedResult",
                                        color = ActionGreen,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        if (isProcessed) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.applyVoiceInputToInventory(
                                            name = if (parsedResult.contains("Parle-G")) "Parle-G Biscuit" else if (parsedResult.contains("Aashirvaad")) "Aashirvaad Atta 5kg" else "Amul Gold Milk 1L",
                                            quantity = if (parsedResult.contains("20")) 20 else if (parsedResult.contains("10")) 10 else 30,
                                            price = if (parsedResult.contains("₹10") || parsedResult.contains("rupees per pack")) 10.0 else if (parsedResult.contains("₹260")) 260.0 else 66.0,
                                            category = "Voice Added"
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("Add to Stock", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedButton(
                                    onClick = { viewModel.dismissVoiceResult() },
                                    border = BorderStroke(0.8.dp, Color.White),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("Clear", fontSize = 10.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureGridSection(onNavigateToDemoTab: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Four Pillars of LocalSync",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TrustNavy,
                letterSpacing = (-0.5).sp
            )
        )

        Text(
            text = "Every feature is tailored to create high-frequency transactions, transparent lines of loyalty status, and automated inventory systems.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Slate600,
                lineHeight = 20.sp
            )
        )

        // The Feature Showcase Grid (Four Sharp Cards)
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FeatureShowcaseCard(
                icon = Icons.Default.Mic,
                title = "1. Voice-Stocking",
                tagline = "Restock inventory using only your voice.",
                description = "Illiterate shopkeepers register custom goods updates instantly. No typing, no English, zero catalog clutter.",
                demoTabName = "voice",
                onNavigateToDemoTab = onNavigateToDemoTab
            )

            FeatureShowcaseCard(
                icon = Icons.Default.People,
                title = "2. Family-Khata",
                tagline = "Transcend paper records to digital ledgers.",
                description = "Manage credit accounts across families cleanly. Transparent ledger syncing with OTP validation ensures zero dispute accounts.",
                demoTabName = "khata",
                onNavigateToDemoTab = onNavigateToDemoTab
            )

            FeatureShowcaseCard(
                icon = Icons.Default.FlashOn,
                title = "3. Flash-Sales",
                tagline = "Broadcast bargains in 3 seconds flat.",
                description = "Launch localized store flash discounts directly on neighboring family lock screens. Drive target foot traffic for expiring inventory.",
                demoTabName = "flash",
                onNavigateToDemoTab = onNavigateToDemoTab
            )

            FeatureShowcaseCard(
                icon = Icons.Default.CardGiftcard,
                title = "4. Family Loyalty",
                tagline = "Automate neighborhood credit circle rewards.",
                description = "Keep families locked to your shop. System awards loyalty tier cuts on timely checkout clearing.",
                demoTabName = "khata",
                onNavigateToDemoTab = onNavigateToDemoTab
            )
        }
    }
}

@Composable
fun FeatureShowcaseCard(
    icon: ImageVector,
    title: String,
    tagline: String,
    description: String,
    demoTabName: String,
    onNavigateToDemoTab: (String) -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Slate200),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Slate100, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = TrustNavy,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = TrustNavy
                )
            }

            Text(
                text = tagline,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = AccentTeal
            )

            Text(
                text = description,
                fontSize = 12.sp,
                color = Slate600,
                lineHeight = 18.sp
            )

            TextButton(
                onClick = { onNavigateToDemoTab(demoTabName) },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Test This Module",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = ActionGreen
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Test",
                        tint = ActionGreen,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MerchantSuccessStories() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Merchant Growth Stories",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TrustNavy,
                letterSpacing = (-0.5).sp
            )
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(LocalSyncSeeds.testimonials) { item ->
                TestimonialCard(item)
            }
        }
    }
}

@Composable
fun TestimonialCard(testimonial: Testimonial) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Slate200),
        modifier = Modifier
            .width(280.dp)
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Stats Badge Highlight
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = Color(0xFFECFDF5), // Action Green background hue
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, ActionGreen.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = testimonial.statKeyword,
                        fontSize = 11.sp,
                        color = ActionGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = testimonial.statDescription,
                    fontSize = 9.sp,
                    color = Slate400,
                    fontWeight = FontWeight.Medium
                )
            }

            // Paragraph Quote
            Text(
                text = testimonial.quote,
                fontSize = 12.sp,
                color = Slate600,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )

            HorizontalDivider(color = Slate100)

            // Owner details Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(TrustNavy, RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = testimonial.avatarInitial,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Column {
                    Text(
                        text = testimonial.ownerName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = TrustNavy
                    )
                    Text(
                        text = testimonial.shopName,
                        fontSize = 10.sp,
                        color = Slate400
                    )
                }
            }
        }
    }
}

@Composable
fun StickyCTAActionBar(onNavigateToDemoTab: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Transparent)
    ) {
        Surface(
            color = TrustNavy.copy(0.97f),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .drawBehind {
                    drawLine(
                        color = Slate600.copy(0.2f),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Become LocalSync Enabled",
                        fontSize = 11.sp,
                        color = ActionGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "100% Android Mobile First",
                        fontSize = 9.sp,
                        color = Color.White.copy(0.7f)
                    )
                }

                Button(
                    onClick = { onNavigateToDemoTab("voice") },
                    colors = ButtonDefaults.buttonColors(containerColor = ActionGreen),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayForWork,
                            contentDescription = "Install Apk",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Download App Demo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
