package org.systers.mentorship.view.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.systers.mentorship.R
import org.systers.mentorship.remote.requests.Register
import org.systers.mentorship.viewmodels.SignUpViewModel
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

/**
 * This activity will let the user to sign up into the system using name, username,
 * email and password.
 */
class SignUpActivity : BaseActivity() {

    private lateinit var signUpViewModel: SignUpViewModel

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

        // DOB
        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val year = currentYear - 13
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        btnDOB.setOnClickListener {

            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                if (year > 2007) {
                    showDate.setText("You must be 13 Years old")
                } else {
                    showDate.setText("" + dayOfMonth + " " + monthOfYear + 1 + ", " + year)
                }
            }, currentYear, month, day)
            datePickerDialog.show()
        }

        btnSignUp.setOnClickListener {

            name = tiName.editText?.text.toString()

            // ADD DOB FOR MINIMUM AGE 13 YEARS OLD


            username = tiUsername.editText?.text.toString()
            email = tiEmail.editText?.text.toString()
            password = tiPassword.editText?.text.toString()
            confirmedPassword = tiConfirmPassword.editText?.text.toString()
            needsMentoring = cbMentee.isChecked
            isAvailableToMentor = cbMentor.isChecked
            year > 0

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

    private fun navigateToLoginActivity() {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
