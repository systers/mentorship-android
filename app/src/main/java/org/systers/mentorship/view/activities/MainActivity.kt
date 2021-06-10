package org.systers.mentorship.view.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.systers.mentorship.R
import org.systers.mentorship.utils.PreferenceManager

/**
 * This activity has the bottom navigation which allows the user to switch between fragments
 */
class MainActivity : BaseActivity() {

    private val TOAST_DURATION = 4000
    private var mLastPress: Long = 0
    private lateinit var exitToast: Toast

    private val preferenceManager: PreferenceManager = PreferenceManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        bottomNavigation.setOnNavigationItemReselectedListener { }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.contentFrame) as NavHostFragment
        val navController = navHostFragment.navController
        // Specify top-level destinations
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_profile,
                R.id.navigation_relation,
                R.id.navigation_members,
                R.id.navigation_requests
            )
        )
        // Setup toolbar to automatically show title based on label specified in nav graph
        collapsingToolbarLayout.setupWithNavController(toolbar, navController, appBarConfiguration)
        // Setup bottom nav to automatically handle navigation based on nav graph and bottom nav menu
        bottomNavigation.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.navigation_home) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - mLastPress > TOAST_DURATION) {
                        exitToast = Toast.makeText(
                            baseContext,
                            getString(R.string.exit_toast),
                            Toast.LENGTH_LONG
                        )
                        exitToast.show()
                        mLastPress = currentTime
                    } else {
                        exitToast.cancel() // Hide toast on App exit
                        isEnabled = false
                        onBackPressed()
                    }
                } else {
                    isEnabled = false
                    onBackPressed()
                    isEnabled = true
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.contentFrame)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}
