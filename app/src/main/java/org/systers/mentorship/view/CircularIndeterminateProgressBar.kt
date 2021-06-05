package org.systers.mentorship.view

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CircularIndeterminateProgressBar(modifier: Modifier = Modifier, isVisible: Boolean) {
    if (isVisible) {
    CircularProgressIndicator(modifier = modifier)
}
}

@Preview(showBackground = true)
@Composable
fun CircularIndeterminateProgressBarPreview() {
    CircularIndeterminateProgressBar(isVisible = true)
}
