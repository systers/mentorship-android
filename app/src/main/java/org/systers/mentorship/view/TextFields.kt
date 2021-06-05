package org.systers.mentorship.view

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.systers.mentorship.R

@Composable
fun EmailTextField(
    value: String,
    onUsernameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onUsernameChange,
        modifier = modifier,
        enabled = enabled,
        label = { Text(stringResource(id = R.string.username_or_email)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            autoCorrect = false,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onImeAction()
            }
        )

    )
}

@Composable
fun PasswordTextField(
    value: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    val showPassword = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = value,
        onValueChange = onPasswordChange,
        modifier = modifier,
        enabled = enabled,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            autoCorrect = false,
            imeAction = imeAction
        ),
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "Hide password"
                    )
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = "Show password"
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(onDone = { onImeAction() })
    )
}
