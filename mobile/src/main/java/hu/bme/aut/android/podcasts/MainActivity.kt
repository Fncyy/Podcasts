package hu.bme.aut.android.podcasts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.ui.favourites.FavouritesFragment
import hu.bme.aut.android.podcasts.ui.home.HomeFragment
import hu.bme.aut.android.podcasts.ui.menu.MenuFragment
import hu.bme.aut.android.podcasts.ui.search.SearchFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.navView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    val fragment = HomeFragment()
                    add(fragment)
                    setStack(fragment)
                }
                R.id.navigation_search -> {
                    val fragment = SearchFragment()
                    add(fragment)
                    setStack(HomeFragment(), fragment)
                }
                R.id.navigation_favourites -> {
                    val fragment = FavouritesFragment()
                    add(fragment)
                    setStack(HomeFragment(), fragment)
                }
                R.id.navigation_menu -> {
                    val fragment = MenuFragment()
                    add(fragment)
                    setStack(HomeFragment(), fragment)
                }
            }
            true
        }
        navView.setOnNavigationItemReselectedListener {}
    }

    private fun add(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navHostFragment, fragment, fragment.tag)
            .commit()
    }

    private fun setStack(vararg fragments: Fragment) {
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragments.forEach(this::add)
    }
}
