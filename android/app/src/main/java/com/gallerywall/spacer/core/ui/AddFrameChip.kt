package com.gallerywall.spacer.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.theme.SurfaceMuted

@Composable
fun AddFrameChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(112.dp)
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceMuted)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = AppIcons.Add,
                contentDescription = null,
                tint = CoralAccent,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = InkPrimary
            )
        }
    }
}
