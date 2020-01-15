package org.systers.mentorship.view.adapters

import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_member_item.view.*
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.models.User
import org.systers.mentorship.utils.Constants
import org.systers.mentorship.utils.Constants.INTERESTS_KEY
import org.systers.mentorship.utils.Constants.LOCATION_KEY
import org.systers.mentorship.utils.Constants.SKILLS_KEY
import org.systers.mentorship.utils.NON_VALID_VALUE_REPLACEMENT
import org.systers.mentorship.view.fragments.MembersFragment

/**
 * This class represents the adapter that fills in each view of the Members recyclerView
 * @param userList list of users to show
 * @param openDetailFunction function to be called when an item from Members list is clicked
 */
class MembersAdapter(
        private var userList: List<User>,
        private val openDetailFunction: (memberId: Int) -> Unit
) : RecyclerView.Adapter<MembersAdapter.MembersViewHolder>() {

    val context = MentorshipApplication.getContext()

    private var filteredUserList = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder =
            MembersViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.list_member_item, parent, false)
            )

    override fun onBindViewHolder(@NonNull holder: MembersViewHolder, position: Int) {
        val item = filteredUserList[position]
        val itemView = holder.itemView

        itemView.tvName.text = item.name
        itemView.tvMentorshipAvailability.text = getMentorshipAvailabilityText(item.isAvailableToMentor, item.needsMentoring)

        val userInterests = item.interests
        val validText = if (userInterests.isNullOrBlank()) NON_VALID_VALUE_REPLACEMENT else userInterests
        val keyText = context.getString(R.string.interests)
        val keyValueText = "$keyText: $validText"
        itemView.tvInterests.text = keyValueText

        itemView.setOnClickListener { openDetailFunction(item.id!!) }
    }

    override fun getItemCount(): Int = filteredUserList.size

    fun filter(map: HashMap<String, String>) {
        when (map[Constants.SORT_KEY]) {
            MembersFragment.SortValues.REGISTRATION_DATE.name -> {
                filteredUserList = userList as MutableList
            }
            MembersFragment.SortValues.NAMEAZ.name -> {
                filteredUserList = userList.sortedBy {
                    it.name
                } as MutableList
            }
            MembersFragment.SortValues.NAMEZA.name -> {
                filteredUserList = userList.sortedByDescending {
                    it.name
                } as MutableList
            }
        }

        if (map[Constants.NEED_MENTORING_KEY] == "true")
            filteredUserList = filteredUserList.filter {
                it.needsMentoring == true
            } as MutableList<User>

        if (map[Constants.AVAILABLE_TO_MENTOR_KEY] == "true")
            filteredUserList = filteredUserList.filter {
                it.isAvailableToMentor == true
            } as MutableList<User>

        val interests = map[INTERESTS_KEY]
        val location = map[LOCATION_KEY]
        val skills = map[SKILLS_KEY]

        filteredUserList = filteredUserList.filter {
            var valid = true

            if (!interests.isNullOrEmpty())
                if (it.interests.isNullOrEmpty())
                    valid = false
                else if (it.interests?.contains(interests) == false)
                    valid = false

            if (!location.isNullOrEmpty())
                if (it.location.isNullOrEmpty())
                    valid = false
                else if (it.location?.contains(location) == false)
                    valid = false

            if (!skills.isNullOrEmpty())
                if (it.skills.isNullOrEmpty())
                    valid = false
                else if (it.skills?.contains(skills) == false)
                    valid = false

            valid
        } as MutableList<User>

        notifyDataSetChanged()
    }

    fun setData(users: List<User>) {
        userList = users
    }

    /**
     * This class holds a view for each item of the Members list
     * @param itemView represents each view of Members list
     */
    class MembersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun getMentorshipAvailabilityText(availableToMentor: Boolean?, needMentoring: Boolean?): String {

        if (availableToMentor != null && needMentoring != null) {
            return if (availableToMentor && needMentoring) context.getString(R.string.available_to_mentor_and_mentee)
            else if (availableToMentor) context.getString(R.string.only_available_to_mentor)
            else if (needMentoring) context.getString(R.string.only_available_to_mentee)
            else context.getString(R.string.not_available_to_mentor_or_mentee)
        }

        return context.getString(R.string.not_available_to_mentor_or_mentee)
    }
}
