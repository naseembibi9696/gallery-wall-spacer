package com.gallerywall.spacer.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gallerywall.spacer.R
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkDeep
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.ui.StepperRow

@Composable
fun AddFrameDialog(
    widthCm: Int,
    heightCm: Int,
    dropCm: Int,
    onWidthChange: (Int) -> Unit,
    onHeightChange: (Int) -> Unit,
    onDropChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val minus = stringResource(R.string.cd_decrease)
    val plus = stringResource(R.string.cd_increase)
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(10.dp),
        containerColor = SurfaceCard,
        titleContentColor = InkDeep,
        textContentColor = InkPrimary,
        title = {
            Text(
                text = stringResource(R.string.dialog_new_frame),
                style = MaterialTheme.typography.labelLarge,
                color = InkDeep
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StepperRow(
                    label = stringResource(R.string.dialog_width),
                    value = stringResource(R.string.value_cm, widthCm.toString()),
                    decrementLabel = minus,
                    incrementLabel = plus,
                    onDecrement = { onWidthChange(-1) },
                    onIncrement = { onWidthChange(1) }
                )
                StepperRow(
                    label = stringResource(R.string.dialog_height),
                    value = stringResource(R.string.value_cm, heightCm.toString()),
                    decrementLabel = minus,
                    incrementLabel = plus,
                    onDecrement = { onHeightChange(-1) },
                    onIncrement = { onHeightChange(1) }
                )
                StepperRow(
                    label = stringResource(R.string.dialog_drop),
                    value = stringResource(R.string.value_cm, dropCm.toString()),
                    decrementLabel = minus,
                    incrementLabel = plus,
                    onDecrement = { onDropChange(-1) },
                    onIncrement = { onDropChange(1) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.add),
                    style = MaterialTheme.typography.labelLarge,
                    color = CoralAccent
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = InkPrimary
                )
            }
        }
    )
}
