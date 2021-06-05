package org.systers.mentorship.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.systers.mentorship.R
import org.systers.mentorship.utils.Constants
import org.systers.mentorship.view.CircularIndeterminateProgressBar
import org.systers.mentorship.view.DefaultSnackBar
import org.systers.mentorship.view.EmailTextField
import org.systers.mentorship.view.PasswordTextField
import org.systers.mentorship.viewmodels.LoginViewModel

/**
 * This activity will let the user to login using username/email and password.
 */
class LoginActivity : BaseActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen(viewModel = loginViewModel,
                onSignUpButtonClick = {
                    intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                })
        }

        loginViewModel.successful.observe(this) { successful ->
            hideProgressDialog()
            if (successful != null) {
                if (successful) {
                    Toast.makeText(this, R.string.logging_successful, Toast.LENGTH_LONG)
                        .show()
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        try {
            val tokenExpiredVal = intent.extras!!.getInt(Constants.TOKEN_EXPIRED_EXTRA)
            if (tokenExpiredVal == 0)
                loginViewModel.message.value = "Session Token expired, please login again"
        } catch (exception: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginViewModel.successful.removeObservers(this)
    }
}

// Composable for the View
@Composable
fun LoginScreen(viewModel: LoginViewModel, onSignUpButtonClick: () -> Unit) {

    val username by viewModel.username.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val buttonEnabled by viewModel.buttonEnabled.observeAsState(false)
    val onUsernameChange: (String) -> Unit = { viewModel.onUsernameChange(it) }
    val onPasswordChange: (String) -> Unit = { viewModel.onPasswordChange(it) }
    val onLoginButtonClick: () -> Unit = { viewModel.onButtonClick() }
    val scaffoldState = rememberScaffoldState()
    val message by viewModel.message.observeAsState()
    Scaffold(scaffoldState = scaffoldState, snackbarHost = { scaffoldState.snackbarHostState }) {
        LoginContent(
            username = username,
            password = password,
            onUsernameChange = onUsernameChange,
            onPasswordChange = onPasswordChange,
            isButtonEnabled = buttonEnabled,
            onLoginButtonClick = onLoginButtonClick,
            onSignUpButtonClick = onSignUpButtonClick,
            scaffoldState = scaffoldState,
            snackbarMessage = message
        )
    }
}

@Composable
fun LoginContent(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isButtonEnabled: Boolean,
    onLoginButtonClick: () -> Unit,
    onSignUpButtonClick: () -> Unit,
    scaffoldState: ScaffoldState,
    snackbarMessage: String?
) {
    // Can be added as params to the function for making testing easier
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val signUpFocusRequest = remember { FocusRequester() }
    val passwordFocusRequest = remember { FocusRequester() }

    Box {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // This LaunchedEffect will display a snackbar each time the snackbar message changes
            LaunchedEffect(key1 = snackbarMessage) {
                scope.launch {
                    snackbarMessage?.let {
                        if (snackbarMessage.isNotEmpty()) {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = it,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }

            // The mentorship system logo
            Image(
                painter = painterResource(id = R.drawable.mentorship_system_logo),
                contentDescription = "Mentorship System Logo",
                modifier = Modifier.fillMaxWidth(0.95f)
            )

            Column {

                EmailTextField(
                    value = username,
                    onUsernameChange = onUsernameChange,
                    enabled = username.isEmpty() || password.isEmpty() || isButtonEnabled,
                    onImeAction = { passwordFocusRequest.requestFocus() },
                    modifier = Modifier.focusRequester(signUpFocusRequest)
                )

                PasswordTextField(
                    value = password,
                    modifier = Modifier
                        .focusRequester(passwordFocusRequest),
                    onPasswordChange = onPasswordChange,
                    enabled = username.isEmpty() || password.isEmpty() || isButtonEnabled,
                    onImeAction = { onLoginButtonClick(); focusManager.clearFocus() }
                )
            }
            Column {
                Button(
                    onClick = onLoginButtonClick,
                    enabled = isButtonEnabled,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(text = stringResource(R.string.login))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onSignUpButtonClick,
                    enabled = !(!isButtonEnabled && username.isNotEmpty() && password.isNotEmpty()),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(text = stringResource(R.string.sign_up))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        CircularIndeterminateProgressBar(
            modifier = Modifier.align(Alignment.Center),
            isVisible = !isButtonEnabled && username.isNotEmpty() && password.isNotEmpty()
        )
        DefaultSnackBar(
            snackbarHostState = scaffoldState.snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val scaffoldState = rememberScaffoldState()
    LoginContent(
        "username",
        "password",
        {},
        {},
        true,
        {},
        {},
        scaffoldState,
        "username or password is incorrect"
    )
}
