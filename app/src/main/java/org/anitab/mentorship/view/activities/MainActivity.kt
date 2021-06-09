package org.anitab.mentorship.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.anitab.mentorship.R
import org.anitab.mentorship.utils.PreferenceManager
import org.anitab.mentorship.view.fragments.HomeFragment
import org.anitab.mentorship.view.fragments.MembersFragment
import org.anitab.mentorship.view.fragments.ProfileFragment
import org.anitab.mentorship.view.fragments.RelationPagerFragment
import org.anitab.mentorship.view.fragments.RequestsFragment

/**
 * This activity has the bottom navigation which allows the user to switch between fragments
 */
class MainActivity : BaseActivity() {

    private var atHome = true

    private val TOAST_DURATION = 4000
    private var mLastPress: Long = 0
    private lateinit var exitToast: Toast

    private val preferenceManager: PreferenceManager = PreferenceManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.setOnNavigationItemReselectedListener { }

        if (savedInstanceState == null) {
            showHomeFragment()
        } else {
            atHome = savedInstanceState.getBoolean("atHome")
        }
    }

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        replaceFragment(R.id.contentFrame, HomeFragment.newInstance(),
                                R.string.fragment_title_home)
                        atHome = true
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_profile -> {
                        replaceFragment(R.id.contentFrame, ProfileFragment.newInstance(),
                                R.string.fragment_title_profile)
                        atHome = false
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_relation -> {
                        replaceFragment(R.id.contentFrame, RelationPagerFragment.newInstance(),
                                R.string.fragment_title_relation)
                        atHome = false
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_members -> {
                        replaceFragment(R.id.contentFrame, MembersFragment.newInstance(),
                                R.string.fragment_title_members)
                        atHome = false
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_requests -> {
                        replaceFragment(R.id.contentFrame, RequestsFragment.newInstance(),
                                R.string.fragment_title_requests)
                        atHome = false
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }

    private fun showHomeFragment() {
        atHome = true
        bottomNavigation.selectedItemId = R.id.navigation_home
        replaceFragment(R.id.contentFrame, HomeFragment.newInstance(), R.string.fragment_title_home)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("atHome", atHome)
    }

    private fun showToast() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastPress > TOAST_DURATION) {
            exitToast = Toast.makeText(baseContext, getString(R.string.exit_toast), Toast.LENGTH_LONG)
            exitToast.show()
            mLastPress = currentTime
        } else {
            exitToast.cancel() // Hide toast on App exit
            super.onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (!atHome) {
            showHomeFragment()
        } else {
            showToast()
        }
    }
}
