package org.anitab.mentorship.view.adapters

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.anitab.mentorship.MentorshipApplication
import org.anitab.mentorship.R
import org.anitab.mentorship.models.Relationship
import org.anitab.mentorship.view.fragments.RelationFragment
import org.anitab.mentorship.view.fragments.TasksFragment

@SuppressLint("ValidFragment")
/**
 * This is the [FragmentPagerAdapter] responsible for the configuration each fragment assigned to
 * each tabs. One tab displays the details of the current mentorship relation and the other provides
 * a detailed information about the tasks.
 * @param fm fragment manager
 */
class RelationPagerAdapter(fm: FragmentManager, private var mentorshipRelation: Relationship) : FragmentPagerAdapter(fm) {

    /**
     * This class represents the number and index of each tab of the layout
     */
    enum class TabsIndex(val value: Int) {
        DETAILS(0),
        TASKS(1)
    }

    val context = MentorshipApplication.getContext()

    override fun getItem(position: Int): Fragment {
        when (position) {
            TabsIndex.DETAILS.value -> {
                val relationFragment = RelationFragment.newInstance(mentorshipRelation)
                relationFragment.retainInstance = true
                return relationFragment
            }

            TabsIndex.TASKS.value -> {
                val tasksFragment = TasksFragment.newInstance(mentorshipRelation)
                tasksFragment.retainInstance = true
                return tasksFragment
            }
        }
        return TasksFragment.newInstance(mentorshipRelation)
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            RequestsPagerAdapter.TabsIndex.PENDING.value -> {
                return context.getString(R.string.details)
            }
            RequestsPagerAdapter.TabsIndex.PAST.value -> {
                return context.getString(R.string.tasks)
            }
        }
        return context.getString(R.string.details)
    }
}
