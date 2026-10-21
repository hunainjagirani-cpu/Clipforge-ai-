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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClipProject
import com.example.model.GamingClip
import com.example.model.UserSubscription
import com.example.ui.components.ClipCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FieryOrange
import com.example.ui.theme.FlameGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.ClipForgeViewModel

@Composable
fun ResultsScreen(
    viewModel: ClipForgeViewModel,
    project: ClipProject?,
    subscription: UserSubscription,
    onBackClick: () -> Unit,
    onShareClip: (GamingClip) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All Clips") }

    val clips = project?.clips ?: emptyList()
    val filteredClips = when (selectedFilter) {
        "Viral 90+" -> clips.filter { it.viralScore >= 90 }
        "Clutches" -> clips.filter { it.category.contains("Clutch", ignoreCase = true) }
        "Funny" -> clips.filter { it.category.contains("Funny", ignoreCase = true) }
        else -> clips
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoidBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar & Back Button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurface)
                            .testTag("results_back_button")
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
                            text = "AI Generated Clips",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "${project?.gameTitle ?: "Gameplay"} • ${clips.size} clips found",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Batch Export Button
                Button(
                    onClick = {
                        val topClip = clips.maxByOrNull { it.viralScore }
                        if (topClip != null) {
                            viewModel.startExport(topClip, "1080p", !subscription.isPro)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("export_best_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Export Top",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Summary Card
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
                    Column {
                        Text(
                            text = "HIGHEST VIRAL POTENTIAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FlameGold
                        )
                        Text(
                            text = "${clips.maxOfOrNull { it.viralScore } ?: 98}/100 Score",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E2538))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Mode: ${project?.selectedMode?.title ?: "Viral"}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                    }
                }
            }
        }

        // Filter Pills Row
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("All Clips", "Viral 90+", "Clutches", "Funny")) { filter ->
                    val isSel = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSel) ElectricCyan else DarkSurface)
                            .border(1.dp, if (isSel) ElectricCyan else DarkCardBorder, RoundedCornerShape(20.dp))
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) Color.Black else Color.White
                        )
                    }
                }
            }
        }

        // Generated Clips Cards List
        if (filteredClips.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No clips match this filter.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(filteredClips) { clip ->
                ClipCard(
                    clip = clip,
                    isPro = subscription.isPro,
                    onEditClick = { viewModel.selectClipForEditor(it) },
                    onDownloadClick = { viewModel.startExport(it, "1080p", !subscription.isPro) },
                    onShareClick = { onShareClip(it) },
                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
