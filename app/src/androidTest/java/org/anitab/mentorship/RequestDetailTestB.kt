package org.anitab.mentorship

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.anitab.mentorship.models.Relationship
import org.anitab.mentorship.utils.Constants
import org.anitab.mentorship.view.activities.MainActivity
import org.anitab.mentorship.view.activities.RequestDetailActivity
import org.junit.Rule
import org.junit.Test

class RequestDetailActivityTestB {

    // Stub data that will be sent in the intent that calls RequestDetailActivity
    private val date: Float = System.currentTimeMillis().toFloat()
    private val relationship = Relationship(
            0, 0, false,
            Relationship.RelationUserResponse(0, "test_user_1"),
            Relationship.RelationUserResponse(1, "test_user_2"),
            date, 0.0F, 0.0F, date, 1, "test"
    )

    // Rule to start Main Activity
    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    /*
     *  Test to verify the accept and reject buttons are visible if a request is received by me
     */
    @Test
    fun checkAcceptAndRejectButtonPresent() {
        // Create an intent that will call the RequestDetailActivity and add stub data
        val intent = Intent(activityRule.activity, RequestDetailActivity::class.java)
        intent.putExtra(Constants.RELATIONSHIP_EXTRA, relationship)

        // Start the RequestDetailActivity
        activityRule.activity.startActivity(intent)

        // Verify whether the accept button is visible/displayed
        onView(withId(R.id.btnAccept)).check(matches(isDisplayed()))

        // Verify whether the reject button is visible/displayed
        onView(withId(R.id.btnReject)).check(matches(isDisplayed()))
    }
}
