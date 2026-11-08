package com.gallerywall.spacer.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.SurfaceMuted

@Composable
fun SectionHeader(
    label: String,
    modifier: Modifier = Modifier,
    showRule: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (showRule) {
            HorizontalDivider(thickness = 1.dp, color = SurfaceMuted)
            Spacer(Modifier.height(12.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = CoralAccent
        )
        Spacer(Modifier.height(10.dp))
    }
}
