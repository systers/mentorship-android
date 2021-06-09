package org.anitab.mentorship.view.fragments

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.anitab.mentorship.R
import org.anitab.mentorship.view.activities.MainActivity
import org.anitab.mentorship.viewmodels.RelationViewModel
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RelationPagerFragmentTest {

    // Start the MainActivity
    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    /*
    * This test tests that the RelationPagerFragment() is loading correctly.
    * */
    @Test
    fun launch_currentRelationFragment() {
        activityTestRule.runOnUiThread {
            activityTestRule.activity.replaceFragment(R.id.contentFrame, RelationPagerFragment.newInstance(), R.string.fragment_title_relation)
        }
    }

    /*
    * This test tests that TextView with correct text and button are displayed, when the viewModels response is Successful
    * */
    @Test
    fun test_if_textView_and_FindMembersButton_isVisible() {
        activityTestRule.runOnUiThread {
            val viewModel = RelationViewModel()
            viewModel.getCurrentRelationDetails()
            viewModel.successfulGet.observeForever {
                if (it != null) {
                    if (it) {
                        onView(withId(R.id.tlMentorshipRelation)).check(matches(not(isDisplayed())))
                        onView(withId(R.id.tvNoCurrentRelation)).check(matches(withText("You are not in a current mentorship relation.")))
                        onView(withId(R.id.tvFindPeopleBtn)).check(matches(isDisplayed()))
                    }
                }
            }
        }
    }

    /*
    * This test tests that when Find Members Button is Clicked, is it navigating to MembersFragment
    * */
    @Test
    fun test_if_FindMembersButtonIsWorking() {
        activityTestRule.runOnUiThread {
            RelationPagerFragment.newInstance().viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    onView(withId(R.id.tvFindPeopleBtn)).perform(click())
                    onView(withId(R.id.rvMembers)).check(matches(isCompletelyDisplayed()))
                }
            }
        }
    }
}
