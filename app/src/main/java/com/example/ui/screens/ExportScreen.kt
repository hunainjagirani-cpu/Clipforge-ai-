package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GamingClip
import com.example.model.UserSubscription
import com.example.ui.components.VideoPlayerSimulation
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
import com.example.viewmodel.ExportProgressState

@Composable
fun ExportScreen(
    viewModel: ClipForgeViewModel,
    clip: GamingClip?,
    subscription: UserSubscription,
    exportState: ExportProgressState,
    onBackClick: () -> Unit,
    onUpgradeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedResolution by remember { mutableStateOf("1080p Full HD") }
    var removeWatermark by remember { mutableStateOf(subscription.isPro) }

    if (clip == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(DarkVoidBg),
            contentAlignment = Alignment.Center
        ) {
            Text("No clip selected for export", color = TextSecondary)
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoidBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
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
                        text = "Export Viral Video",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Ready for TikTok, Shorts & Reels",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Preview Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = clip.title,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    VideoPlayerSimulation(
                        clip = clip,
                        isPro = subscription.isPro || removeWatermark,
                        showControls = false,
                        compact = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    )
                }
            }
        }

        // Resolution Selector (720p, 1080p, 4K 60fps)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "EXPORT RESOLUTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )

                    val resolutions = listOf(
                        Pair("720p HD", "Fast render • 18MB"),
                        Pair("1080p Full HD", "Recommended for Shorts • 38MB"),
                        Pair("4K 60fps Ultra", "Crisp quality (PRO) • 92MB")
                    )

                    resolutions.forEach { (res, desc) ->
                        val is4k = res.contains("4K")
                        val isSelected = selectedResolution == res

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ElectricCyan.copy(alpha = 0.15f) else DarkSurfaceVariant)
                                .border(
                                    1.dp,
                                    if (isSelected) ElectricCyan else DarkCardBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    if (is4k && !subscription.isPro) {
                                        onUpgradeClick()
                                    } else {
                                        selectedResolution = res
                                    }
                                }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = res,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ElectricCyan else Color.White
                                    )
                                    if (is4k) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(NeonPurple)
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text("PRO", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                                        }
                                    }
                                }
                                Text(text = desc, fontSize = 10.sp, color = TextSecondary)
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Watermark Setting (Free vs Pro)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Remove ClipForge Watermark",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (!subscription.isPro) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(FlameGold)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("PRO", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                }
                            }
                        }
                        Text(
                            text = if (subscription.isPro) "Watermark disabled for Pro creators" else "Watermark removal requires Pro plan",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Switch(
                        checked = removeWatermark,
                        onCheckedChange = { checked ->
                            if (!subscription.isPro && checked) {
                                onUpgradeClick()
                            } else {
                                removeWatermark = checked
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = FlameGold,
                            checkedTrackColor = Color(0xFF664400)
                        )
                    )
                }
            }
        }

        // Export Progress or Render Action
        item {
            if (exportState.isExporting) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan)
                ) {
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
                                text = "RENDERING VIDEO...",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricCyan
                            )
                            Text(
                                text = "${(exportState.progress * 100).toInt()}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = FlameGold
                            )
                        }

                        LinearProgressIndicator(
                            progress = { exportState.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = ElectricCyan,
                            trackColor = Color(0xFF1E2538)
                        )

                        Text(
                            text = exportState.stage,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else if (exportState.finishedItem != null) {
                // Export Complete Card with Sharing Actions
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F291E)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Video Exported to Device!",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "Resolution: ${exportState.finishedItem.resolution} • File Size: ${exportState.finishedItem.fileSizeMb}MB",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )

                        // Share Intent Launcher
                        Button(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "🔥 Watch this insane ${clip.gameTitle} clip made with ClipForge AI!\n${clip.hookText}\n\n${clip.aiHashtags.joinToString(" ")}"
                                    )
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share Clip to Socials")
                                context.startActivity(shareIntent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share to TikTok / Shorts / Reels", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // Primary Export Button
                Button(
                    onClick = {
                        viewModel.startExport(clip, selectedResolution, removeWatermark)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("render_export_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = Color.Black),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.VideoFile, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Render & Save Video ($selectedResolution)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
