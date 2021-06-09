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

class RequestDetailActivityTestA {

    // stub data that will be sent in the intent that calls RequestDetailActivity
    private val date: Float = System.currentTimeMillis().toFloat()
    private val relationship = Relationship(
            0, 0, true,
            Relationship.RelationUserResponse(0, "test_user_1"),
            Relationship.RelationUserResponse(1, "test_user_2"),
            date, 0.0F, 0.0F, date, 1, "test"
    )

    // rule to start MainActivity
    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    /*
     * Test to verify the delete button is visible if a request is sent by me
     */
    @Test
    fun checkDeleteButtonPresent() {
        // create an intent to open request detail activity with stub data
        val intent = Intent(activityRule.activity, RequestDetailActivity::class.java)
        intent.putExtra(Constants.RELATIONSHIP_EXTRA, relationship)

        // starts the request detail activity
        activityRule.activity.startActivity(intent)

        // checks whether the delete button is visible/present in the request detail activity
        onView(withId(R.id.btnDelete)).check(matches(isDisplayed()))
    }
}
