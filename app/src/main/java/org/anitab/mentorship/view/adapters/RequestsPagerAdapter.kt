package org.anitab.mentorship.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.anitab.mentorship.MentorshipApplication
import org.anitab.mentorship.R
import org.anitab.mentorship.models.RelationState
import org.anitab.mentorship.models.Relationship
import org.anitab.mentorship.utils.Constants
import org.anitab.mentorship.utils.getUnixTimestampInMilliseconds
import org.anitab.mentorship.view.fragments.RequestPagerFragment

/**
 * This is the [FragmentPagerAdapter] responsible for the configuration each fragment assigned to
 * each tabs. I will filter the [requestsList] and split it into 2 additional lists: pending
 * and past requests lists
 * @param requestsList list of all mentorship relations and requests
 * @param fm fragment manager
 */
class RequestsPagerAdapter(
    private val requestsList: List<Relationship>,
    private val pendingRequestsList: List<Relationship>,
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    /**
     * This class represents the number and index of each tab of the layout
     */
    enum class TabsIndex(val value: Int) {
        PENDING(0),
        PAST(1),
        ALL(2)
    }

    val context = MentorshipApplication.getContext()

    private val pastList: List<Relationship> by lazy {
        requestsList.filter {
            val hasEndTimePassed = getUnixTimestampInMilliseconds(it.endDate) < System.currentTimeMillis()
            val isAcceptedState = RelationState.ACCEPTED.value == it.state

            !isAcceptedState && hasEndTimePassed
        }
    }
    private val allList: List<Relationship> by lazy {
        requestsList.filter {
            val isAcceptedState = RelationState.ACCEPTED.value == it.state

            !isAcceptedState
        }
    }

    override fun getItemCount(): Int = Constants.TOTAL_REQUEST_TABS

    override fun createFragment(position: Int): Fragment {
        when (position) {
            TabsIndex.PENDING.value -> {
                return RequestPagerFragment.newInstance(
                        pendingRequestsList, context.getString(R.string.empty_pending_requests))
            }
            TabsIndex.PAST.value -> {
                return RequestPagerFragment.newInstance(
                        pastList, context.getString(R.string.empty_past_requests))
            }
            TabsIndex.ALL.value -> {
                return RequestPagerFragment.newInstance(
                        allList, context.getString(R.string.empty_requests))
            }
        }
        return RequestPagerFragment.newInstance(
                pendingRequestsList, context.getString(R.string.empty_pending_requests))
    }
}
