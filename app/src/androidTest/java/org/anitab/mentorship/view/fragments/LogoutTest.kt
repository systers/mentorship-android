package org.anitab.mentorship.view.fragments

import android.content.ComponentName
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.anitab.mentorship.R
import org.anitab.mentorship.view.activities.LoginActivity
import org.anitab.mentorship.view.activities.SettingsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LogoutTest {

    // Start the SettingsActivity
    @get:Rule
    val activityTestRule = ActivityTestRule(SettingsActivity::class.java)

    // Rule to define the Intents
    @Rule
    @JvmField
    var intentsRule = IntentsTestRule(LoginActivity::class.java)

    /*
    * Test to check if the Logout Dialog is opening on click
    * */
    @Test
    fun check_if_logout_dialog_is_opening() {
        onView(withId(R.id.tvLogout)).run {
            check(matches(withText(R.string.popup_menu_logout)))
            perform(click())
        }
    }

    /*
   * Test to check if the button is logging out users
   * */
    @Test
    fun check_if_logout_button_is_logging_out_user() {
        // Find and click the Logout button in Settings Activity
        onView(withId(R.id.tvLogout)).perform(click())

        // clicks the Ok button in the Dialog
        onView(withText(R.string.logout)).inRoot(RootMatchers.isDialog())
                .check(matches(ViewMatchers.isDisplayed())).perform(click())

        // Verify that LoginActivity is started
        Intents.intended(IntentMatchers.hasComponent(
                ComponentName(InstrumentationRegistry.getInstrumentation().context, LoginActivity::class.java))
        )
    }
}
