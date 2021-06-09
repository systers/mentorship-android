package org.anitab.mentorship.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPage
import org.anitab.mentorship.R
import org.anitab.mentorship.dsl.getColorAttr

class IntroActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val page1 = SliderPage(
            title = getString(R.string.page_1_title),
            description = getString(R.string.page_1_description),
            bgColor = getColorAttr(R.attr.colorPrimary),
            imageDrawable = R.drawable.ic_undraw_shared_workspace
        )

        val page2 = SliderPage(
            title = getString(R.string.page_2_title),
            description = getString(R.string.page_2_description),
            bgColor = getColorAttr(R.attr.colorPrimaryDark),
            imageDrawable = R.drawable.ic_undraw_proud_self
        )

        val page3 = SliderPage(
            title = getString(R.string.page_3_title),
            description = getString(R.string.page_3_description),
            bgColor = getColorAttr(R.attr.colorPrimary),
            imageDrawable = R.drawable.ic_undraw_a_day_off
        )

        val page4 = SliderPage(
            title = getString(R.string.page_4_title),
            description = getString(R.string.page_4_description),
            bgColor = getColorAttr(R.attr.colorPrimaryDark),
            imageDrawable = R.drawable.ic_undraw_people
        )

        addSlide(AppIntro2Fragment.newInstance(page1))
        addSlide(AppIntro2Fragment.newInstance(page2))
        addSlide(AppIntro2Fragment.newInstance(page3))
        addSlide(AppIntro2Fragment.newInstance(page4))
        skipButtonEnabled = false
        setColorTransitionsEnabled(true)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        val preferences =
            getSharedPreferences(getString(R.string.intro_prefs), Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(getString(R.string.intro_prefs_first_run), false)
        editor.apply()
    }
}
