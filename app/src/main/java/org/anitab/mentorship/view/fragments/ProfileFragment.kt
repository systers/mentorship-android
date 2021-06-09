package org.anitab.mentorship.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.*
import org.anitab.mentorship.R
import org.anitab.mentorship.databinding.FragmentProfileBinding
import org.anitab.mentorship.viewmodels.ProfileViewModel

/**
 * The fragment is responsible for showing the User's profile
 */
class ProfileFragment : BaseFragment() {

    companion object {
        /**
         * Creates an instance of ProfileFragment
         */
        fun newInstance() = ProfileFragment()
        val TAG: String = ProfileFragment::class.java.simpleName
    }

    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun getLayoutResourceId(): Int = R.layout.fragment_profile

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentProfileBinding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
        return fragmentProfileBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        srlProfile.setOnRefreshListener { fetchNewest() }

        profileViewModel.successfulGet.observe(viewLifecycleOwner, {
            successful ->
            srlProfile.isRefreshing = false
            if (successful != null) {
                if (successful) {
                    fragmentProfileBinding.user = profileViewModel.user
                } else {
                    Snackbar.make(fragmentProfileBinding.root, profileViewModel.message,
                            Snackbar.LENGTH_LONG).show()
                }
            }
        })
        fetchNewest()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_my_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit_profile -> {
                if (fragmentProfileBinding.user != null) {
                    val editProfileFragment: EditProfileFragment = EditProfileFragment.newInstance(profileViewModel.user)
                    editProfileFragment.setOnDismissListener {
                        fetchNewest()
                    }
                    fragmentManager?.let { editProfileFragment.show(it, getString(R.string.fragment_title_edit_profile)) }
                }
                true
            }
            R.id.menu_refresh -> {
                fetchNewest()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchNewest() {
        srlProfile.isRefreshing = true
        profileViewModel.getProfile()
    }

    override fun onDestroy() {
        super.onDestroy()

        profileViewModel.successfulGet.removeObservers(requireActivity())
        profileViewModel.successfulGet.value = null
    }
}
