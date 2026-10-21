package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ClipForgeRepository
import com.example.data.GamingPresets
import com.example.model.ClipMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ClipForge AI", appName)
    }

    @Test
    fun `test gaming preset project creation and viral clips`() {
        val preset = GamingPresets.supportedGames.first { it.id == "valorant" }
        val project = GamingPresets.createSampleProject(preset, ClipMode.BEST_GAMEPLAY)
        assertNotNull(project)
        assertTrue(project.clips.isNotEmpty())
        val firstClip = project.clips.first()
        assertTrue(firstClip.viralScore in 0..100)
        assertTrue(firstClip.durationSec > 0)
    }

    @Test
    fun `test repository credit deduction and pro upgrade`() {
        val repo = ClipForgeRepository()
        assertEquals(60, repo.subscription.value.aiCredits)
        val success = repo.deductCredits(10)
        assertTrue(success)
        assertEquals(50, repo.subscription.value.aiCredits)

        repo.upgradeToPro()
        assertTrue(repo.subscription.value.isPro)
        assertEquals(500, repo.subscription.value.aiCredits)
    }
}
