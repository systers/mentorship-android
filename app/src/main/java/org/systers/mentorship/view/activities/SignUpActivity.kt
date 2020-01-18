package org.systers.mentorship.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.btnLogin
import kotlinx.android.synthetic.main.activity_sign_up.btnSignUp
import kotlinx.android.synthetic.main.activity_sign_up.tiPassword
import kotlinx.android.synthetic.main.activity_sign_up.tiUsername
import org.systers.mentorship.R
import org.systers.mentorship.remote.requests.Register
import org.systers.mentorship.utils.Constants
import org.systers.mentorship.viewmodels.SignUpViewModel
import org.systers.mentorship.viewmodels.SocialLoginViewModel

/**
 * This activity will let the user to sign up into the system using name, username,
 * email and password.
 */
class SignUpActivity : BaseActivity() {

    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var socialLoginViewModel: SocialLoginViewModel

    private lateinit var name: String
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmedPassword: String
    private var isAvailableToMentor: Boolean = false
    private var needsMentoring: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        signUpViewModel.successful.observe(this, Observer { successful ->
            hideProgressDialog()
            if (successful != null) {
                if (successful) {
                    Toast.makeText(this, signUpViewModel.message, Toast.LENGTH_LONG).show()
                    navigateToLoginActivity()
                } else {
                    Snackbar.make(getRootView(), signUpViewModel.message, Snackbar.LENGTH_LONG)
                            .show()
                }
            }
        })

        tvTC.movementMethod = LinkMovementMethod.getInstance()

        btnSignUp.setOnClickListener {

            name = tiName.editText?.text.toString()
            username = tiUsername.editText?.text.toString()
            email = tiEmail.editText?.text.toString()
            password = tiPassword.editText?.text.toString()
            confirmedPassword = tiConfirmPassword.editText?.text.toString()
            needsMentoring = cbMentee.isChecked
            isAvailableToMentor = cbMentor.isChecked

            if (validateDetails()) {
                val requestData = Register(name, username, email, password, true, needsMentoring, isAvailableToMentor)
                signUpViewModel.register(requestData)
                showProgressDialog(getString(R.string.signing_up))
            }
        }
        btnLogin.setOnClickListener {
            navigateToLoginActivity()
        }
        cbTC.setOnCheckedChangeListener { _, b ->
            btnSignUp.isEnabled = b
        }
        imgUserAvatarSignUp.setOnClickListener {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
        }

        socialLoginViewModel = ViewModelProviders.of(this).get(SocialLoginViewModel::class.java)
        socialLoginViewModel.successful.observe(this, Observer {
            if (it != null)
                if (it)
                    socialSignUp()
                else {
                    hideProgressDialog()
                    if (socialLoginViewModel.message.isNotEmpty())
                        Snackbar.make(getRootView(), socialLoginViewModel.message,
                                Snackbar.LENGTH_SHORT).show()
                }
        })

        btnSignUpGoogle.setOnClickListener {
            showProgressDialog(getString(R.string.signing_up))
            socialLoginViewModel.loginWithGoogle(this)
        }

        btnSignUpFacebook.setOnClickListener {
            showProgressDialog(getString(R.string.signing_up))
            socialLoginViewModel.loginWithFacebook(this)
        }

        btnSignUpTwitter.setOnClickListener {
            showProgressDialog(getString(R.string.signing_up))
            socialLoginViewModel.loginWithTwitter(this)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        signUpViewModel.successful.removeObservers(this)
        signUpViewModel.successful.value = null
    }

    private fun validateDetails(): Boolean {
        var isValid = true
        if (name.isBlank()) {
            tiName.error = getString(R.string.error_empty_name)
            isValid = false
        } else {
            tiName.error = null
        }

        if (username.isBlank()) {
            tiUsername.error = getString(R.string.error_empty_username)
            isValid = false
        } else {
            tiUsername.error = null
        }

        if (email.isBlank()) {
            tiEmail.error = getString(R.string.error_empty_email)
            isValid = false
        } else {
            tiEmail.error = null
        }

        if (password.isBlank()) {
            tiPassword.error = getString(R.string.error_empty_password)
            isValid = false
        } else {
            tiPassword.error = null
        }

        if (password != confirmedPassword) {
            tiConfirmPassword.error = getString(R.string.error_not_matching_passwords)
            isValid = false
        } else {
            tiConfirmPassword.error = null
        }

        return isValid
    }

    override fun onBackPressed() {
        navigateToLoginActivity()
    }

    private fun navigateToLoginActivity() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        socialLoginViewModel.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.RC_SIGN_IN_GOOGLE) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val token = account?.idToken
                    if (token != null) {
                        val credential = GoogleAuthProvider.getCredential(token, null)
                        socialLoginViewModel.firebaseAuth(credential)
                    } else {
                        hideProgressDialog()
                        Snackbar.make(getRootView(), R.string.auth_failed,
                                Snackbar.LENGTH_SHORT).show()
                    }
                } catch (e: ApiException) {
                    hideProgressDialog()
                    e.printStackTrace()
                    Snackbar.make(getRootView(), R.string.auth_failed,
                            Snackbar.LENGTH_SHORT).show()
                }
            }
        } else {
            hideProgressDialog()
            Snackbar.make(getRootView(), R.string.error_something_went_wrong,
                    Snackbar.LENGTH_SHORT).show()
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                Glide.with(this)
                        .load(resultUri)
                        .centerCrop()
                        .into(imgUserAvatarSignUp)
                //TODO: upload the image
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.error.printStackTrace()
            }
        }
    }

    private fun socialSignUp() {
        hideProgressDialog()
        Toast.makeText(baseContext, "Welcome, ${socialLoginViewModel.auth.currentUser?.displayName}!",
                Toast.LENGTH_SHORT).show()
        // TODO: Add logic for social sign up
        /*
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // we need to finish LoginActivity as well
        finishAffinity()
        */
    }

}
