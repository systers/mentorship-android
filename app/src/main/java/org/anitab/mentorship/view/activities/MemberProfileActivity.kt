package org.anitab.mentorship.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_member_profile.*
import org.anitab.mentorship.R
import org.anitab.mentorship.models.User
import org.anitab.mentorship.utils.Constants
import org.anitab.mentorship.utils.setTextViewStartingWithBoldSpan
import org.anitab.mentorship.viewmodels.MemberProfileViewModel
import org.anitab.mentorship.viewmodels.ProfileViewModel

/**
 * This activity will show the public profile of a user of the system
 */
class MemberProfileActivity : BaseActivity() {
    private val memberProfileViewModel: MemberProfileViewModel by viewModels()
    private lateinit var userProfile: User
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_profile)
        supportActionBar?.title = getString(R.string.member_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val profileViewModel: ProfileViewModel by viewModels()
        profileViewModel.successfulGet.observe(this, {
            successful ->
            if (successful != null) {
                if (successful) {
                    setCurrentUser(profileViewModel.user)
                } else {
                    Snackbar.make(getRootView(), profileViewModel.message, Snackbar.LENGTH_LONG)
                            .show()
                }
            }
        })
        profileViewModel.getProfile()

        srlMemberProfile.setOnRefreshListener { fetchNewest() }

        memberProfileViewModel.successful.observe(this, {
            successful ->
            srlMemberProfile.isRefreshing = false
            if (successful != null) {
                if (successful) {
                    setUserProfile(memberProfileViewModel.userProfile)
                } else {
                    Snackbar.make(getRootView(), memberProfileViewModel.message, Snackbar.LENGTH_LONG)
                            .show()
                }
            }
        })

        val memberId = intent.getIntExtra(Constants.MEMBER_USER_ID, -1)

        memberProfileViewModel.userId = memberId

        fetchNewest()

        btnSendRequest.setOnClickListener {
            if (userProfile?.availableToMentor ?: false && !(userProfile?.needMentoring ?:false) &&
                    (currentUser?.availableToMentor ?: false && !(currentUser?.needMentoring ?:false))) {
                Snackbar.make(getRootView(), getString(R.string.both_users_only_available_to_mentor), Snackbar.LENGTH_LONG)
                        .show()
            } else {
                val intent = Intent(this@MemberProfileActivity, SendRequestActivity::class.java)
                intent.putExtra(SendRequestActivity.OTHER_USER_ID_INTENT_EXTRA, userProfile.id)
                intent.putExtra(SendRequestActivity.OTHER_USER_NAME_INTENT_EXTRA, userProfile.name)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_refresh -> {
                fetchNewest()
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
    }

    private fun fetchNewest() {
        srlMemberProfile.isRefreshing = true
        memberProfileViewModel.getUserProfile()
    }

    private fun setCurrentUser(user: User) {
        currentUser = user
    }
    private fun setUserProfile(user: User) {
        userProfile = user
        tvName.text = user.name

        if (user.availableToMentor != null) {
            setTextViewStartingWithBoldSpan(
                    tvAvailableToMentor,
                    getString(R.string.available_to_mentor),
                    if (user.availableToMentor!!)
                        getString(R.string.yes) else getString(R.string.no))
        }
        if (user.needMentoring != null) {
            setTextViewStartingWithBoldSpan(
                    tvNeedMentoring,
                    getString(R.string.need_mentoring),
                    if (user.needMentoring!!)
                        getString(R.string.yes) else getString(R.string.no))
        }
        setTextViewStartingWithBoldSpan(tvBio, getString(R.string.bio), user.bio)
        setTextViewStartingWithBoldSpan(
                tvLocation, getString(R.string.location), user.location)
        setTextViewStartingWithBoldSpan(
                tvOrganization, getString(R.string.organization), user.organization)
        setTextViewStartingWithBoldSpan(
                tvOccupation, getString(R.string.occupation), user.occupation)
        setTextViewStartingWithBoldSpan(
                tvInterests, getString(R.string.interests), user.interests)
        setTextViewStartingWithBoldSpan(
                tvSkills, getString(R.string.skills), user.skills)
        setTextViewStartingWithBoldSpan(
                tvUsername, getString(R.string.username), user.username)
        setTextViewStartingWithBoldSpan(
                tvSlackUsername, getString(R.string.slack_username), user.slackUsername)
        if (!user.availableToMentor!! && !user.needMentoring!!)
            btnSendRequest.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        memberProfileViewModel.successful.removeObservers(this)
        memberProfileViewModel.successful.value = null
    }
}
