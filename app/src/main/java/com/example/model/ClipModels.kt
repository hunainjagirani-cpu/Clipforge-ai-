package com.example.model

enum class ClipMode(val title: String, val subtitle: String, val badge: String) {
    VIRAL("Viral Mode", "Punchy hooks, fast pacing, trending audio", "🔥 VIRAL"),
    FUNNY("Funny Mode", "Sound effects, comedic zooms, fail highlights", "😂 FUNNY"),
    BEST_GAMEPLAY("Best Gameplay", "Skill clutches, kill streaks, cinematic moments", "⚡ CLUTCH"),
    REACTIONS("Rage & Reactions", "Webcam focus, sound level bursts, rage cuts", "😡 REACTION"),
    FAST_PACED("Fast-Paced", "Zero downtime, beat-synced cuts, high tempo", "⏩ HYPER")
}

enum class CaptionStyle(val displayName: String, val description: String) {
    HORMOZI("Hormozi Pop", "High contrast yellow & white bold words with punch"),
    TIKTOK_BOUNCE("TikTok Bounce", "Dynamic pulsing cyan & neon green karaoke effect"),
    NEON_CYBER("Cyber Glow", "Futuristic glowing violet & electric aqua text"),
    CLEAN_SUBTITLE("Clean Minimal", "Sleek modern subtitles with semi-dark pill"),
    FIRE_ANIME("Fire Anime", "Explosive flame orange gradient with dramatic emphasis")
}

enum class CropRatio(val displayName: String, val ratioLabel: String, val aspectRatio: Float) {
    VERTICAL_9_16("9:16 Vertical", "Shorts / Reels / TikTok", 9f / 16f),
    SPLIT_SCREEN("Split Cam + Game", "Facecam Top / Game Bottom", 9f / 16f),
    SQUARE_1_1("1:1 Square", "Instagram Feed / Discord", 1f),
    LANDSCAPE_16_9("16:9 Classic", "YouTube / Twitch VoD", 16f / 9f)
}

enum class DetectedEventType(val label: String, val icon: String) {
    KILL_ELIMINATION("Elimination / Kill", "🎯"),
    FUNNY_MOMENT("Funny Moment", "😂"),
    VICTORY_WIN("Victory / Win", "🏆"),
    DEFEAT_LOSS("Defeat / Loss", "💀"),
    REACTION_RAGE("Streamer Reaction", "😱"),
    HIGH_ACTION("High Action Clutch", "🔥"),
    BORING_SECTION("Silence Pruned", "✂️")
}

data class DetectedEvent(
    val type: DetectedEventType,
    val timestampSec: Int,
    val description: String,
    val confidence: Int
)

data class MusicTrack(
    val id: String,
    val title: String,
    val genre: String,
    val bpm: Int,
    val durationText: String
)

data class GamingClip(
    val id: String,
    val projectId: String,
    val title: String,
    val gameTitle: String,
    val viralScore: Int, // 0-100
    val durationSec: Int,
    val startTimeSec: Int,
    val endTimeSec: Int,
    val category: String,
    val hookText: String,
    val aiTitle: String,
    val aiDescription: String,
    val aiHashtags: List<String>,
    val cropRatio: CropRatio = CropRatio.VERTICAL_9_16,
    val captionStyle: CaptionStyle = CaptionStyle.HORMOZI,
    val captionText: String = "",
    val musicTrack: MusicTrack? = null,
    val musicVolume: Float = 0.65f,
    val zoomEffects: Boolean = true,
    val silenceRemoved: Boolean = true,
    val facecamEnabled: Boolean = true,
    val facecamTop: Boolean = true,
    val watermark: Boolean = true,
    val isFavorite: Boolean = false,
    val detectedEvents: List<DetectedEvent> = emptyList()
)

data class ClipProject(
    val id: String,
    val title: String,
    val gameTitle: String,
    val originalDurationSec: Int,
    val fileSizeMb: Float,
    val createdAt: Long,
    val status: ProjectStatus = ProjectStatus.READY,
    val selectedMode: ClipMode = ClipMode.VIRAL,
    val clips: List<GamingClip> = emptyList()
)

enum class ProjectStatus {
    ANALYZING,
    READY,
    FAILED
}

data class ExportHistoryItem(
    val id: String,
    val clipId: String,
    val clipTitle: String,
    val gameTitle: String,
    val exportedAt: Long,
    val resolution: String,
    val durationSec: Int,
    val fileSizeMb: Float,
    val platform: String = "YouTube Shorts / TikTok"
)

data class UserSubscription(
    val isPro: Boolean = false,
    val aiCredits: Int = 60,
    val maxCredits: Int = 60,
    val exportsUsedThisMonth: Int = 1,
    val maxFreeExports: Int = 3,
    val planName: String = "Free Creator",
    val expiryDate: String? = null
)
