package org.systers.mentorship.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import org.systers.mentorship.R
import org.systers.mentorship.databinding.FragmentHomeBinding
import org.systers.mentorship.view.adapters.AchievementsAdapter
import org.systers.mentorship.viewmodels.HomeViewModel

/**
 * The fragment is responsible for showing a welcoming message and show some statistics of the User
 * usage of the app
 */
class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var achievementsAdapter: AchievementsAdapter

    companion object {
        /**
         * Creates an instance of HomeFragment
         */
        fun newInstance() = HomeFragment()
        val TAG: String = HomeFragment::class.java.simpleName
    }

    override fun getLayoutResourceId(): Int = R.layout.fragment_home
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        achievementsAdapter = AchievementsAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        val divider = DividerItemDecoration(context, linearLayoutManager.orientation)

        rvAchievements.apply {
            adapter = achievementsAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(divider)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        with(homeViewModel) {
            successful.observe(viewLifecycleOwner, Observer { successful ->
                if (successful == true) {
                    binding.stats = homeViewModel.homeStats
                    if (homeViewModel.homeStats.achievements.isEmpty()) {
                        tvNoAchievements.visibility = View.VISIBLE
                        rvAchievements.visibility = View.GONE

                    } else {
                        tvNoAchievements.visibility = View.GONE
                        rvAchievements.visibility = View.VISIBLE
                        achievementsAdapter.submitList(homeViewModel.homeStats.achievements)
                    }
                } else if (!successful) {
                    view?.let {
                        Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            })
        }
        homeViewModel.getHomeStats()
    }
}

