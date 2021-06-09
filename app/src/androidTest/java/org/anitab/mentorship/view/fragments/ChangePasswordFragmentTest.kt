package org.anitab.mentorship.view.fragments

import android.view.View
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.material.textfield.TextInputLayout
import org.anitab.mentorship.R
import org.anitab.mentorship.view.activities.SettingsActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ChangePasswordFragmentTest {

    private val res = InstrumentationRegistry.getInstrumentation().targetContext.resources
    private val PASSWORD_TOO_WEAK = res.getString(R.string.error_password_too_weak)
    private val PASSWORDS_DO_NOT_MATCH = res.getString(R.string.password_not_match)

    // Start the SettingsActivity
    @get:Rule
    val activityTestRule = ActivityTestRule(SettingsActivity::class.java)

    /*
    * Test to launch edit password Fragment
    * */
    @Test
    fun launch_editPassword_fragment() {
        onView(withId(R.id.tvResetPassword)).run {
            check(matches(withText(R.string.change_password)))
            perform(click())
        }
    }

    /*
    * This test checks that errors are being displayed correctly when the password is too weak
    * */
    @Test
    fun check_if_errors_are_shown_when_password_is_too_weak() {
        // launch the fragment
        ChangePasswordFragment.newInstance().show(activityTestRule.activity.supportFragmentManager, null)

        enter_credentials(R.id.tilNewPassword, "ab")

        // clicks the OK button in the Dialog Fragment
        onView(withText(R.string.ok)).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(click())

        // Checks if the errors are showing with the correct text
        onView(withId(R.id.tilNewPassword)).check(matches(hasTextInputLayoutErrorText(PASSWORD_TOO_WEAK)))
    }

    /*
    * This test checks that errors are being displayed correctly when the password do not match
    * */
    @Test
    fun check_if_errors_are_shown_when_passwords_do_not_match() {
        // launch the Fragment
        ChangePasswordFragment.newInstance().show(activityTestRule.activity.supportFragmentManager, null)

        enter_credentials(R.id.tilNewPassword, "AnitaB.org@2020")
        enter_credentials(R.id.tilConfirmPassword, "AnitaB.org")

        // clicks the OK button in the Dialog Fragment
        onView(withText(R.string.ok)).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(click())

        // Checks if the errors are showing with the correct text
        onView(withId(R.id.tilConfirmPassword)).check(matches(hasTextInputLayoutErrorText(PASSWORDS_DO_NOT_MATCH)))
    }

    // --- Helper Methods --- //

    /*
    * helper method to enter password
    * */
    private fun enter_credentials(id: Int, password: String) = findEditTextInTextInputLayout(id).run {
        check(matches(isDisplayed()))
        perform(click())
        perform(typeText(password))
    }

    // helper method to find Edit Text
    private fun findEditTextInTextInputLayout(@IdRes textInputLayoutId: Int): ViewInteraction {

        return onView(CoreMatchers.allOf(isDescendantOfA(withId(textInputLayoutId)), isAssignableFrom(EditText::class.java)))
    }

    /**
     * Helper method
     * This simply implements the null check, checks the type and then casts.
     */
    fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            /**
             * Generates a description of the object.  The description may be part of a
             * larger object of which this is just a component, so it
             * should be worded appropriately.
             *
             * @param description
             * The description to be built or appended to.
             */
            override fun describeTo(description: Description?) {
                // ("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            /**
             * Subclasses should implement this. The item will already have been checked for
             * the specific type and will never be null.
             */
            override fun matchesSafely(item: View?): Boolean {

                if ((item !is TextInputLayout)) {
                    return false
                }

                val error = item.error
                if (error == null) {
                    return false
                }

                val errorMsg: String = error.toString()
                return expectedErrorText.equals(errorMsg)
            }
        }
    }
}
