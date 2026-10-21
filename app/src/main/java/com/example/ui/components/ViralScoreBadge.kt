package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkVoidBg
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FieryOrange
import com.example.ui.theme.FlameGold
import com.example.ui.theme.NeonGreen

@Composable
fun ViralScoreBadge(
    score: Int,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val (badgeColors, label, icon) = when {
        score >= 95 -> Triple(
            listOf(FlameGold, FieryOrange),
            "VIRAL GOD 🔥",
            Icons.Default.LocalFireDepartment
        )
        score >= 90 -> Triple(
            listOf(FieryOrange, Color(0xFFFF3366)),
            "SUPER VIRAL ⚡",
            Icons.Default.LocalFireDepartment
        )
        score >= 80 -> Triple(
            listOf(ElectricCyan, NeonGreen),
            "HIGH POTENTIAL ✨",
            Icons.Default.TrendingUp
        )
        else -> Triple(
            listOf(ElectricCyan, Color(0xFF818CF8)),
            "TRENDING 🚀",
            Icons.Default.WorkspacePremium
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DarkVoidBg.copy(alpha = 0.85f))
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(badgeColors),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = if (compact) 6.dp else 10.dp, vertical = if (compact) 3.dp else 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Viral Score",
                tint = badgeColors.first(),
                modifier = Modifier.size(if (compact) 12.dp else 16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$score",
                style = if (compact) {
                    MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black)
                } else {
                    MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                },
                color = Color.White
            )
            if (!compact) {
                Text(
                    text = "/100",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = badgeColors.first()
                )
            }
        }
    }
}
