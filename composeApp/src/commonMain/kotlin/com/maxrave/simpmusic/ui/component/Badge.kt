package com.maxrave.simpmusic.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.maxrave.simpmusic.ui.theme.*

// ─── Aurora Badge Components ───────────────────────────────────────────────────

/** E (Explicit) badge */
@Composable
fun ExplicitBadge(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(TextTertiary.copy(alpha = 0.25f))
            .border(0.5.dp, TextTertiary.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp),
    ) {
        Text(
            text       = "E",
            fontSize   = 9.sp,
            fontWeight = FontWeight.Bold,
            color      = TextTertiary,
        )
    }
}

/** New badge with gradient fill */
@Composable
fun NewBadge(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AuroraBrush)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text       = "NEW",
            fontSize   = 9.sp,
            fontWeight = FontWeight.Bold,
            color      = Color.White,
            letterSpacing = 0.8.sp,
        )
    }
}

/** Generic label badge */
@Composable
fun LabelBadge(
    text: String,
    modifier: Modifier = Modifier,
    brush: Brush = Brush.linearGradient(listOf(Aurora1, Aurora2)),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(brush)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text       = text,
            fontSize   = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color      = Color.White,
        )
    }
}
