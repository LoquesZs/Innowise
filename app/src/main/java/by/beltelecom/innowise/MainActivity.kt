package by.beltelecom.innowise

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.beltelecom.innowise.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

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
}
