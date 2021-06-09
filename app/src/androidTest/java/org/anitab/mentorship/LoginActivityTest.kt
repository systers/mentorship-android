package org.anitab.mentorship

import android.content.ComponentName
import android.view.View
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.material.textfield.TextInputLayout
import org.anitab.mentorship.utils.CountingIdlingResourceSingleton
import org.anitab.mentorship.view.activities.LoginActivity
import org.anitab.mentorship.view.activities.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class specifies the UI test for LoginActivity
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    private val EMPTY_USERNAME_ERROR = "Username/Email cannot be empty"
    private val EMPTY_PASSWORD_ERROR = "Password cannot be empty"
    private val INCORRECT_CREDENTIALS_ERROR = "Username or password is wrong."

    private val INCORRECT_TEST_USERNAME = "blah"
    private val INCORRECT_TEST_PASSWORD = "blah"

    private val CORRECT_TEST_USERNAME = "gamikira"
    private val CORRECT_TEST_PASSWORD = "Divyansh@2001"

    @get:Rule
    var activityRule = ActivityTestRule(LoginActivity::class.java)

    @Rule
    @JvmField
    var intentsRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(CountingIdlingResourceSingleton.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(CountingIdlingResourceSingleton.countingIdlingResource)
    }

    private fun findEditTextInTextInputLayout(@IdRes textInputLayoutId: Int): ViewInteraction {

        return onView(allOf(isDescendantOfA(withId(textInputLayoutId)), isAssignableFrom(EditText::class.java)))
    }

    companion object {

        /**
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

    /**
     * This test checks that error messages are shown after the Login button is clicked
     * with:
     * username: EMPTY
     * password: EMPTY
     * expected: Empty username and password error
     */
    @Test
    fun testLoginButtonClickedWhenUsernameAndPasswordAreEmpty() {
        enterCredentials("", "")

        // Checks that the error message in both the editTexts appears after button click
        onView(withId(R.id.tiUsername)).check(matches(hasTextInputLayoutErrorText(EMPTY_USERNAME_ERROR)))
        onView(withId(R.id.tiPassword)).check(matches(hasTextInputLayoutErrorText(EMPTY_PASSWORD_ERROR)))
    }

    /**
     * This test checks that error messages are shown after the Login button is clicked
     * with:
     * username: EMPTY
     * password: PRESENT
     * expected : Empty username error
     */
    @Test
    fun testLoginButtonClickedWhenUsernameIsEmptyAndPasswordIsFilled() {
        enterCredentials("", CORRECT_TEST_PASSWORD)

        // Check for error message on username and not on password
        onView(withId(R.id.tiUsername)).check(matches(hasTextInputLayoutErrorText(EMPTY_USERNAME_ERROR)))
        onView(withId(R.id.tiPassword)).check(matches(not(hasTextInputLayoutErrorText(EMPTY_PASSWORD_ERROR))))
    }

    /**
     * This test checks that error messages are shown after the Login button is clicked
     * with:
     * username: PRESENT, correct
     * password: EMPTY
     * expected: empty password error
     */
    @Test
    fun testLoginButtonClickedWhenUsernameIsFilledAndPasswordIsEmpty() {
        enterCredentials(CORRECT_TEST_USERNAME, "")

        // Check for no error message on username with an error message on password
        onView(withId(R.id.tiUsername)).check(matches(not(hasTextInputLayoutErrorText(EMPTY_USERNAME_ERROR))))
        onView(withId(R.id.tiPassword)).check(matches(hasTextInputLayoutErrorText(EMPTY_PASSWORD_ERROR)))
    }

    /**
     * This test checks that error messages are shown after the Login button is clicked
     * with:
     * username: PRESENT, incorrect
     * password: PRESENT, incorrect
     * expected: Incorrect credentials error
     */
    @Test
    fun testLoginButtonClickedWhenDataIsIncorrect() {
        enterCredentials(INCORRECT_TEST_USERNAME, INCORRECT_TEST_PASSWORD)

        // Verify that a Snackbar with a proper message is shown
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(INCORRECT_CREDENTIALS_ERROR)))
    }

    /**
     * This test checks that the MainActivity is started when the credentials entered
     * by the user are correct.
     * username: PRESENT
     * password: PRESENT
     * expected: Intent to main activity
     */
    @Test
    fun testLoginButtonClickedWhenDataIsCorrect() {
        enterCredentials(CORRECT_TEST_USERNAME, CORRECT_TEST_PASSWORD)

        // Verify that MainActivity is started
        intended(hasComponent(
            ComponentName(InstrumentationRegistry.getInstrumentation().context, MainActivity::class.java))
        )
    }

    /**
     * This test checks that the Snackbar with an error message is shown after the Login
     * button is clicked with:
     * username: PRESENT, incorrect
     * password: PRESENT, correct
     * expected: Incorrect credentials error
     */
    @Test
    fun testLoginButtonWhenOnlyUsernameIsIncorrect() {
        enterCredentials(INCORRECT_TEST_USERNAME, CORRECT_TEST_PASSWORD)

        // Verify that a Snackbar with a proper message is shown
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(INCORRECT_CREDENTIALS_ERROR)))
    }

    /**
     * This test checks that the Snackbar with an error message is shown after the Login
     * button is clicked with:
     * username: PRESENT, correct
     * password: PRESENT, incorrect
     * expected: Incorrect credentials error
     */
    @Test
    fun testLoginButtonWhenOnlyPasswordIsIncorrect() {
        enterCredentials(CORRECT_TEST_USERNAME, INCORRECT_TEST_PASSWORD)

        // Verify that a Snackbar with a proper message is shown
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(INCORRECT_CREDENTIALS_ERROR)))
    }

    /**
     * Convenience method which enters credentials and click on the Login Button.
     */
    private fun enterCredentials(username: String, password: String) {
        // Type in a username
        findEditTextInTextInputLayout(R.id.tiUsername).perform(typeText(username), closeSoftKeyboard())

        // Type in a password
        findEditTextInTextInputLayout(R.id.tiPassword).perform(typeText(password), closeSoftKeyboard())

        // Perform a click on the Login Button
        onView(withId(R.id.btnLogin)).perform(click())
    }
}
