@file:Suppress("DEPRECATION")

package org.anitab.mentorship.view.activities

import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import org.anitab.mentorship.utils.ProgressBar

/**
 * An Activity class which other Activities can extend from. It provides some basic functions like
 * showing/hiding progress dialog bars, hiding keyboard etc.
 */
abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null
    private var progressBar: ProgressBar? = null

    /**
     * Shows a [ProgressDialog]. This is used when fetching data from a remote source
     * @param message text to be shown while he progress dialog is being shown
     */
    fun showProgressDialog(message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this, ProgressDialog.STYLE_SPINNER)
            progressDialog?.setCancelable(false)
        }
        progressDialog?.setMessage(message)
        progressDialog?.show()
    }

    /**
     * Hides the [ProgressDialog]. This is called when fetching data from a remote source finishes
     */
    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing)
            progressDialog?.dismiss()
    }

    protected fun showProgressBar() {
        progressBar = ProgressBar(this)
        progressBar?.show()
    }

    protected fun hideProgressBar() {
        progressBar?.hide()
    }

    protected fun hideKeyboard(view: View) {
        val inputManager = this
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager
                .RESULT_UNCHANGED_SHOWN)
    }

    protected fun getRootView(): View {
        return window.decorView.findViewById(android.R.id.content)
    }

    /**
     * The [fragment] is added to the container view with id [containerId]. The operation is
     * performed by the FragmentManager.
     */
    fun replaceFragment(containerId: Int, fragment: Fragment, title: Int) {
        supportFragmentManager.beginTransaction()
                .replace(containerId, fragment, getString(title)).commit()
        supportActionBar?.setTitle(title)
        collapsingToolbarLayout?.title = getString(title)
    }
}
