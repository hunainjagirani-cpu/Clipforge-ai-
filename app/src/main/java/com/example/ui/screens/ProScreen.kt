package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserSubscription
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FieryOrange
import com.example.ui.theme.FlameGold
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.ClipForgeViewModel

@Composable
fun ProScreen(
    viewModel: ClipForgeViewModel,
    subscription: UserSubscription,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedBillingAnnual by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoidBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurface)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "ClipForge Pro VIP",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = TextPrimary
                    )
                    Text(
                        text = "Unlock Ultra 4K, Unlimited Exports & Zero Watermarks",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Current Account Status Pill
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (subscription.isPro) NeonPurple else DarkCardBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CURRENT MEMBERSHIP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Text(
                            text = subscription.planName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = if (subscription.isPro) FlameGold else ElectricCyan
                        )
                        Text(
                            text = "AI Credits: ${subscription.aiCredits} balance",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    if (subscription.isPro) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.horizontalGradient(listOf(NeonPurple, FieryOrange)))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "ACTIVE VIP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.upgradeToPro() },
                            colors = ButtonDefaults.buttonColors(containerColor = FlameGold, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Upgrade", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Billing Cycle Toggle (Monthly vs Annual - Save 40%)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (selectedBillingAnnual) ElectricCyan else Color.Transparent)
                        .clickable { selectedBillingAnnual = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Annual (Save 45%)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedBillingAnnual) Color.Black else TextSecondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (selectedBillingAnnual) Color.Black else FlameGold)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "BEST",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = if (selectedBillingAnnual) ElectricCyan else Color.Black
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (!selectedBillingAnnual) ElectricCyan else Color.Transparent)
                        .clickable { selectedBillingAnnual = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Monthly ($14.99/mo)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!selectedBillingAnnual) Color.Black else TextSecondary
                    )
                }
            }
        }

        // Price Hero Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B36)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (selectedBillingAnnual) "$7.99 / month" else "$14.99 / month",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = if (selectedBillingAnnual) "Billed annually at $95.88/year • Cancel anytime" else "Billed monthly • Cancel anytime",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.upgradeToPro() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("activate_pro_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Diamond, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (subscription.isPro) "VIP Active (Manage)" else "Start ClipForge Pro",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Freemium Comparison Matrix
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "PLAN COMPARISON",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )

                    val comparisonRows = listOf(
                        Triple("Monthly AI Credits", "60 Credits", "500 Credits (Refillable)"),
                        Triple("Exports per Month", "3 Exports / mo", "Unlimited Exports"),
                        Triple("Video Watermark", "ClipForge Badge", "Zero Watermark"),
                        Triple("Maximum Quality", "720p / 1080p", "4K 60fps Ultra"),
                        Triple("Processing Speed", "Standard Queue", "Instant High-Priority"),
                        Triple("Clips per Video", "3 Clips", "Up to 10 Clips"),
                        Triple("Caption Styles", "2 Basic Styles", "All 5 Viral Animations"),
                        Triple("Facecam Cutout", "Fixed position", "Custom drag & reposition")
                    )

                    comparisonRows.forEach { (feature, freeVal, proVal) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = feature,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                modifier = Modifier.weight(1.2f)
                            )
                            Text(
                                text = freeVal,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                modifier = Modifier.weight(0.9f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = proVal,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FlameGold,
                                modifier = Modifier.weight(1.1f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }

        // Top-Up AI Credits Pack
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Need More Credits?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "+100 AI Credits Instant Refill",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.refillCredits() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FlameGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FlameGold),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Refill +100", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
