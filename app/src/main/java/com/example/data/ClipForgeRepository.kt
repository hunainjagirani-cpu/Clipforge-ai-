package com.example.data

import com.example.model.CaptionStyle
import com.example.model.ClipMode
import com.example.model.ClipProject
import com.example.model.CropRatio
import com.example.model.ExportHistoryItem
import com.example.model.GamingClip
import com.example.model.MusicTrack
import com.example.model.UserSubscription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ClipForgeRepository {

    // User subscription & AI credits state
    private val _subscription = MutableStateFlow(
        UserSubscription(
            isPro = false,
            aiCredits = 60,
            maxCredits = 60,
            exportsUsedThisMonth = 1,
            maxFreeExports = 3,
            planName = "Free Creator"
        )
    )
    val subscription: StateFlow<UserSubscription> = _subscription.asStateFlow()

    // Projects list
    private val _projects = MutableStateFlow<List<ClipProject>>(emptyList())
    val projects: StateFlow<List<ClipProject>> = _projects.asStateFlow()

    // Export history
    private val _exportHistory = MutableStateFlow<List<ExportHistoryItem>>(emptyList())
    val exportHistory: StateFlow<List<ExportHistoryItem>> = _exportHistory.asStateFlow()

    init {
        // Pre-populate with initial starter projects so users have instant content
        val valorantDemo = GamingPresets.createSampleProject(
            GamingPresets.supportedGames[0],
            ClipMode.BEST_GAMEPLAY
        )
        val minecraftDemo = GamingPresets.createSampleProject(
            GamingPresets.supportedGames[1],
            ClipMode.FUNNY
        )
        _projects.value = listOf(valorantDemo, minecraftDemo)

        _exportHistory.value = listOf(
            ExportHistoryItem(
                id = "exp_01",
                clipId = valorantDemo.clips.first().id,
                clipTitle = valorantDemo.clips.first().title,
                gameTitle = "Valorant",
                exportedAt = System.currentTimeMillis() - 86400000L * 2,
                resolution = "1080p 60fps",
                durationSec = 26,
                fileSizeMb = 38.4f
            )
        )
    }

    fun addProject(project: ClipProject) {
        _projects.update { current -> listOf(project) + current }
    }

    fun deleteProject(projectId: String) {
        _projects.update { current -> current.filterNot { it.id == projectId } }
    }

    fun updateClip(clip: GamingClip) {
        _projects.update { currentList ->
            currentList.map { project ->
                if (project.id == clip.projectId) {
                    val updatedClips = project.clips.map { if (it.id == clip.id) clip else it }
                    project.copy(clips = updatedClips)
                } else {
                    project
                }
            }
        }
    }

    fun toggleFavorite(clipId: String) {
        _projects.update { currentList ->
            currentList.map { project ->
                val updatedClips = project.clips.map { clip ->
                    if (clip.id == clipId) clip.copy(isFavorite = !clip.isFavorite) else clip
                }
                project.copy(clips = updatedClips)
            }
        }
    }

    fun getAllClips(): List<GamingClip> {
        return _projects.value.flatMap { it.clips }
    }

    fun getFavoriteClips(): List<GamingClip> {
        return getAllClips().filter { it.isFavorite }
    }

    fun findClipById(clipId: String): GamingClip? {
        return getAllClips().find { it.id == clipId }
    }

    fun findProjectById(projectId: String): ClipProject? {
        return _projects.value.find { it.id == projectId }
    }

    fun recordExport(clip: GamingClip, resolution: String): ExportHistoryItem {
        val item = ExportHistoryItem(
            id = "exp_${System.currentTimeMillis()}",
            clipId = clip.id,
            clipTitle = clip.title,
            gameTitle = clip.gameTitle,
            exportedAt = System.currentTimeMillis(),
            resolution = resolution,
            durationSec = clip.durationSec,
            fileSizeMb = if (resolution.contains("4K")) 92.5f else 34.2f
        )
        _exportHistory.update { current -> listOf(item) + current }
        _subscription.update { current ->
            current.copy(exportsUsedThisMonth = current.exportsUsedThisMonth + 1)
        }
        return item
    }

    fun deductCredits(amount: Int): Boolean {
        val currentCredits = _subscription.value.aiCredits
        if (currentCredits >= amount) {
            _subscription.update { it.copy(aiCredits = it.aiCredits - amount) }
            return true
        }
        return false
    }

    fun upgradeToPro() {
        _subscription.update {
            it.copy(
                isPro = true,
                aiCredits = 500,
                maxCredits = 500,
                planName = "Pro Creator VIP",
                expiryDate = "Next Month"
            )
        }
    }

    fun refillCredits(amount: Int = 100) {
        _subscription.update {
            it.copy(aiCredits = it.aiCredits + amount)
        }
    }
}
