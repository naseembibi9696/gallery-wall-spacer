package com.gallerywall.spacer.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkDeep
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.core.theme.TextSecondary

@Composable
fun FrameChip(
    sizeLabel: String,
    dropLabel: String,
    spriteIndex: Int,
    selected: Boolean,
    stateLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(112.dp)
            .height(140.dp)
            .clickable(onClick = onClick)
            .semantics { stateDescription = stateLabel },
        shape = RoundedCornerShape(10.dp),
        color = SurfaceCard,
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) InkPrimary else SurfaceMuted)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(spriteFor(spriteIndex)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = sizeLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 13.sp,
                    color = InkDeep
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = dropLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
            if (selected) {
                Spacer(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(6.dp)
                        .background(CoralAccent, CircleShape)
                )
            }
        }
    }
}
