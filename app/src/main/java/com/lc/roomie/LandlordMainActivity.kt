package com.lc.roomie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.lc.roomie.databinding.ActivityMainLandlordBinding

class LandlordMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainLandlordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainLandlordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(LandlordHome())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(LandlordHome())
                    true
                }
                R.id.inbox -> {
                    replaceFragment(LandlordInbox())
                    true
                }
                R.id.menu -> {
                    replaceFragment(LandlordMenu())
                    true
                }
                else -> false
            }
        }



    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}