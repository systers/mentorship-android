package org.anitab.mentorship.view.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_change_password.view.*
import org.anitab.mentorship.R
import org.anitab.mentorship.remote.requests.ChangePassword
import org.anitab.mentorship.utils.checkPasswordSecurity
import org.anitab.mentorship.viewmodels.ChangePasswordViewModel

/**
 * The fragment is responsible for changing User password
 */
class ChangePasswordFragment : DialogFragment() {

    companion object {
        /**
         * Creates an instance of ChangePasswordFragment
         */
        fun newInstance() = ChangePasswordFragment()
    }

    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()
    private lateinit var changePasswordView: View
    private lateinit var currentPassword: String
    private lateinit var newPassword: String
    private lateinit var confirmPassword: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        changePasswordViewModel.successfulUpdate.observe(this, { successful ->

            if (successful != null) {
                when {
                    successful -> Toast.makeText(activity, getString(R.string.password_updated), Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(activity, changePasswordViewModel.message, Toast.LENGTH_SHORT).show()
                }
            }
            dismiss()
        })

        changePasswordView = LayoutInflater.from(context).inflate(R.layout.fragment_change_password, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.change_password))
        builder.setView(changePasswordView)
        builder.setPositiveButton(getString(R.string.ok)) { _, _ -> }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }
        val passwordDialog = builder.create()
        passwordDialog.setOnShowListener { passwordDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = false }

        changePasswordView.tilConfirmPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(confirmPasswordEditable: Editable?) {
                passwordDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = confirmPasswordEditable!!.isNotEmpty()
            }
        })
        return passwordDialog
    }

    override fun onResume() {
        super.onResume()
        val passwordDialog = dialog as? AlertDialog
        passwordDialog?.setCanceledOnTouchOutside(false)

        // Runtime check New Password & ConfirmPassword
        passwordDialog?.tilNewPassword?.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validatePassword()
            }
        })
        passwordDialog?.tilConfirmPassword?.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateConfirmedPassword()
            }
        })

        passwordDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            currentPassword = changePasswordView.tilCurrentPassword?.editText?.text.toString()
            newPassword = changePasswordView.tilNewPassword?.editText?.text.toString()
            confirmPassword = changePasswordView.tilConfirmPassword?.editText?.text.toString()

            if (validatePassword() && validateConfirmedPassword()) {
                changePasswordViewModel.changeUserPassword(ChangePassword(currentPassword, newPassword))
            }
        }
    }

    private fun validatePassword(): Boolean {
        currentPassword = changePasswordView.tilCurrentPassword?.editText?.text.toString()
        newPassword = changePasswordView.tilNewPassword?.editText?.text.toString()
        var isValid = true

        if (currentPassword == newPassword) {
            changePasswordView.tilNewPassword?.error = getString(R.string.current_new_password_should_not_same)
            isValid = false
        } else {
            if (!newPassword.checkPasswordSecurity()) {
                changePasswordView.tilNewPassword?.error = getString(R.string.error_password_too_weak)
                isValid = false
            } else {
                changePasswordView.tilNewPassword?.error = null
            }
        }
        return isValid
    }

    private fun validateConfirmedPassword(): Boolean {
        confirmPassword = changePasswordView.tilConfirmPassword?.editText?.text.toString()
        val isValid = true
        if (newPassword != confirmPassword) {
            changePasswordView.tilConfirmPassword?.error = getString(R.string.password_not_match)
        } else {
            changePasswordView.tilConfirmPassword?.error = null
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        changePasswordViewModel.successfulUpdate.removeObservers(this)
        changePasswordViewModel.successfulUpdate.value = null
    }
}
