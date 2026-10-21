package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.GamePreset
import com.example.data.GamingPresets
import com.example.model.ClipMode
import com.example.model.ClipProject
import com.example.model.UserSubscription
import com.example.ui.components.ClipCard
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: ClipForgeViewModel,
    subscription: UserSubscription,
    projects: List<ClipProject>,
    onNavigateToProjects: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToPro: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDemoDialog by remember { mutableStateOf(false) }
    var showUploadDialog by remember { mutableStateOf(false) }
    var selectedDemoGame by remember { mutableStateOf(GamingPresets.supportedGames[0]) }
    var selectedClipMode by remember { mutableStateOf(ClipMode.VIRAL) }

    // Upload dialog inputs
    var customVideoTitle by remember { mutableStateOf("") }
    var customGameTitle by remember { mutableStateOf("Valorant") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }

    // Video file picker contract
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            showUploadDialog = true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoidBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Section with Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.clipforge_hero_banner),
                            contentDescription = "ClipForge AI Banner",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, DarkSurface.copy(alpha = 0.95f))
                                    )
                                )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Required Exact Headline & Subheadline
                        Text(
                            text = "Turn Hours of Gameplay Into Viral Clips.",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 23.sp,
                                lineHeight = 28.sp
                            ),
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Upload your gaming recording. AI finds the moments worth watching.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            ),
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Primary Button: "Upload Gameplay" & Secondary: "Try Demo"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    videoPickerLauncher.launch("video/*")
                                },
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(48.dp)
                                    .testTag("upload_gameplay_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ElectricCyan,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Upload Gameplay",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = { showDemoDialog = true },
                                modifier = Modifier
                                    .weight(0.9f)
                                    .height(48.dp)
                                    .testTag("try_demo_button"),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = FieryOrange
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, FieryOrange),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Try Demo",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Navigation Tabs: My Projects, Export History, Pro
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickNavCard(
                    title = "My Projects",
                    subtitle = "${projects.size} Saved",
                    icon = Icons.Default.Folder,
                    tint = ElectricCyan,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToProjects
                )
                QuickNavCard(
                    title = "Exports",
                    subtitle = "History & Share",
                    icon = Icons.Default.History,
                    tint = FieryOrange,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToHistory
                )
                QuickNavCard(
                    title = "AI Credits",
                    subtitle = "${subscription.aiCredits} Left",
                    icon = Icons.Default.AutoAwesome,
                    tint = FlameGold,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToPro
                )
            }
        }

        // AI Clip Modes Selector
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI Clip Modes",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Smart Focus",
                        fontSize = 11.sp,
                        color = ElectricCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ClipMode.values()) { mode ->
                        val isSelected = selectedClipMode == mode
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ElectricCyan else DarkSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) ElectricCyan else DarkCardBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedClipMode = mode }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Column {
                                Text(
                                    text = mode.badge,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSelected) Color.Black else ElectricCyan
                                )
                                Text(
                                    text = mode.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.Black else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Supported Games Section (Minecraft, GTA, PUBG/BGMI, Free Fire, Valorant, Fortnite, Call of Duty, Roblox)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Supported Games",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Trained AI Models",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(GamingPresets.supportedGames) { game ->
                        Card(
                            modifier = Modifier
                                .width(135.dp)
                                .clickable {
                                    selectedDemoGame = game
                                    showDemoDialog = true
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(text = game.iconEmoji, fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = game.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = game.subtitle,
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recent Generated Highlights Showcase
        val allClips = projects.flatMap { it.clips }
        if (allClips.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Viral Clips",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "${allClips.size} Ready",
                        fontSize = 11.sp,
                        color = FlameGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(allClips.take(3)) { clip ->
                ClipCard(
                    clip = clip,
                    isPro = subscription.isPro,
                    onEditClick = { viewModel.selectClipForEditor(it) },
                    onDownloadClick = { viewModel.startExport(it, "1080p", !subscription.isPro) },
                    onShareClick = { /* Handled in MainActivity or parent */ },
                    onFavoriteToggle = { viewModel.toggleFavorite(it) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Try Demo Dialog
    if (showDemoDialog) {
        AlertDialog(
            onDismissRequest = { showDemoDialog = false },
            title = {
                Text(
                    text = "Launch AI Demo Analysis",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Select a popular game and AI mode to run the full highlight detection pipeline:",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Text(
                        text = "Choose Game:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GamingPresets.supportedGames.forEach { game ->
                            val isSel = selectedDemoGame.id == game.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) ElectricCyan else DarkSurfaceVariant)
                                    .clickable { selectedDemoGame = game }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${game.iconEmoji} ${game.title}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.Black else Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Detection Mode:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FieryOrange
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ClipMode.values().forEach { mode ->
                            val isSel = selectedClipMode == mode
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) FieryOrange else DarkSurfaceVariant)
                                    .clickable { selectedClipMode = mode }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = mode.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else TextSecondary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDemoDialog = false
                        viewModel.startDemoAnalysis(selectedDemoGame, selectedClipMode)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = Color.Black)
                ) {
                    Text("Analyze Now (10 Credits)", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDemoDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkSurface
        )
    }

    // Upload Gameplay Dialog
    if (showUploadDialog) {
        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = {
                Text(
                    text = "Configure Video Analysis",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Video selected: ${selectedVideoUri?.lastPathSegment ?: "Gameplay.mp4"}",
                        fontSize = 12.sp,
                        color = ElectricCyan,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = customVideoTitle,
                        onValueChange = { customVideoTitle = it },
                        label = { Text("Recording Title") },
                        placeholder = { Text("e.g. Ranked Placement Match") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = customGameTitle,
                        onValueChange = { customGameTitle = it },
                        label = { Text("Game Name") },
                        placeholder = { Text("e.g. Valorant / Minecraft / GTA") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "AI Optimization Mode:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FieryOrange
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ClipMode.values().forEach { mode ->
                            val isSel = selectedClipMode == mode
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) ElectricCyan else DarkSurfaceVariant)
                                    .clickable { selectedClipMode = mode }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = mode.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.Black else Color.White
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showUploadDialog = false
                        viewModel.startCustomUploadAnalysis(
                            videoTitle = customVideoTitle,
                            gameTitle = customGameTitle,
                            mode = selectedClipMode,
                            videoUri = selectedVideoUri?.toString()
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = Color.Black)
                ) {
                    Text("Start AI Analysis", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkSurface
        )
    }
}

@Composable
private fun QuickNavCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }
    }
}
