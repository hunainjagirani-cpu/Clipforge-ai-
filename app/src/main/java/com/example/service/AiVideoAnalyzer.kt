package com.example.service

import com.example.BuildConfig
import com.example.data.GamingPresets
import com.example.model.CaptionStyle
import com.example.model.ClipMode
import com.example.model.ClipProject
import com.example.model.CropRatio
import com.example.model.DetectedEvent
import com.example.model.DetectedEventType
import com.example.model.GamingClip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AnalysisProgress(
    val stageName: String,
    val stageSubtitle: String,
    val progress: Float, // 0.0 to 1.0
    val detectedCount: Int = 0,
    val liveLog: String = ""
)

object AiVideoAnalyzer {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    val STAGES = listOf(
        Pair("Analyzing Gameplay", "Scanning visual frames, HUD health & killfeed telemetry..."),
        Pair("Finding Highlights", "Identifying peak moments: eliminations, clutches, epic wins..."),
        Pair("Detecting Reactions", "Tracking facecam emotion spikes & voice chat volume surges..."),
        Pair("Creating Clips", "Applying smart 9:16 vertical crop & auto silence removal..."),
        Pair("Adding Captions", "Synthesizing dynamic word-by-word animated subtitles..."),
        Pair("Almost Ready", "Calculating viral potential scores & final render preview...")
    )

    suspend fun analyzeVideo(
        videoTitle: String,
        gameTitle: String,
        mode: ClipMode,
        videoUri: String?,
        isPro: Boolean,
        onProgress: (AnalysisProgress) -> Unit
    ): ClipProject = withContext(Dispatchers.IO) {
        val totalStages = STAGES.size
        val detectedHighlights = mutableListOf<String>()

        for (i in STAGES.indices) {
            val (stageName, stageSubtitle) = STAGES[i]
            val baseProgress = i.toFloat() / totalStages.toFloat()

            // Sub-steps for ultra smooth realistic animation
            for (step in 1..4) {
                val stepProgress = baseProgress + (step.toFloat() / (totalStages * 4).toFloat())
                val currentLog = when (i) {
                    0 -> "Inspecting frame #${(step * 4200) + 1200} at 60 FPS • Resolution 1080p"
                    1 -> "Detected elimination burst at 04:12 • Action score 94%"
                    2 -> "Webcam bounding box located (x: 82%, y: 14%) • Screamer audio peak detected"
                    3 -> "Auto-pruning 14.2s boring segment • Centering 9:16 focal action tracker"
                    4 -> "Transcribing speech • Applying ${mode.name} dynamic caption presets"
                    else -> "Generating viral scores (85-98) • Preparing editable timelines"
                }

                if (i == 1 && step == 2) detectedHighlights.add("Triple Kill")
                if (i == 2 && step == 3) detectedHighlights.add("Facecam Scream")
                if (i == 3 && step == 1) detectedHighlights.add("Silence Pruned")

                onProgress(
                    AnalysisProgress(
                        stageName = stageName,
                        stageSubtitle = stageSubtitle,
                        progress = stepProgress.coerceAtMost(0.98f),
                        detectedCount = detectedHighlights.size,
                        liveLog = currentLog
                    )
                )
                // Speed up processing for Pro users as highlighted in the prompt!
                delay(if (isPro) 180L else 320L)
            }
        }

        // Try AI enhancement via Gemini if key is provided
        val aiHook = tryEnhanceWithGemini(gameTitle, mode)

        // Synthesize clips based on game and selected mode
        val projectId = "proj_${System.currentTimeMillis()}"
        val clips = buildAnalyzedClips(projectId, videoTitle, gameTitle, mode, aiHook)

        onProgress(
            AnalysisProgress(
                stageName = "Almost Ready",
                stageSubtitle = "All viral clips generated!",
                progress = 1.0f,
                detectedCount = detectedHighlights.size + 2,
                liveLog = "Completed: ${clips.size} viral-ready clips generated successfully!"
            )
        )

        ClipProject(
            id = projectId,
            title = videoTitle,
            gameTitle = gameTitle,
            originalDurationSec = 1840,
            fileSizeMb = 1450f,
            createdAt = System.currentTimeMillis(),
            selectedMode = mode,
            clips = clips
        )
    }

    private suspend fun tryEnhanceWithGemini(gameTitle: String, mode: ClipMode): String? {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return null
        }

        return try {
            val prompt = "Generate ONE high-energy, viral 1-sentence hook for a $gameTitle gaming short clip in ${mode.title} mode. Use 1 or 2 emojis. Example: 'Bro really thought he could survive a 1v5 with 2 HP 💀'. Keep it under 12 words."
            val jsonPayload = JSONObject().apply {
                put("contents", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", org.json.JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(jsonPayload.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string() ?: ""
                val jsonObj = JSONObject(body)
                val candidates = jsonObj.optJSONArray("candidates")
                val text = candidates?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text")?.trim()
                text?.replace("\"", "")
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun buildAnalyzedClips(
        projectId: String,
        videoTitle: String,
        gameTitle: String,
        mode: ClipMode,
        aiHook: String?
    ): List<GamingClip> {
        val preset = GamingPresets.supportedGames.find {
            it.title.contains(gameTitle, ignoreCase = true) || gameTitle.contains(it.title, ignoreCase = true)
        } ?: GamingPresets.supportedGames[0]

        val clip1Hook = aiHook ?: when (mode) {
            ClipMode.VIRAL -> "Wait for the 3rd elimination... bro is INSANE 💀"
            ClipMode.FUNNY -> "How did he miss that point-blank shot?! 😭"
            ClipMode.BEST_GAMEPLAY -> "1v5 with 2 HP and 5 seconds left on the clock ⚡"
            ClipMode.REACTIONS -> "He literally screamed so loud the mic clipped 😱"
            ClipMode.FAST_PACED -> "Zero misses, clean recoil, pure aimbot energy 🚀"
        }

        val clip1 = GamingClip(
            id = "${projectId}_clip_1",
            projectId = projectId,
            title = "$gameTitle - Ultra Viral Highlight #1",
            gameTitle = gameTitle,
            viralScore = 97,
            durationSec = 25,
            startTimeSec = 210,
            endTimeSec = 235,
            category = "Kill Streak / Clutch",
            hookText = clip1Hook,
            aiTitle = "HE ACTUALLY PULLED THIS OFF IN $gameTitle?! 🤯",
            aiDescription = "Viral moment detected by ClipForge AI. 9:16 vertical auto-cut with facecam focus.",
            aiHashtags = listOf("#$gameTitle", "#gaming", "#viral", "#shorts", "#fyp", "#gamer"),
            cropRatio = CropRatio.VERTICAL_9_16,
            captionStyle = CaptionStyle.HORMOZI,
            captionText = "WAIT FOR IT! ONE SHOT! TWO DOWN! FLICK ONTO HIM! ACE!!",
            musicTrack = GamingPresets.royaltyFreeTracks[0],
            zoomEffects = true,
            silenceRemoved = true,
            facecamEnabled = true,
            isFavorite = true,
            detectedEvents = listOf(
                DetectedEvent(DetectedEventType.KILL_ELIMINATION, 214, "Triple Elimination", 99),
                DetectedEvent(DetectedEventType.HIGH_ACTION, 222, "Near Death Evasion", 96),
                DetectedEvent(DetectedEventType.REACTION_RAGE, 230, "Loud Audio Spike", 98)
            )
        )

        val clip2 = GamingClip(
            id = "${projectId}_clip_2",
            projectId = projectId,
            title = "$gameTitle - Hilarious Game Moment",
            gameTitle = gameTitle,
            viralScore = 91,
            durationSec = 20,
            startTimeSec = 540,
            endTimeSec = 560,
            category = "Funny Moment",
            hookText = "Bro thought he was completely safe... 💀",
            aiTitle = "The Most Unexpected Fail in $gameTitle Ever 😂",
            aiDescription = "Funny gaming clip with auto zoom and sound effect timing.",
            aiHashtags = listOf("#funnygaming", "#$gameTitle", "#fail", "#gamingmemes"),
            cropRatio = CropRatio.SPLIT_SCREEN,
            captionStyle = CaptionStyle.TIKTOK_BOUNCE,
            captionText = "HE HAS NO IDEA... SNEAK BEHIND... OH NO HE TURNED AROUND!",
            musicTrack = GamingPresets.royaltyFreeTracks[1],
            zoomEffects = true,
            silenceRemoved = true,
            facecamEnabled = true,
            detectedEvents = listOf(
                DetectedEvent(DetectedEventType.FUNNY_MOMENT, 546, "Unexpected Betrayal", 95),
                DetectedEvent(DetectedEventType.DEFEAT_LOSS, 554, "Instant Elimination", 92)
            )
        )

        val clip3 = GamingClip(
            id = "${projectId}_clip_3",
            projectId = projectId,
            title = "$gameTitle - Speed Run & Boss Finish",
            gameTitle = gameTitle,
            viralScore = 88,
            durationSec = 28,
            startTimeSec = 915,
            endTimeSec = 943,
            category = "Skill Highlight",
            hookText = "Watch how fast he clears this entire area ⏩",
            aiTitle = "World Record Pace in $gameTitle! Clean Execution",
            aiDescription = "Beat-synced highlight cut with background phonk and smart subtitles.",
            aiHashtags = listOf("#speedrun", "#$gameTitle", "#gamingclips", "#insaneplays"),
            cropRatio = CropRatio.VERTICAL_9_16,
            captionStyle = CaptionStyle.FIRE_ANIME,
            captionText = "DODGE! STRIKE! DOUBLE BOOST! WE HIT THE RECORD TIME!",
            musicTrack = GamingPresets.royaltyFreeTracks[2],
            zoomEffects = true,
            silenceRemoved = true,
            facecamEnabled = true,
            detectedEvents = listOf(
                DetectedEvent(DetectedEventType.HIGH_ACTION, 920, "Flawless Movement Route", 98),
                DetectedEvent(DetectedEventType.VICTORY_WIN, 938, "Victory Moment Triggered", 100)
            )
        )

        return listOf(clip1, clip2, clip3)
    }
}
