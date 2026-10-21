package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SwitchCamera
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GamingPresets
import com.example.model.CaptionStyle
import com.example.model.CropRatio
import com.example.model.GamingClip
import com.example.model.UserSubscription
import com.example.ui.components.VideoPlayerSimulation
import com.example.ui.components.ViralScoreBadge
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FieryOrange
import com.example.ui.theme.FlameGold
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.ClipForgeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditorScreen(
    viewModel: ClipForgeViewModel,
    clip: GamingClip?,
    subscription: UserSubscription,
    onBackClick: () -> Unit,
    onExportClick: (GamingClip) -> Unit,
    modifier: Modifier = Modifier
) {
    if (clip == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(DarkVoidBg),
            contentAlignment = Alignment.Center
        ) {
            Text("No clip selected", color = TextSecondary)
        }
        return
    }

    // Editable state hooks
    var editedCaptionText by remember(clip.id) { mutableStateOf(clip.captionText) }
    var editedHookText by remember(clip.id) { mutableStateOf(clip.hookText) }
    var musicVolume by remember(clip.id) { mutableFloatStateOf(clip.musicVolume) }

    // Range slider for trimming (startTime to endTime)
    var trimRange by remember(clip.id) {
        mutableStateOf(clip.startTimeSec.toFloat()..(clip.endTimeSec.toFloat()))
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoidBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurface)
                            .testTag("editor_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "AI Clip Studio",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = clip.title.take(24),
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Button(
                    onClick = { onExportClick(clip) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricCyan,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("editor_export_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Live Dynamic Preview Player
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ViralScoreBadge(score = clip.viralScore, compact = true)
                        Text(
                            text = "Active Aspect: ${clip.cropRatio.ratioLabel}",
                            fontSize = 10.sp,
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    VideoPlayerSimulation(
                        clip = clip,
                        isPro = subscription.isPro,
                        showControls = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
            }
        }

        // Trim Video Section (Start & End time slider)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TRIM TIMELINE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                        Text(
                            text = "Duration: ${(trimRange.endInclusive - trimRange.start).toInt()}s",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FlameGold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    val minVal = (clip.startTimeSec - 20).coerceAtLeast(0).toFloat()
                    val maxVal = (clip.endTimeSec + 25).toFloat()

                    RangeSlider(
                        value = trimRange,
                        onValueChange = { range ->
                            trimRange = range
                            viewModel.updateActiveClip(
                                startTimeSec = range.start.toInt(),
                                endTimeSec = range.endInclusive.toInt()
                            )
                        },
                        valueRange = minVal..maxVal,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan,
                            inactiveTrackColor = Color(0xFF26324D)
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Start: ${trimRange.start.toInt()}s",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "End: ${trimRange.endInclusive.toInt()}s",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Change Crop / Aspect Ratio (9:16 Vertical, Split Screen, 1:1, 16:9)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "SMART CROPPING & RATIO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(CropRatio.values()) { ratio ->
                            val isSel = clip.cropRatio == ratio
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) ElectricCyan else DarkSurfaceVariant)
                                    .border(
                                        1.dp,
                                        if (isSel) ElectricCyan else DarkCardBorder,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        viewModel.updateActiveClip(cropRatio = ratio)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = ratio.displayName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) Color.Black else Color.White
                                    )
                                    Text(
                                        text = ratio.ratioLabel,
                                        fontSize = 9.sp,
                                        color = if (isSel) Color(0xFF1E293B) else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Captions Style & Custom Text Editor
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ANIMATED CAPTIONS STYLE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )

                        // AI Regenerate Captions Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceVariant)
                                .clickable { viewModel.regenerateAiCaptions(clip) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = FlameGold,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AI Regenerate",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FlameGold
                            )
                        }
                    }

                    // Style Pickers
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(CaptionStyle.values()) { style ->
                            val isSel = clip.captionStyle == style
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) FieryOrange else DarkSurfaceVariant)
                                    .clickable {
                                        viewModel.updateActiveClip(captionStyle = style)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = style.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else TextSecondary
                                )
                            }
                        }
                    }

                    // Caption Text Field
                    OutlinedTextField(
                        value = editedCaptionText,
                        onValueChange = {
                            editedCaptionText = it
                            viewModel.updateActiveClip(captionText = it)
                        },
                        label = { Text("Subtitles Text") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Hook / Text Overlay Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "VIRAL HOOK OVERLAY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FlameGold
                        )

                        // Regenerate Hook Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceVariant)
                                .clickable { viewModel.regenerateAiHook(clip) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "New Hook",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricCyan
                            )
                        }
                    }

                    OutlinedTextField(
                        value = editedHookText,
                        onValueChange = {
                            editedHookText = it
                            viewModel.updateActiveClip(hookText = it)
                        },
                        label = { Text("Intro Hook Banner") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FlameGold,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Add Music & Volume Adjuster
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
                        text = "BACKGROUND MUSIC",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(GamingPresets.royaltyFreeTracks) { track ->
                            val isSel = clip.musicTrack?.id == track.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) ElectricCyan else DarkSurfaceVariant)
                                    .clickable {
                                        viewModel.updateActiveClip(musicTrack = track)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = null,
                                        tint = if (isSel) Color.Black else TextSecondary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = track.title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) Color.Black else Color.White
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Volume",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Slider(
                            value = musicVolume,
                            onValueChange = {
                                musicVolume = it
                                viewModel.updateActiveClip(musicVolume = it)
                            },
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = ElectricCyan,
                                activeTrackColor = ElectricCyan,
                                inactiveTrackColor = Color(0xFF26324D)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${(musicVolume * 100).toInt()}%",
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Toggle Options: Zoom Effects, Silence Removal, Facecam Position
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
                        text = "AI EFFECTS & FACE-CAM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )

                    // Auto Zoom Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Auto-Zoom on Action Moments", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("Dynamically scales frame on eliminations", fontSize = 10.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = clip.zoomEffects,
                            onCheckedChange = { viewModel.updateActiveClip(zoomEffects = it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = ElectricCyan, checkedTrackColor = Color(0xFF005566))
                        )
                    }

                    // Auto Silence Removal Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Auto Silence Removal", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("Cuts boring downtime to increase retention", fontSize = 10.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = clip.silenceRemoved,
                            onCheckedChange = { viewModel.updateActiveClip(silenceRemoved = it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = ElectricCyan, checkedTrackColor = Color(0xFF005566))
                        )
                    }

                    // Facecam Cutout Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Facecam Overlay", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("Positions streamer camera with active live indicator", fontSize = 10.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = clip.facecamEnabled,
                            onCheckedChange = { viewModel.updateActiveClip(facecamEnabled = it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = FieryOrange, checkedTrackColor = Color(0xFF552200))
                        )
                    }

                    if (clip.facecamEnabled) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Facecam Position", fontSize = 12.sp, color = TextSecondary)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurfaceVariant)
                                    .clickable {
                                        viewModel.updateActiveClip(facecamTop = !clip.facecamTop)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (clip.facecamTop) "Top Left 📍" else "Bottom Left 📍",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
