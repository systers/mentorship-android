package org.systers.mentorship.view.fragments

import android.app.Activity.RESULT_OK
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_members.*
import org.systers.mentorship.R
import org.systers.mentorship.utils.Constants
import org.systers.mentorship.utils.Constants.FILTER_MAP
import org.systers.mentorship.utils.Constants.FILTER_REQUEST_CODE
import org.systers.mentorship.utils.Constants.SORT_KEY
import org.systers.mentorship.view.activities.FilterActivity
import org.systers.mentorship.view.activities.MainActivity
import org.systers.mentorship.view.activities.MemberProfileActivity
import org.systers.mentorship.view.adapters.MembersAdapter
import org.systers.mentorship.viewmodels.MembersViewModel

/**
 * The fragment is responsible for showing all the members of the system in a list format
 */
class MembersFragment : BaseFragment() {

    companion object {
        /**
         * Creates an instance of [MembersFragment]
         */
        fun newInstance() = MembersFragment()
    }

    private lateinit var membersViewModel: MembersViewModel
    private lateinit var rvAdapter: MembersAdapter
    private var filterMap = hashMapOf(SORT_KEY to SortValues.REGISTRATION_DATE.name)

    override fun getLayoutResourceId(): Int = R.layout.fragment_members

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        rvAdapter = MembersAdapter(listOf(), openUserProfile)

        membersViewModel = ViewModelProviders.of(this).get(MembersViewModel::class.java)
        membersViewModel.successful.observe(this, Observer { successful ->
            (activity as MainActivity).hideProgressDialog()
            if (successful != null) {
                if (successful) {
                    if (membersViewModel.userList.isEmpty()) {
                        tvEmptyList.text = getString(R.string.empty_members_list)
                        rvMembers.visibility = View.GONE
                    } else {
                        rvMembers.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = rvAdapter
                        }
                        tvEmptyList.visibility = View.GONE
                    }
                    rvAdapter.setData(membersViewModel.userList)
                    rvAdapter.filter(filterMap)
                } else {
                    view?.let {
                        Snackbar.make(it, membersViewModel.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })

        (activity as MainActivity).showProgressDialog(getString(R.string.fetching_users))
        membersViewModel.getUsers()
    }

    private val openUserProfile: (Int) -> Unit =
            { memberId ->
                val intent = Intent(activity, MemberProfileActivity::class.java)
                intent.putExtra(Constants.MEMBER_USER_ID, memberId)
                startActivity(intent)
            }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_members, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val intent = Intent(context, FilterActivity::class.java)
                intent.putExtra(FILTER_MAP, filterMap)
                startActivityForResult(intent, FILTER_REQUEST_CODE)
                activity?.overridePendingTransition(
                        R.anim.anim_slide_from_bottom,
                        R.anim.anim_stay)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            filterMap = data?.extras?.get(FILTER_MAP) as HashMap<String, String>?
                    ?: hashMapOf(SORT_KEY to SortValues.REGISTRATION_DATE.name)
            rvAdapter.filter(filterMap)
        }
    }

    enum class SortValues {
        NAMEAZ,
        NAMEZA,
        REGISTRATION_DATE
    }

}
