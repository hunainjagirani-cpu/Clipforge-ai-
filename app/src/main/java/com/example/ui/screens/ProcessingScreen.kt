package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.service.AiVideoAnalyzer
import com.example.service.AnalysisProgress
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

@Composable
fun ProcessingScreen(
    progress: AnalysisProgress?,
    modifier: Modifier = Modifier
) {
    val currentProgress = progress?.progress ?: 0.1f
    val currentStageName = progress?.stageName ?: "Analyzing Gameplay"
    val currentSubtitle = progress?.stageSubtitle ?: "Scanning video frames..."
    val currentLog = progress?.liveLog ?: "Initializing AI vision engine..."

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val radarPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radar"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoidBg)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Video Preview Scanner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("processing_video_preview"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sample_clip_frame),
                        contentDescription = "Scanning Video",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Cyber Scanner Grid Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0x3300F0FF),
                                        Color(0x880B0E17)
                                    )
                                )
                            )
                    )

                    // Radar scanning badge
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xCC000000))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radar,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI NEURAL SCANNER ACTIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = ElectricCyan
                        )
                    }

                    // Progress overlay on video
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(72.dp)
                        ) {
                            CircularProgressIndicator(
                                progress = { currentProgress },
                                modifier = Modifier.size(72.dp),
                                color = ElectricCyan,
                                strokeWidth = 5.dp,
                                trackColor = Color(0x44FFFFFF)
                            )
                            Text(
                                text = "${(currentProgress * 100).toInt()}%",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Current Stage Title & Status
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentStageName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    ),
                    color = ElectricCyan
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = currentSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { currentProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = ElectricCyan,
                    trackColor = Color(0xFF1E2538)
                )
            }
        }

        // Required Animated Processing Indicator with 6 Stages:
        // "Analyzing Gameplay", "Finding Highlights", "Detecting Reactions",
        // "Creating Clips", "Adding Captions", "Almost Ready"
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("processing_stages_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "AI PIPELINE STAGES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )

                    AiVideoAnalyzer.STAGES.forEachIndexed { index, pair ->
                        val stageName = pair.first
                        val stageIndex = AiVideoAnalyzer.STAGES.indexOfFirst { it.first == currentStageName }
                        val isDone = index < stageIndex || currentProgress >= 1f
                        val isCurrent = index == stageIndex && currentProgress < 1f

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Completed",
                                    tint = NeonGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else if (isCurrent) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = ElectricCyan,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Circle,
                                    contentDescription = "Pending",
                                    tint = Color(0xFF334155),
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = stageName,
                                    fontSize = 13.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                    color = when {
                                        isDone -> TextPrimary
                                        isCurrent -> ElectricCyan
                                        else -> TextSecondary.copy(alpha = 0.6f)
                                    }
                                )
                                if (isCurrent) {
                                    Text(
                                        text = pair.second,
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Live Telemetry Stream / Detected Events
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LIVE TELEMETRY STREAM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FieryOrange
                        )
                        Text(
                            text = "${progress?.detectedCount ?: 2} Events Detected",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FlameGold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "> $currentLog",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF38BDF8),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
