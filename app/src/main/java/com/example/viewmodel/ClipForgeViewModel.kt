package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ClipForgeRepository
import com.example.data.GamePreset
import com.example.data.GamingPresets
import com.example.model.CaptionStyle
import com.example.model.ClipMode
import com.example.model.ClipProject
import com.example.model.CropRatio
import com.example.model.ExportHistoryItem
import com.example.model.GamingClip
import com.example.model.MusicTrack
import com.example.model.UserSubscription
import com.example.service.AiVideoAnalyzer
import com.example.service.AnalysisProgress
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppScreen {
    DASHBOARD,
    PROCESSING,
    RESULTS,
    EDITOR,
    EXPORT,
    PRO_UPGRADE,
    PROJECTS_LIST,
    EXPORT_HISTORY
}

data class ExportProgressState(
    val isExporting: Boolean = false,
    val progress: Float = 0f,
    val stage: String = "",
    val finishedItem: ExportHistoryItem? = null
)

class ClipForgeViewModel(
    private val repository: ClipForgeRepository = ClipForgeRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    val subscription: StateFlow<UserSubscription> = repository.subscription
    val projects: StateFlow<List<ClipProject>> = repository.projects
    val exportHistory: StateFlow<List<ExportHistoryItem>> = repository.exportHistory

    // Selected project & clip
    private val _activeProject = MutableStateFlow<ClipProject?>(null)
    val activeProject: StateFlow<ClipProject?> = _activeProject.asStateFlow()

    private val _activeClip = MutableStateFlow<GamingClip?>(null)
    val activeClip: StateFlow<GamingClip?> = _activeClip.asStateFlow()

    // Analysis progress
    private val _analysisProgress = MutableStateFlow<AnalysisProgress?>(null)
    val analysisProgress: StateFlow<AnalysisProgress?> = _analysisProgress.asStateFlow()

    // Export simulation state
    private val _exportState = MutableStateFlow(ExportProgressState())
    val exportState: StateFlow<ExportProgressState> = _exportState.asStateFlow()

    // Status snackbar / feedback
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    init {
        // Default active project
        val firstProject = repository.projects.value.firstOrNull()
        _activeProject.value = firstProject
        _activeClip.value = firstProject?.clips?.firstOrNull()
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectProject(project: ClipProject) {
        _activeProject.value = project
        _activeClip.value = project.clips.firstOrNull()
        _currentScreen.value = AppScreen.RESULTS
    }

    fun selectClipForEditor(clip: GamingClip) {
        _activeClip.value = clip
        _currentScreen.value = AppScreen.EDITOR
    }

    fun startDemoAnalysis(gamePreset: GamePreset, mode: ClipMode) {
        viewModelScope.launch {
            if (!repository.deductCredits(10)) {
                _statusMessage.value = "Out of AI Credits! Please upgrade or refill."
                _currentScreen.value = AppScreen.PRO_UPGRADE
                return@launch
            }

            _currentScreen.value = AppScreen.PROCESSING
            val analyzedProject = AiVideoAnalyzer.analyzeVideo(
                videoTitle = "${gamePreset.title} - ${mode.title} Session",
                gameTitle = gamePreset.title,
                mode = mode,
                videoUri = null,
                isPro = subscription.value.isPro,
                onProgress = { progress ->
                    _analysisProgress.value = progress
                }
            )

            repository.addProject(analyzedProject)
            _activeProject.value = analyzedProject
            _activeClip.value = analyzedProject.clips.firstOrNull()
            _statusMessage.value = "Generated ${analyzedProject.clips.size} viral-ready clips!"
            _currentScreen.value = AppScreen.RESULTS
        }
    }

    fun startCustomUploadAnalysis(
        videoTitle: String,
        gameTitle: String,
        mode: ClipMode,
        videoUri: String?
    ) {
        viewModelScope.launch {
            if (!repository.deductCredits(10)) {
                _statusMessage.value = "Out of AI Credits! Please upgrade to Pro."
                _currentScreen.value = AppScreen.PRO_UPGRADE
                return@launch
            }

            _currentScreen.value = AppScreen.PROCESSING
            val analyzedProject = AiVideoAnalyzer.analyzeVideo(
                videoTitle = videoTitle.ifBlank { "$gameTitle Gameplay Recording" },
                gameTitle = gameTitle,
                mode = mode,
                videoUri = videoUri,
                isPro = subscription.value.isPro,
                onProgress = { progress ->
                    _analysisProgress.value = progress
                }
            )

            repository.addProject(analyzedProject)
            _activeProject.value = analyzedProject
            _activeClip.value = analyzedProject.clips.firstOrNull()
            _statusMessage.value = "Upload analyzed! ${analyzedProject.clips.size} clips created."
            _currentScreen.value = AppScreen.RESULTS
        }
    }

    fun updateActiveClip(
        cropRatio: CropRatio? = null,
        captionStyle: CaptionStyle? = null,
        captionText: String? = null,
        musicTrack: MusicTrack? = null,
        musicVolume: Float? = null,
        zoomEffects: Boolean? = null,
        silenceRemoved: Boolean? = null,
        facecamEnabled: Boolean? = null,
        facecamTop: Boolean? = null,
        hookText: String? = null,
        startTimeSec: Int? = null,
        endTimeSec: Int? = null
    ) {
        val current = _activeClip.value ?: return
        val updated = current.copy(
            cropRatio = cropRatio ?: current.cropRatio,
            captionStyle = captionStyle ?: current.captionStyle,
            captionText = captionText ?: current.captionText,
            musicTrack = if (musicTrack != null) musicTrack else current.musicTrack,
            musicVolume = musicVolume ?: current.musicVolume,
            zoomEffects = zoomEffects ?: current.zoomEffects,
            silenceRemoved = silenceRemoved ?: current.silenceRemoved,
            facecamEnabled = facecamEnabled ?: current.facecamEnabled,
            facecamTop = facecamTop ?: current.facecamTop,
            hookText = hookText ?: current.hookText,
            startTimeSec = startTimeSec ?: current.startTimeSec,
            endTimeSec = endTimeSec ?: current.endTimeSec,
            durationSec = if (startTimeSec != null || endTimeSec != null) {
                ((endTimeSec ?: current.endTimeSec) - (startTimeSec ?: current.startTimeSec)).coerceAtLeast(5)
            } else current.durationSec
        )
        _activeClip.value = updated
        repository.updateClip(updated)

        // Also update the active project reference
        _activeProject.update { proj ->
            proj?.copy(clips = proj.clips.map { if (it.id == updated.id) updated else it })
        }
    }

    fun toggleFavorite(clipId: String) {
        repository.toggleFavorite(clipId)
        if (_activeClip.value?.id == clipId) {
            _activeClip.update { it?.copy(isFavorite = !(it.isFavorite)) }
        }
    }

    fun startExport(clip: GamingClip, resolution: String, removeWatermark: Boolean) {
        viewModelScope.launch {
            if (!subscription.value.isPro && removeWatermark) {
                _statusMessage.value = "Watermark removal requires ClipForge Pro!"
                _currentScreen.value = AppScreen.PRO_UPGRADE
                return@launch
            }

            _exportState.value = ExportProgressState(
                isExporting = true,
                progress = 0.05f,
                stage = "Initializing 9:16 vertical render engine..."
            )

            val renderStages = listOf(
                Pair("Cropping & centering focal player point...", 0.25f),
                Pair("Burning in animated ${clip.captionStyle.displayName} subtitles...", 0.50f),
                Pair("Equalizing game audio & mixing ${clip.musicTrack?.title ?: "soundtrack"}...", 0.75f),
                Pair("Encoding $resolution MP4 with H.265 hardware acceleration...", 0.95f),
                Pair("Finalizing viral clip metadata...", 1.0f)
            )

            for ((stageText, targetProgress) in renderStages) {
                delay(if (subscription.value.isPro) 200L else 350L)
                _exportState.update { it.copy(stage = stageText, progress = targetProgress) }
            }

            val historyItem = repository.recordExport(clip, resolution)
            _exportState.update {
                it.copy(
                    isExporting = false,
                    stage = "Complete! Clip saved to device.",
                    finishedItem = historyItem
                )
            }
            _statusMessage.value = "🎉 Clip exported successfully ($resolution)!"
        }
    }

    fun clearExportState() {
        _exportState.value = ExportProgressState()
    }

    fun regenerateAiCaptions(clip: GamingClip) {
        viewModelScope.launch {
            if (!repository.deductCredits(5)) {
                _statusMessage.value = "Need 5 AI credits to regenerate subtitles!"
                return@launch
            }
            _statusMessage.value = "Regenerating high-energy AI captions..."
            delay(600)
            val alternateCaptions = listOf(
                "WATCH THIS! BRO HIT A 360 NO SCOPE! NO FREAKING WAY!!",
                "HE DID NOT JUST DO THAT! CALL AN AMBULANCE! HE IS CRACKED!!",
                "2 HP CLUTCH! ONE DOWN! TWO DOWN! UNREAL ACCURACY!!"
            )
            val newCaption = alternateCaptions.random()
            updateActiveClip(captionText = newCaption)
            _statusMessage.value = "Captions updated with AI!"
        }
    }

    fun regenerateAiHook(clip: GamingClip) {
        viewModelScope.launch {
            if (!repository.deductCredits(5)) {
                _statusMessage.value = "Need 5 AI credits to regenerate viral hook!"
                return@launch
            }
            _statusMessage.value = "Generating new viral hook with AI..."
            delay(500)
            val alternateHooks = listOf(
                "This play should be illegal in ranked lobbies 💀",
                "Bro went full demon mode for 10 seconds straight ⚡",
                "Wait for the flick at the end... unbelievable 🤯",
                "They really thought they had the round won 😂"
            )
            updateActiveClip(hookText = alternateHooks.random())
            _statusMessage.value = "Viral hook regenerated!"
        }
    }

    fun upgradeToPro() {
        repository.upgradeToPro()
        _statusMessage.value = "🔥 Welcome to ClipForge Pro VIP! 500 Credits added."
        _currentScreen.value = AppScreen.DASHBOARD
    }

    fun refillCredits() {
        repository.refillCredits(100)
        _statusMessage.value = "+100 AI Credits added to your account!"
    }

    fun dismissStatusMessage() {
        _statusMessage.value = null
    }

    fun deleteProject(projectId: String) {
        repository.deleteProject(projectId)
        if (_activeProject.value?.id == projectId) {
            _activeProject.value = repository.projects.value.firstOrNull()
            _activeClip.value = _activeProject.value?.clips?.firstOrNull()
            _currentScreen.value = AppScreen.DASHBOARD
        }
        _statusMessage.value = "Project deleted."
    }
}
