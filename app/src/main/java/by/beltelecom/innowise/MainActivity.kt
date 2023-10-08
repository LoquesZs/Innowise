package by.beltelecom.innowise

import android.os.Bundle
import android.os.WorkSource
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import by.beltelecom.innowise.common.workmanager.CacheClearWorker
import by.beltelecom.innowise.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding

    private var keepSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplash
            }
        }

        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener(this)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener {
            val currentDestinationID = navController.currentDestination?.id
            when (it.itemId) {
                R.id.home -> {
                    if (currentDestinationID != R.id.HomeFragment) navController.navigate(R.id.action_To_HomeFragment)
                }
                R.id.bookmark -> {
                    if (currentDestinationID != R.id.BookmarkFragment) navController.navigate(R.id.action_To_BookmarksFragment)
                }
            }
            true
        }
    }

    fun removeSplashScreen() {
        if (keepSplash) {
            keepSplash = false
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.DetailFragment -> {
                binding.bottomNavigation.visibility = View.GONE
            }
            R.id.HomeFragment -> {
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.bottomNavigation.selectedItemId = R.id.home
            }
            R.id.BookmarkFragment -> {
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.bottomNavigation.selectedItemId = R.id.bookmark
            }
        }
    }

    override fun onStop() {
        super.onStop()
        val clearCacheRequest = OneTimeWorkRequestBuilder<CacheClearWorker>()
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        WorkManager
            .getInstance(applicationContext)
            .beginUniqueWork("clearing caches", ExistingWorkPolicy.REPLACE, clearCacheRequest)
            .enqueue()
    }
}
