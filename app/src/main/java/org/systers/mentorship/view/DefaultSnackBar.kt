package org.systers.mentorship.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DefaultSnackBar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(hostState = snackbarHostState,
        snackbar = { data -> DefaultSnackBarView(snackbarData = data) },
        modifier = modifier
    )
}

@Composable
private fun DefaultSnackBarView(snackbarData: SnackbarData) {
    Snackbar(modifier = Modifier.fillMaxWidth(0.95f), snackbarData = snackbarData, actionOnNewLine = true)
}

@Preview(showBackground = true)
@Composable
private fun SnackBarPreview() {
    val data = object : SnackbarData {
        override val actionLabel: String?
            get() = null
        override val duration: SnackbarDuration
            get() = SnackbarDuration.Short
        override val message: String
            get() = "Username/Password Incorrect"

        override fun dismiss() {
            TODO("Not yet implemented")
        }

        override fun performAction() {
            TODO("Not yet implemented")
        }
    }
    DefaultSnackBarView(snackbarData = data)
}
