package org.anitab.mentorship

import android.widget.EditText
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.anitab.mentorship.view.activities.MainActivity
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditProfileFragmentTest {

    // launches the main Activity
    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    // This setup will run before every test to check if the edit profile fragment is built or the profile Fragment is
    // still refreshing. If it is, then it would wait until the Profile Fragment is finished loading.
    @Before
    fun setup() {
        onView(withId(R.id.navigation_profile)).perform(click())
        var timeout = 0L
        while (timeout < 5000) {
            try {
                onView(withId(R.id.imgUserAvatar)).check(matches(isDisplayed()))
                break
            } catch (e: Exception) {
                Thread.sleep(1000)
                timeout += 1000
            }
        }
        onView(withId(R.id.menu_edit_profile)).perform(click())
    }

    /*
    * This test checks if the cancel button is working properly
    * */
    @Test
    fun check_if_cancel_button_is_dimissing_the_dialog() {
        onView(withText(R.string.cancel)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
    }

    /*
    * This test checks if the save button is working properly
    * */
    @Test
    fun check_if_save_button_is_working() {
        onView(withText(R.string.save)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
    }

    /*
    * This test checks if all the edit texts are being displayed and they are all working
    * */
    @Test
    fun check_if_all_edit_text_fields_are_being_displayed_and_working() {
        enterText(R.id.tiName)
        enterText(R.id.tiBio)
        enterText(R.id.tiSlack)
        enterText(R.id.tiLocation)
        enterText(R.id.tiOccupation)
        enterText(R.id.tiOrganization)
        enterText(R.id.tiSkills)
        enterText(R.id.tiInterests)
        onView(withText(R.string.cancel)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
    }

    /*
    * This test checks if the switch buttons are working properly
    * */
    @Test
    fun check_if_switch_buttons_are_working() {
        onView(withId(R.id.switchAvailableToMentor)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.switchNeedsMentoring)).check(matches(isDisplayed())).perform(click())
        onView(withText(R.string.cancel)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
    }

    // helper methods

    // check if the provided id is an edit text
    private fun findEditTextInTextInputLayout(@IdRes textInputLayoutId: Int): ViewInteraction {

        return onView(CoreMatchers.allOf(isDescendantOfA(withId(textInputLayoutId)), isAssignableFrom(EditText::class.java)))
    }

    /**
     * Convenience method which enters credentials and click on the Login Button.
     */
    private fun enterText(id: Int) {
        // Type in a username
        findEditTextInTextInputLayout(id).perform(ViewActions.typeText("AnitaB.org"), ViewActions.closeSoftKeyboard())
    }
}
