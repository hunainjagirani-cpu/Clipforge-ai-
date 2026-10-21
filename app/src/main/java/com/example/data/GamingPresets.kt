package com.example.data

import com.example.model.CaptionStyle
import com.example.model.ClipMode
import com.example.model.ClipProject
import com.example.model.CropRatio
import com.example.model.DetectedEvent
import com.example.model.DetectedEventType
import com.example.model.GamingClip
import com.example.model.MusicTrack

data class GamePreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val tag: String,
    val iconEmoji: String,
    val sampleDurationMinutes: Int,
    val sampleFileSizeMb: Float,
    val defaultHighlights: List<String>,
    val defaultMode: ClipMode
)

object GamingPresets {
    val supportedGames = listOf(
        GamePreset(
            id = "valorant",
            title = "Valorant",
            subtitle = "Tactical Shooter 5v5",
            tag = "Riot Games",
            iconEmoji = "🎯",
            sampleDurationMinutes = 34,
            sampleFileSizeMb = 1420f,
            defaultHighlights = listOf("1v5 Clutch Ace", "Operator Headshot Flick", "Defuse Denial 0.02s", "Rage in Voice Chat"),
            defaultMode = ClipMode.BEST_GAMEPLAY
        ),
        GamePreset(
            id = "minecraft",
            title = "Minecraft",
            subtitle = "Survival / Bedwars",
            tag = "Mojang",
            iconEmoji = "⛏️",
            sampleDurationMinutes = 48,
            sampleFileSizeMb = 1850f,
            defaultHighlights = listOf("MLG Water Bucket Save", "Warden Jump Scare", "Bedwars 360 Bridge Trap", "Hardcore 100-Day Near Death"),
            defaultMode = ClipMode.FUNNY
        ),
        GamePreset(
            id = "gta",
            title = "GTA V / Online",
            subtitle = "Action / Heists / RP",
            tag = "Rockstar",
            iconEmoji = "🏎️",
            sampleDurationMinutes = 52,
            sampleFileSizeMb = 2100f,
            defaultHighlights = listOf("5-Star Police Bike Escape", "Stunt Plane Under Bridge", "Casino Heist Fail", "Hilarious NPC Ragdoll"),
            defaultMode = ClipMode.VIRAL
        ),
        GamePreset(
            id = "fortnite",
            title = "Fortnite",
            subtitle = "Battle Royale",
            tag = "Epic Games",
            iconEmoji = "⚡",
            sampleDurationMinutes = 26,
            sampleFileSizeMb = 1120f,
            defaultHighlights = listOf("Shockwave Snipe in Mid-Air", "20-Bomb Victory Royale", "Boxed Like a Fish Clapped", "Emote Over Boss Defeat"),
            defaultMode = ClipMode.FAST_PACED
        ),
        GamePreset(
            id = "bgmi_pubg",
            title = "PUBG / BGMI",
            subtitle = "Battlegrounds Mobile",
            tag = "Krafton",
            iconEmoji = "🪂",
            sampleDurationMinutes = 31,
            sampleFileSizeMb = 1380f,
            defaultHighlights = listOf("Airdrop Ambush Squad Wipe", "AWM Headshot at 400m", "Bridge Camp Buggy Crash", "Chicken Dinner 1v4"),
            defaultMode = ClipMode.BEST_GAMEPLAY
        ),
        GamePreset(
            id = "free_fire",
            title = "Free Fire",
            subtitle = "Fast Survival Royale",
            tag = "Garena",
            iconEmoji = "🔥",
            sampleDurationMinutes = 22,
            sampleFileSizeMb = 890f,
            defaultHighlights = listOf("One-Tap Desert Eagle Kill", "Gloo Wall 360 Defense", "Squad Clutch Booyah", "Funny Vehicle Flip"),
            defaultMode = ClipMode.FAST_PACED
        ),
        GamePreset(
            id = "cod",
            title = "Call of Duty",
            subtitle = "Warzone / Modern Warfare",
            tag = "Activision",
            iconEmoji = "💥",
            sampleDurationMinutes = 40,
            sampleFileSizeMb = 1950f,
            defaultHighlights = listOf("Gulag Knife 360", "Loadout Drop Ambush", "Nuke Streak Achieved", "Hot Mic Death Comm"),
            defaultMode = ClipMode.REACTIONS
        ),
        GamePreset(
            id = "roblox",
            title = "Roblox",
            subtitle = "Doors / Blox Fruits / Obby",
            tag = "Roblox Corp",
            iconEmoji = "🧱",
            sampleDurationMinutes = 25,
            sampleFileSizeMb = 940f,
            defaultHighlights = listOf("Impossible Obby Speedrun", "Entity Screamer Jump", "PvP Combo Destruction", "Trolling Random Players"),
            defaultMode = ClipMode.FUNNY
        )
    )

    val royaltyFreeTracks = listOf(
        MusicTrack("track_phonk", "Drift Phonk Overdrive", "Gaming Phonk", 142, "0:30"),
        MusicTrack("track_cyber", "Cyberpunk Neon Rush", "Synthwave", 128, "0:35"),
        MusicTrack("track_trap", "Sub-Zero Bass Drop", "Hype Trap", 136, "0:28"),
        MusicTrack("track_lofi", "Midnight Pixel Rest", "Lofi Chill", 85, "0:45"),
        MusicTrack("track_orchestral", "Epic Final Boss Chorus", "Cinematic", 150, "0:32")
    )

    fun createSampleProject(gamePreset: GamePreset, mode: ClipMode): ClipProject {
        val projectId = "demo_${gamePreset.id}_${System.currentTimeMillis()}"
        val clips = generateSampleClips(projectId, gamePreset, mode)
        return ClipProject(
            id = projectId,
            title = "${gamePreset.title} - Highlight Reel",
            gameTitle = gamePreset.title,
            originalDurationSec = gamePreset.sampleDurationMinutes * 60,
            fileSizeMb = gamePreset.sampleFileSizeMb,
            createdAt = System.currentTimeMillis(),
            selectedMode = mode,
            clips = clips
        )
    }

    private fun generateSampleClips(
        projectId: String,
        preset: GamePreset,
        mode: ClipMode
    ): List<GamingClip> {
        val clips = mutableListOf<GamingClip>()
        when (preset.id) {
            "valorant" -> {
                clips.add(
                    GamingClip(
                        id = "${projectId}_1",
                        projectId = projectId,
                        title = "1v5 Radiant Site Retake Ace!",
                        gameTitle = "Valorant",
                        viralScore = 98,
                        durationSec = 26,
                        startTimeSec = 412,
                        endTimeSec = 438,
                        category = "Insane Clutch",
                        hookText = "Bro really pulled off the 1v5 with 2 HP 💀",
                        aiTitle = "HE ACTUALLY 1V5 ACED ON RETAKE?! #shorts",
                        aiDescription = "Impossible clutch on C site with Vandal headshots only. Ranked lobby went completely silent.",
                        aiHashtags = listOf("#valorant", "#valorantclips", "#clutch", "#vandal", "#fyp", "#gaming"),
                        cropRatio = CropRatio.VERTICAL_9_16,
                        captionStyle = CaptionStyle.HORMOZI,
                        captionText = "TWO HP LEFT! WATCH THIS! ONE DOWN! TWO DOWN! FLICK! ACE!!",
                        musicTrack = royaltyFreeTracks[0],
                        zoomEffects = true,
                        silenceRemoved = true,
                        isFavorite = true,
                        detectedEvents = listOf(
                            DetectedEvent(DetectedEventType.KILL_ELIMINATION, 415, "Headshot Kill (Vandal)", 99),
                            DetectedEvent(DetectedEventType.KILL_ELIMINATION, 418, "Flick Headshot on Jett", 98),
                            DetectedEvent(DetectedEventType.HIGH_ACTION, 424, "Crossfire Evaded with 2 HP", 95),
                            DetectedEvent(DetectedEventType.KILL_ELIMINATION, 431, "Double Collateral Ace", 100),
                            DetectedEvent(DetectedEventType.REACTION_RAGE, 436, "Team Voice Chat Explosion", 97)
                        )
                    )
                )
                clips.add(
                    GamingClip(
                        id = "${projectId}_2",
                        projectId = projectId,
                        title = "Operator Wallbang Through Smoke",
                        gameTitle = "Valorant",
                        viralScore = 91,
                        durationSec = 19,
                        startTimeSec = 820,
                        endTimeSec = 839,
                        category = "Skill Highlight",
                        hookText = "He reported me for this shot immediately 😭",
                        aiTitle = "The Dirtiest Operator Shot Ever Recorded #gaming",
                        aiDescription = "Blind sound queue shot through B main smoke. 100% luck or 200 IQ prediction?",
                        aiHashtags = listOf("#valorant", "#gaminghighlights", "#operator", "#valorantmoments"),
                        cropRatio = CropRatio.SPLIT_SCREEN,
                        captionStyle = CaptionStyle.TIKTOK_BOUNCE,
                        captionText = "HEAR THE FOOTSTEP... PREDICT... BOOM! THEY'RE REPORTING ME!!",
                        musicTrack = royaltyFreeTracks[1],
                        zoomEffects = true,
                        silenceRemoved = true,
                        detectedEvents = listOf(
                            DetectedEvent(DetectedEventType.KILL_ELIMINATION, 828, "No-Scope Wallbang", 96),
                            DetectedEvent(DetectedEventType.REACTION_RAGE, 834, "Streamer Shock Face", 94)
                        )
                    )
                )
                clips.add(
                    GamingClip(
                        id = "${projectId}_3",
                        projectId = projectId,
                        title = "When Your Teammate Flashbangs You...",
                        gameTitle = "Valorant",
                        viralScore = 87,
                        durationSec = 22,
                        startTimeSec = 1150,
                        endTimeSec = 1172,
                        category = "Funny Fail",
                        hookText = "Why do random teammates always do this?! 💀",
                        aiTitle = "Top 10 Teammate Betrayals of All Time #shorts",
                        aiDescription = "Phoenix flashes his own team before rushing. Instant disaster ensues.",
                        aiHashtags = listOf("#funnygaming", "#gamerfail", "#valorantfunny", "#tiktokgaming"),
                        cropRatio = CropRatio.VERTICAL_9_16,
                        captionStyle = CaptionStyle.NEON_CYBER,
                        captionText = "DON'T FLASH ME! BRO! I'M BLIND! WE ARE ALL DEAD!",
                        musicTrack = royaltyFreeTracks[2],
                        zoomEffects = true,
                        silenceRemoved = true,
                        detectedEvents = listOf(
                            DetectedEvent(DetectedEventType.FUNNY_MOMENT, 1155, "Friendly Flashbang", 98),
                            DetectedEvent(DetectedEventType.DEFEAT_LOSS, 1162, "Full Team Wipeout", 91)
                        )
                    )
                )
            }
            "minecraft" -> {
                clips.add(
                    GamingClip(
                        id = "${projectId}_1",
                        projectId = projectId,
                        title = "Hardcore 100 Days - Warden Jumpscare Clutch",
                        gameTitle = "Minecraft",
                        viralScore = 96,
                        durationSec = 31,
                        startTimeSec = 340,
                        endTimeSec = 371,
                        category = "Insane Clutch",
                        hookText = "My heart stopped for 3 seconds straight 😱",
                        aiTitle = "ALMOST LOST 100 DAYS HARDCORE TO THIS! 💀",
                        aiDescription = "Deep Dark ancient city exploration went horribly wrong when the Warden summoned right underneath me.",
                        aiHashtags = listOf("#minecraft", "#minecrafthardcore", "#warden", "#clutch", "#fyp"),
                        cropRatio = CropRatio.VERTICAL_9_16,
                        captionStyle = CaptionStyle.FIRE_ANIME,
                        captionText = "HEAR THE SHRIEKER! SHIFT! RUN! ELYTRA BOOST! WE SURVIVED!!",
                        musicTrack = royaltyFreeTracks[0],
                        zoomEffects = true,
                        silenceRemoved = true,
                        isFavorite = true,
                        detectedEvents = listOf(
                            DetectedEvent(DetectedEventType.REACTION_RAGE, 345, "Heartbeat Sound Detected", 98),
                            DetectedEvent(DetectedEventType.HIGH_ACTION, 354, "Water Bucket Clutched on 1 Heart", 99),
                            DetectedEvent(DetectedEventType.VICTORY_WIN, 368, "Escaped Deep Dark", 94)
                        )
                    )
                )
                clips.add(
                    GamingClip(
                        id = "${projectId}_2",
                        projectId = projectId,
                        title = "Bedwars 360 Fireball Jump & Win",
                        gameTitle = "Minecraft",
                        viralScore = 93,
                        durationSec = 24,
                        startTimeSec = 780,
                        endTimeSec = 804,
                        category = "Skill Highlight",
                        hookText = "They never expected the 360 fireball jump 🚀",
                        aiTitle = "CLEANEST Bedwars Fireball Clutch You'll Ever See",
                        aiDescription = "Final bed destruction using fireball jump momentum straight into their base.",
                        aiHashtags = listOf("#bedwars", "#minecraftclips", "#pvp", "#gaming"),
                        cropRatio = CropRatio.SPLIT_SCREEN,
                        captionStyle = CaptionStyle.HORMOZI,
                        captionText = "WATCH THE TIMING! FIREBALL JUMP! BED DESTROYED! GG!",
                        musicTrack = royaltyFreeTracks[2],
                        zoomEffects = true,
                        silenceRemoved = true,
                        detectedEvents = listOf(
                            DetectedEvent(DetectedEventType.HIGH_ACTION, 788, "Fireball Velocity Boost", 97),
                            DetectedEvent(DetectedEventType.VICTORY_WIN, 799, "Final Bed Destroyed", 100)
                        )
                    )
                )
            }
            else -> {
                // Default high action set for other games (GTA, Fortnite, etc.)
                clips.add(
                    GamingClip(
                        id = "${projectId}_1",
                        projectId = projectId,
                        title = "${preset.title} - The Ultimate Viral Play",
                        gameTitle = preset.title,
                        viralScore = 95,
                        durationSec = 28,
                        startTimeSec = 120,
                        endTimeSec = 148,
                        category = "High Action Clutch",
                        hookText = "You won't believe what happens in the last 5 seconds 🤯",
                        aiTitle = "INSANE ${preset.title.uppercase()} MOMENT! #gaming",
                        aiDescription = "Peak gameplay captured by ClipForge AI highlight detection engine.",
                        aiHashtags = listOf("#${preset.id}", "#gaming", "#viral", "#clips", "#fyp"),
                        cropRatio = CropRatio.VERTICAL_9_16,
                        captionStyle = CaptionStyle.HORMOZI,
                        captionText = "WAIT FOR THE DROP! INSANE ACCURACY! DOUBLE KILL! LET'S GO!!",
                        musicTrack = royaltyFreeTracks[0],
                        zoomEffects = true,
                        silenceRemoved = true,
                        isFavorite = true,
                        detectedEvents = listOf(
                            DetectedEvent(DetectedEventType.KILL_ELIMINATION, 125, "First Strike Elim", 95),
                            DetectedEvent(DetectedEventType.HIGH_ACTION, 134, "Momentum Climax", 98),
                            DetectedEvent(DetectedEventType.VICTORY_WIN, 145, "Flawless Round Win", 99)
                        )
                    )
                )
                clips.add(
                    GamingClip(
                        id = "${projectId}_2",
                        projectId = projectId,
                        title = "${preset.title} - Funniest Fail of the Week",
                        gameTitle = preset.title,
                        viralScore = 89,
                        durationSec = 21,
                        startTimeSec = 310,
                        endTimeSec = 331,
                        category = "Funny Fail",
                        hookText = "How did this even happen?! 💀💀",
                        aiTitle = "When Physics Stop Working in ${preset.title} 😂",
                        aiDescription = "Unbelievable game bug and funny moment saved by ClipForge AI.",
                        aiHashtags = listOf("#${preset.id}", "#funny", "#fail", "#gamingmoments"),
                        cropRatio = CropRatio.SPLIT_SCREEN,
                        captionStyle = CaptionStyle.TIKTOK_BOUNCE,
                        captionText = "LOOK AT THIS GUY! NO WAY! HAHAHA! HE FELL OFF!",
                        musicTrack = royaltyFreeTracks[1],
                        zoomEffects = true,
                        silenceRemoved = true,
                        detectedEvents = listOf(
                            DetectedEvent(DetectedEventType.FUNNY_MOMENT, 316, "Physics Glitch Ragdoll", 97),
                            DetectedEvent(DetectedEventType.REACTION_RAGE, 324, "Laughter Burst Peak", 93)
                        )
                    )
                )
                clips.add(
                    GamingClip(
                        id = "${projectId}_3",
                        projectId = projectId,
                        title = "${preset.title} - Fast-Paced Kill Montage",
                        gameTitle = preset.title,
                        viralScore = 92,
                        durationSec = 24,
                        startTimeSec = 600,
                        endTimeSec = 624,
                        category = "Speed Run",
                        hookText = "Turn up your volume for this beat drop 🎧",
                        aiTitle = "Beat-Synced Elimination Sequence in ${preset.title}",
                        aiDescription = "Every sniper shot perfectly matches the bassline. Pure satisfaction.",
                        aiHashtags = listOf("#${preset.id}", "#montage", "#beatsync", "#satisfying"),
                        cropRatio = CropRatio.VERTICAL_9_16,
                        captionStyle = CaptionStyle.FIRE_ANIME,
                        captionText = "TARGET ACQUIRED! BAM! NEXT TARGET! BAM! WIPE OUT!",
                        musicTrack = royaltyFreeTracks[2],
                        zoomEffects = true,
                        silenceRemoved = true,
                        detectedEvents = listOf(
                            DetectedEvent(DetectedEventType.KILL_ELIMINATION, 606, "Sniper Elim", 99),
                            DetectedEvent(DetectedEventType.KILL_ELIMINATION, 614, "Quickscope Double", 98),
                            DetectedEvent(DetectedEventType.VICTORY_WIN, 622, "Match Point", 99)
                        )
                    )
                )
            }
        }
        return clips
    }
}
