package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.GamingClip
import com.example.ui.components.AppHeader
import com.example.ui.screens.EditorScreen
import com.example.ui.screens.ExportHistoryScreen
import com.example.ui.screens.ExportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProcessingScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.ProScreen
import com.example.ui.screens.ResultsScreen
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FieryOrange
import com.example.ui.theme.FlameGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.ClipForgeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ClipForgeApp()
            }
        }
    }
}

@Composable
fun ClipForgeApp(viewModel: ClipForgeViewModel = viewModel()) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val subscription by viewModel.subscription.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle()
    val activeProject by viewModel.activeProject.collectAsStateWithLifecycle()
    val activeClip by viewModel.activeClip.collectAsStateWithLifecycle()
    val analysisProgress by viewModel.analysisProgress.collectAsStateWithLifecycle()
    val exportState by viewModel.exportState.collectAsStateWithLifecycle()
    val exportHistory by viewModel.exportHistory.collectAsStateWithLifecycle()
    val statusMessage by viewModel.statusMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(statusMessage) {
        statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissStatusMessage()
        }
    }

    // Android Guidelines: BackHandler for sub-screens
    BackHandler(enabled = currentScreen != AppScreen.DASHBOARD) {
        when (currentScreen) {
            AppScreen.PROCESSING -> { /* Keep processing or user can wait */ }
            AppScreen.RESULTS -> viewModel.navigateTo(AppScreen.DASHBOARD)
            AppScreen.EDITOR -> viewModel.navigateTo(AppScreen.RESULTS)
            AppScreen.EXPORT -> viewModel.navigateTo(AppScreen.EDITOR)
            AppScreen.PRO_UPGRADE -> viewModel.navigateTo(AppScreen.DASHBOARD)
            AppScreen.PROJECTS_LIST -> viewModel.navigateTo(AppScreen.DASHBOARD)
            AppScreen.EXPORT_HISTORY -> viewModel.navigateTo(AppScreen.DASHBOARD)
            AppScreen.DASHBOARD -> { }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkVoidBg),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentScreen == AppScreen.DASHBOARD) {
                AppHeader(
                    subscription = subscription,
                    onProClick = { viewModel.navigateTo(AppScreen.PRO_UPGRADE) },
                    modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                )
            }
        },
        bottomBar = {
            if (currentScreen != AppScreen.PROCESSING) {
                ClipForgeBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        viewModel.navigateTo(screen)
                    },
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        },
        containerColor = DarkVoidBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.DASHBOARD -> {
                    HomeScreen(
                        viewModel = viewModel,
                        subscription = subscription,
                        projects = projects,
                        onNavigateToProjects = { viewModel.navigateTo(AppScreen.PROJECTS_LIST) },
                        onNavigateToHistory = { viewModel.navigateTo(AppScreen.EXPORT_HISTORY) },
                        onNavigateToPro = { viewModel.navigateTo(AppScreen.PRO_UPGRADE) }
                    )
                }

                AppScreen.PROCESSING -> {
                    ProcessingScreen(
                        progress = analysisProgress
                    )
                }

                AppScreen.RESULTS -> {
                    ResultsScreen(
                        viewModel = viewModel,
                        project = activeProject,
                        subscription = subscription,
                        onBackClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        onShareClip = { clip ->
                            shareClipToSocials(context, clip)
                        }
                    )
                }

                AppScreen.EDITOR -> {
                    EditorScreen(
                        viewModel = viewModel,
                        clip = activeClip,
                        subscription = subscription,
                        onBackClick = { viewModel.navigateTo(AppScreen.RESULTS) },
                        onExportClick = {
                            viewModel.navigateTo(AppScreen.EXPORT)
                        }
                    )
                }

                AppScreen.EXPORT -> {
                    ExportScreen(
                        viewModel = viewModel,
                        clip = activeClip,
                        subscription = subscription,
                        exportState = exportState,
                        onBackClick = { viewModel.navigateTo(AppScreen.EDITOR) },
                        onUpgradeClick = { viewModel.navigateTo(AppScreen.PRO_UPGRADE) }
                    )
                }

                AppScreen.PRO_UPGRADE -> {
                    ProScreen(
                        viewModel = viewModel,
                        subscription = subscription,
                        onBackClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                    )
                }

                AppScreen.PROJECTS_LIST -> {
                    ProjectsScreen(
                        viewModel = viewModel,
                        projects = projects,
                        onBackClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                    )
                }

                AppScreen.EXPORT_HISTORY -> {
                    ExportHistoryScreen(
                        history = exportHistory,
                        onBackClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }
                    )
                }
            }
        }
    }
}

@Composable
fun ClipForgeBottomNav(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .border(1.dp, DarkCardBorder)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Default.Bolt,
            label = "Home",
            isSelected = currentScreen == AppScreen.DASHBOARD,
            onClick = { onNavigate(AppScreen.DASHBOARD) },
            testTag = "nav_home"
        )

        BottomNavItem(
            icon = Icons.Default.Movie,
            label = "Clips",
            isSelected = currentScreen == AppScreen.RESULTS,
            onClick = { onNavigate(AppScreen.RESULTS) },
            testTag = "nav_clips"
        )

        BottomNavItem(
            icon = Icons.Default.Tune,
            label = "Studio",
            isSelected = currentScreen == AppScreen.EDITOR,
            onClick = { onNavigate(AppScreen.EDITOR) },
            testTag = "nav_studio"
        )

        BottomNavItem(
            icon = Icons.Default.Folder,
            label = "Projects",
            isSelected = currentScreen == AppScreen.PROJECTS_LIST,
            onClick = { onNavigate(AppScreen.PROJECTS_LIST) },
            testTag = "nav_projects"
        )

        BottomNavItem(
            icon = Icons.Default.Diamond,
            label = "VIP Pro",
            isSelected = currentScreen == AppScreen.PRO_UPGRADE,
            onClick = { onNavigate(AppScreen.PRO_UPGRADE) },
            isHighlight = true,
            testTag = "nav_pro"
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isHighlight: Boolean = false,
    testTag: String = ""
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = when {
                isSelected -> ElectricCyan
                isHighlight -> FlameGold
                else -> TextSecondary
            },
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = when {
                isSelected -> ElectricCyan
                isHighlight -> FlameGold
                else -> TextSecondary
            }
        )
    }
}

private fun shareClipToSocials(context: android.content.Context, clip: GamingClip) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "🔥 Check out this viral ${clip.gameTitle} clip made with ClipForge AI!\n\"${clip.hookText}\"\n\n${clip.aiHashtags.joinToString(" ")}"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Clip to TikTok / Shorts / Reels")
    context.startActivity(shareIntent)
}
