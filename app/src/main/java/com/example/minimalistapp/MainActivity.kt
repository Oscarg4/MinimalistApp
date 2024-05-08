package com.example.minimalistapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val addFragment = AddFragment()
    private val perfilFragment = PerfilFragment()
    private val aboutUsFragment = AboutUsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    setCurrentFragment(homeFragment)
                    true
                }
                R.id.nav_search -> {
                    setCurrentFragment(searchFragment)
                    true
                }
                R.id.nav_add -> {
                    setCurrentFragment(addFragment)
                    true
                }
                R.id.nav_users -> {
                    setCurrentFragment(perfilFragment)
                    true
                }
                R.id.nav_aboutUs -> {
                    setCurrentFragment(aboutUsFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.ContainerView, fragment)
            commit()
        }
    }
}
