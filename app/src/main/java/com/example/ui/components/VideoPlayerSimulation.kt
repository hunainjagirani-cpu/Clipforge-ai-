package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CaptionStyle
import com.example.model.CropRatio
import com.example.model.GamingClip
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FieryOrange
import com.example.ui.theme.FlameGold
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerSimulation(
    clip: GamingClip,
    modifier: Modifier = Modifier,
    isPro: Boolean = false,
    showControls: Boolean = true,
    compact: Boolean = false
) {
    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var isMuted by remember { mutableStateOf(false) }

    // Live playback ticker
    LaunchedEffect(isPlaying, clip.durationSec) {
        if (isPlaying) {
            val totalSec = clip.durationSec.coerceAtLeast(1)
            while (true) {
                delay(100L)
                currentProgress += 0.1f / totalSec.toFloat()
                if (currentProgress >= 1f) {
                    currentProgress = 0f
                }
            }
        }
    }

    // Dynamic zoom pulse transition
    val infiniteTransition = rememberInfiniteTransition(label = "zoom_pulse")
    val zoomScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (clip.zoomEffects) 1.06f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "zoom"
    )

    // Animated karaoke caption word index
    val captionWords = remember(clip.captionText) {
        clip.captionText.split(" ").filter { it.isNotBlank() }
    }
    val activeWordIndex = (currentProgress * captionWords.size).toInt().coerceIn(0, (captionWords.size - 1).coerceAtLeast(0))

    val currentSecondsText = String.format("%02d:%02d", ((currentProgress * clip.durationSec).toInt()) / 60, ((currentProgress * clip.durationSec).toInt()) % 60)
    val totalSecondsText = String.format("%02d:%02d", clip.durationSec / 60, clip.durationSec % 60)

    val targetAspectRatio = when (clip.cropRatio) {
        CropRatio.VERTICAL_9_16 -> 9f / 16f
        CropRatio.SPLIT_SCREEN -> 9f / 16f
        CropRatio.SQUARE_1_1 -> 1f
        CropRatio.LANDSCAPE_16_9 -> 16f / 9f
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .border(1.dp, Color(0xFF222C42), RoundedCornerShape(16.dp))
            .testTag("video_player_simulation")
    ) {
        // Aspect ratio container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(targetAspectRatio)
                .clip(RoundedCornerShape(16.dp))
                .clickable { isPlaying = !isPlaying },
            contentAlignment = Alignment.Center
        ) {
            // Background Video Image Simulation
            Image(
                painter = painterResource(id = R.drawable.sample_clip_frame),
                contentDescription = "Simulated Gameplay Frame",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(if (clip.zoomEffects) zoomScale else 1.0f),
                contentScale = ContentScale.Crop
            )

            // Split screen mode divider & overlay if selected
            if (clip.cropRatio == CropRatio.SPLIT_SCREEN) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color(0x33000000)),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = "CAM • 60 FPS",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(ElectricCyan)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.6f),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = "GAME FEED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = FieryOrange,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // Facecam Cutout Overlay (if enabled & not split screen)
            if (clip.facecamEnabled && clip.cropRatio != CropRatio.SPLIT_SCREEN) {
                Box(
                    modifier = Modifier
                        .align(if (clip.facecamTop) Alignment.TopStart else Alignment.BottomStart)
                        .padding(14.dp)
                        .size(if (compact) 48.dp else 66.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp,
                            Brush.sweepGradient(listOf(ElectricCyan, NeonPurple, FieryOrange)),
                            CircleShape
                        )
                        .background(Color(0xFF0F172A))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🔴 LIVE",
                            fontSize = if (compact) 7.sp else 8.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFF3366)
                        )
                        Text(
                            text = "FACE",
                            fontSize = if (compact) 8.sp else 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Viral Hook Banner (top area)
            if (clip.hookText.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xCC000000))
                        .border(1.dp, ElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = clip.hookText,
                        fontSize = if (compact) 10.sp else 12.sp,
                        fontWeight = FontWeight.Black,
                        color = FlameGold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Animated Captions burned in
            if (captionWords.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    RenderAnimatedCaption(
                        captionStyle = clip.captionStyle,
                        words = captionWords,
                        activeWordIndex = activeWordIndex,
                        compact = compact
                    )
                }
            }

            // Watermark (shows on Free plan or if watermark enabled)
            if (clip.watermark && !isPro) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = if (showControls) 54.dp else 12.dp, end = 12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x99000000))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ClipForge AI",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Sound Energy Waveform overlay (bottom)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = if (showControls) 48.dp else 6.dp, start = 14.dp, end = 14.dp)
                    .height(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val waveHeights = listOf(4, 12, 16, 8, 14, 18, 10, 6, 14, 18, 12, 16, 8, 15, 6, 18, 10, 14, 8)
                waveHeights.forEachIndexed { idx, baseH ->
                    val dynamicH = if (isPlaying) {
                        (baseH * (0.6f + (Math.sin((currentProgress.toDouble() * 20.0) + idx.toDouble()).toFloat() * 0.4f))).coerceIn(3f, 18f)
                    } else baseH.toFloat()
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(dynamicH.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (idx % 3 == 0) FieryOrange else ElectricCyan)
                    )
                }
            }

            // Center Play Icon when paused
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xCC00F0FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Bottom Controls Bar (Scrubber, Play/Pause, Audio Mute, Timecode)
        if (showControls) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkVoidBg)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                // Slider Scrubber
                Slider(
                    value = currentProgress,
                    onValueChange = {
                        currentProgress = it
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = ElectricCyan,
                        activeTrackColor = ElectricCyan,
                        inactiveTrackColor = Color(0xFF26324D)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = ElectricCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { isMuted = !isMuted },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                                contentDescription = "Audio Toggle",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "$currentSecondsText / $totalSecondsText",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Music track pill if assigned
                    if (clip.musicTrack != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E2538))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "Music",
                                tint = FieryOrange,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = clip.musicTrack.title.take(16),
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RenderAnimatedCaption(
    captionStyle: CaptionStyle,
    words: List<String>,
    activeWordIndex: Int,
    compact: Boolean
) {
    val fontSize = if (compact) 13.sp else 18.sp

    when (captionStyle) {
        CaptionStyle.HORMOZI -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                words.forEachIndexed { index, word ->
                    val isHighlighted = index == activeWordIndex
                    Text(
                        text = "$word ",
                        fontSize = if (isHighlighted) (fontSize.value + 3).sp else fontSize,
                        fontWeight = FontWeight.Black,
                        color = if (isHighlighted) FlameGold else Color.White
                    )
                }
            }
        }

        CaptionStyle.TIKTOK_BOUNCE -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xCC0F172A))
                    .border(1.dp, NeonGreen.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                words.forEachIndexed { index, word ->
                    val isHighlighted = index == activeWordIndex
                    Text(
                        text = "$word ",
                        fontSize = fontSize,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isHighlighted) NeonGreen else ElectricCyan
                    )
                }
            }
        }

        CaptionStyle.NEON_CYBER -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xCC111827))
                    .border(1.dp, NeonPurple, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                words.forEachIndexed { index, word ->
                    val isHighlighted = index == activeWordIndex
                    Text(
                        text = "$word ",
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (isHighlighted) ElectricCyan else NeonPurple
                    )
                }
            }
        }

        CaptionStyle.FIRE_ANIME -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xDD2D0B00))
                    .border(1.dp, FieryOrange, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                words.forEachIndexed { index, word ->
                    val isHighlighted = index == activeWordIndex
                    Text(
                        text = "$word ",
                        fontSize = if (isHighlighted) (fontSize.value + 2).sp else fontSize,
                        fontWeight = FontWeight.Black,
                        color = if (isHighlighted) FlameGold else FieryOrange
                    )
                }
            }
        }

        CaptionStyle.CLEAN_SUBTITLE -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xAA000000))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = words.joinToString(" "),
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
