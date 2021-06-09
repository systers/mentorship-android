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
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.material.textfield.TextInputLayout
import org.anitab.mentorship.utils.CountingIdlingResourceSingleton
import org.anitab.mentorship.view.activities.LoginActivity
import org.anitab.mentorship.view.activities.SignUpActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class specifies the UI test for SignUpActivity
 */
@RunWith(AndroidJUnit4::class)
class SignUpActivityTest {

    private val EMPTY_USERNAME_ERROR: String = "Username/Email cannot be empty"
    private val EMPTY_PASSWORD_ERROR: String = "Password cannot be empty"
    private val EMPTY_EMAIL_ERROR: String = "Email cannot be empty"
    private val EMPTY_NAME_ERROR: String = "Name cannot be empty"
    private val EMPTY_CONFIRM_PASSWORD_ERROR: String = "Password confirmation cannot be empty"

    private val PASSWORD_DIDNT_MATCH_ERROR: String = "Passwords didn't match!"
    private val TOO_WEAK_PASSWORD_ERROR: String = "Your password is too weak! Use at least one small and capital letter, one number and one special sign!"

    private val EMAIL_ALREADY_REGISTERED: String = "A user with that email already exists."
    private val USERNAME_ALREADY_REGISTERED: String = "A user with that username already exists."

    /**
     * This basically setups the SignUpActivity before test
     */
    @get:Rule
    var mActivityRule: ActivityTestRule<SignUpActivity> = ActivityTestRule(SignUpActivity::class.java)

    @Rule
    @JvmField
    var intentsRule = IntentsTestRule(LoginActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(CountingIdlingResourceSingleton.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(CountingIdlingResourceSingleton.countingIdlingResource)
    }

    /**
     * This method is used to find the EditText within the TextInputLayout. Useful for typing into the TextInputLayout
     */
    fun findEditTextInTextInputLayout(@IdRes textInputLayoutId: Int): ViewInteraction {

        return onView(Matchers.allOf(isDescendantOfA(withId(textInputLayoutId)), isAssignableFrom(EditText::class.java)))
    }

    companion object {

        /**
         * This method simply implements the null check, checks the types and casts
         */
        fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> {

            return object : TypeSafeMatcher<View>() {
                /**
                 * Generates a description of the object.  The description may be part of a
                 * a description of a larger object of which this is just a component, so it
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

                    val error: CharSequence? = item.error
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
     * This test method is defined to test that when all the fields are empty and the user
     * clicks the signUp button then the errors in all the fields are displayed or not
     */
    @Test
    fun testSignUpClickedWhenAllFieldsAreEmpty() {

        findEditTextInTextInputLayout(R.id.tiName).perform(typeText(""), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiUsername).perform(typeText(""), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiEmail).perform(typeText(""), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiPassword).perform(typeText(""), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiConfirmPassword).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.cbTC)).perform(click())

        onView(withId(R.id.btnSignUp)).perform(scrollTo(), click())

        onView(withId(R.id.tiName)).check(matches(hasTextInputLayoutErrorText(EMPTY_NAME_ERROR)))
        onView(withId(R.id.tiUsername)).check(matches(hasTextInputLayoutErrorText(EMPTY_USERNAME_ERROR)))
        onView(withId(R.id.tiEmail)).check(matches(hasTextInputLayoutErrorText(EMPTY_EMAIL_ERROR)))
        onView(withId(R.id.tiPassword)).check(matches(hasTextInputLayoutErrorText(EMPTY_PASSWORD_ERROR)))
        onView(withId(R.id.tiConfirmPassword)).check(matches(hasTextInputLayoutErrorText(EMPTY_CONFIRM_PASSWORD_ERROR)))
        onView(withId(R.id.tvNoteSignUp)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * This test method simply checks that if the password and confirm password are unequal
     * and user clicks on signUp button then the error message is coming in confirm
     * password editText
     */
    @Test
    fun whenPassAreUnequalShowsErrorTest() {
        findEditTextInTextInputLayout(R.id.tiName).perform(typeText("name"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiUsername).perform(typeText("username"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiEmail).perform(typeText("email@test.com"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiPassword).perform(typeText("qwertz123!"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiConfirmPassword).perform(typeText("qwertz123!45"), closeSoftKeyboard())
        onView(withId(R.id.cbTC)).perform(click())

        onView(withId(R.id.btnSignUp)).perform(scrollTo(), click())

        onView(withId(R.id.tiConfirmPassword)).check(matches(hasTextInputLayoutErrorText(PASSWORD_DIDNT_MATCH_ERROR)))
    }

    /**
     * This test method simply checks that if the password is too weak
     * and user clicks on signUp button then the error message is coming in
     * password editText
     */
    @Test
    fun whenPassIsTooWeakShowsErrorTest() {
        findEditTextInTextInputLayout(R.id.tiName).perform(typeText("name"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiUsername).perform(typeText("username"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiEmail).perform(typeText("email@test.com"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiPassword).perform(typeText("qwertz123!"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiConfirmPassword).perform(typeText("qwertz123!"), closeSoftKeyboard())
        onView(withId(R.id.cbTC)).perform(click())

        onView(withId(R.id.btnSignUp)).perform(scrollTo(), click())

        onView(withId(R.id.tiPassword)).check(matches(hasTextInputLayoutErrorText(TOO_WEAK_PASSWORD_ERROR)))
    }

    /**
     * This test method simply checks that if the password is too weak
     * and user clicks on signUp button then the error message is coming in
     * password editText
     */
    @Test
    fun whenPassIsTooWeakShowsErrorTest2() {
        findEditTextInTextInputLayout(R.id.tiName).perform(typeText("name"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiUsername).perform(typeText("username"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiEmail).perform(typeText("email@test.com"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiPassword).perform(typeText("Qwertz123"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiConfirmPassword).perform(typeText("Qwertz123"), closeSoftKeyboard())
        onView(withId(R.id.cbTC)).perform(click())

        onView(withId(R.id.btnSignUp)).perform(scrollTo(), click())

        onView(withId(R.id.tiPassword)).check(matches(hasTextInputLayoutErrorText(TOO_WEAK_PASSWORD_ERROR)))
    }

    /*
    * This test checks that login button is opening the login activity
    * */
    @Test
    fun loginButtonIsWorkingCorrectly() {
        onView(withId(R.id.btnLogin)).perform(scrollTo(), click())
        intended(hasComponent(
                ComponentName(InstrumentationRegistry.getInstrumentation().context, LoginActivity::class.java)
        ))
    }

    /*
    * This test checks that correct error message is shown when Username is already registered
    * */
    @Test
    fun whenUsernameIsAlreadyRegistered() {
        findEditTextInTextInputLayout(R.id.tiName).perform(typeText("name"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiUsername).perform(typeText("Divyansh"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiEmail).perform(typeText("justdvnsh2208@gmail.com"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiPassword).perform(typeText("Divyansh@2001"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiConfirmPassword).perform(typeText("Divyansh@2001"), closeSoftKeyboard())
        onView(withId(R.id.cbBoth)).perform(click())
        onView(withId(R.id.cbTC)).perform(click())

        onView(withId(R.id.btnSignUp)).perform(scrollTo(), click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(USERNAME_ALREADY_REGISTERED)))
    }

    /*
    * This test checks that correct error message is shown when Email is already registered
    * */
    @Test
    fun whenEmailIsAlreadyRegistered() {
        findEditTextInTextInputLayout(R.id.tiName).perform(typeText("name"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiUsername).perform(typeText("Divyansh22087812"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiEmail).perform(typeText("justdvnsh2208@gmail.com"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiPassword).perform(typeText("Divyansh@2001"), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiConfirmPassword).perform(typeText("Divyansh@2001"), closeSoftKeyboard())
        onView(withId(R.id.cbBoth)).perform(click())
        onView(withId(R.id.cbTC)).perform(click())

        onView(withId(R.id.btnSignUp)).perform(scrollTo(), click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(EMAIL_ALREADY_REGISTERED)))
    }
}
