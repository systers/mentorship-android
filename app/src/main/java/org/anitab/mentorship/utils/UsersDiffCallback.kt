package org.anitab.mentorship.utils

import androidx.recyclerview.widget.DiffUtil
import org.anitab.mentorship.models.User

class UsersDiffCallback(private val mOldUsersList: List<User>, private val mNewUsersList: List<User>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldUsersList.size
    }

    override fun getNewListSize(): Int {
        return mNewUsersList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUsersList[oldItemPosition].id == mNewUsersList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUsersList[oldItemPosition] == mNewUsersList[newItemPosition]
    }
}
