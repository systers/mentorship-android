package org.anitab.mentorship.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_member_item.view.*
import org.anitab.mentorship.MentorshipApplication
import org.anitab.mentorship.R
import org.anitab.mentorship.models.User
import org.anitab.mentorship.utils.Constants
import org.anitab.mentorship.utils.Constants.INTERESTS_KEY
import org.anitab.mentorship.utils.Constants.LOCATION_KEY
import org.anitab.mentorship.utils.Constants.SKILLS_KEY
import org.anitab.mentorship.utils.NON_VALID_VALUE_REPLACEMENT
import org.anitab.mentorship.utils.UsersDiffCallback
import org.anitab.mentorship.view.fragments.MembersFragment

/**
 * This class represents the adapter that fills in each view of the Members recyclerView
 * @param userList list of users to show
 * @param openDetailFunction function to be called when an item from Members list is clicked
 */
class MembersAdapter(
    private var userList: ArrayList<User> = arrayListOf<User>(),
    private val openDetailFunction: (memberId: Int, sharedImageView: ImageView, sharedTextView: TextView) -> Unit

) : RecyclerView.Adapter<MembersAdapter.MembersViewHolder>() {

    val context = MentorshipApplication.getContext()
    var lastPosition = -1
    private var filterMap = hashMapOf(Constants.SORT_KEY to MembersFragment.SortValues.REGISTRATION_DATE.name)
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
        itemView.tvUsername.text = item.username
        itemView.tvMentorshipAvailability.text = getMentorshipAvailabilityText(item.availableToMentor, item.needMentoring)

        val userInterests = item.interests
        val validText = if (userInterests.isNullOrBlank()) NON_VALID_VALUE_REPLACEMENT else userInterests
        val keyText = context.getString(R.string.interests)
        val keyValueText = "$keyText: $validText"
        itemView.tvInterests.text = keyValueText

        itemView.setOnClickListener { openDetailFunction(item.id!!, itemView.circleImageView, itemView.tvName) }

        val animation = AnimationUtils.loadAnimation(context,
                if (position > lastPosition) R.anim.bottom_to_top else R.anim.top_to_bottom)
        holder.itemView.startAnimation(animation)
        lastPosition = position
    }

    override fun getItemCount(): Int = filteredUserList.size

    fun updateUsersList(map: HashMap<String, String>, newUsers: List<User>) {
        // updating users list
        setData(newUsers)
        // getting updated filtered users
        val newFilteredUsers = getFilteredUsers(map, newUsers)

        // applying changes to adapter
        val usersDiffCallback = UsersDiffCallback(filteredUserList, newFilteredUsers)
        val diffResult = DiffUtil.calculateDiff(usersDiffCallback)
        filteredUserList.clear()
        filteredUserList.addAll(newFilteredUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getFilteredUsers(map: HashMap<String, String>, newUsers: List<User>): List<User> {
        var newFilteredList: List<User> = arrayListOf()
        when (map[Constants.SORT_KEY]) {
            MembersFragment.SortValues.REGISTRATION_DATE.name -> {
                newFilteredList = newUsers
            }
            MembersFragment.SortValues.NAMEAZ.name -> {
                newFilteredList = newUsers.sortedBy {
                    it.name
                } as MutableList
            }
            MembersFragment.SortValues.NAMEZA.name -> {
                newFilteredList = newUsers.sortedByDescending {
                    it.name
                } as MutableList
            }
        }

        if (map[Constants.NEED_MENTORING_KEY] == "true")
            newFilteredList = newFilteredList.filter {
                it.needMentoring == true
            } as MutableList<User>

        if (map[Constants.AVAILABLE_TO_MENTOR_KEY] == "true")
            newFilteredList = newFilteredList.filter {
                it.availableToMentor == true
            } as MutableList<User>

        val interests = map[INTERESTS_KEY]
        val location = map[LOCATION_KEY]
        val skills = map[SKILLS_KEY]

        newFilteredList = newFilteredList.filter {
            var valid = true

            if (!interests.isNullOrEmpty())
                if (it.interests.isNullOrEmpty())
                    valid = false
                else if (it.interests?.contains(interests, ignoreCase = true) == false)
                    valid = false

            if (!location.isNullOrEmpty())
                if (it.location.isNullOrEmpty())
                    valid = false
                else if (it.location?.contains(location, ignoreCase = true) == false)
                    valid = false

            if (!skills.isNullOrEmpty())
                if (it.skills.isNullOrEmpty())
                    valid = false
                else if (it.skills?.contains(skills, ignoreCase = true) == false)
                    valid = false

            valid
        } as MutableList<User>

        return newFilteredList
    }

    private fun setData(users: List<User>) {
        userList.clear()
        userList.addAll(users)
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

    override fun onViewDetachedFromWindow(holder: MembersViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
}
