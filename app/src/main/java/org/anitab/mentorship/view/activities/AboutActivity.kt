package org.anitab.mentorship.view.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*
import org.anitab.mentorship.R

class AboutActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setTitle(R.string.fragment_title_about)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnGit.setOnClickListener(this)
        btnSlack.setOnClickListener(this)
        btnWebsite.setOnClickListener(this)
        btnTermsCondition.setOnClickListener(this)
        btncodeofconduct.setOnClickListener(this)
        btnprivacypolicy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val url = when (v?.id) {
            R.id.btnGit -> getString(R.string.url_github)
            R.id.btnSlack -> getString(R.string.url_zulip)
            R.id.btnWebsite -> getString(R.string.url_website)
            R.id.btnTermsCondition -> getString(R.string.url_terms)
            R.id.btncodeofconduct -> getString(R.string.url_code_of_conduct)
            R.id.btnprivacypolicy -> getString(R.string.url_privacy)

            else -> {
                return
            }
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.activity_not_found_exception_error, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
