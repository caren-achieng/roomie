package com.lc.roomie

import android.R
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.ui.AppBarConfiguration
import com.lc.roomie.databinding.ActivityAddListingBinding
import kotlinx.android.synthetic.main.activity_add_listing.*
import kotlinx.android.synthetic.main.fragment_building_type.*


class AddListingActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddListingBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityAddListingBinding.inflate(layoutInflater)
//        val animDrawable = binding.collapsingToolbar.background as AnimationDrawable
        val animDrawable = binding.rootLayout.background as AnimationDrawable

        animDrawable.setEnterFadeDuration(10)
        animDrawable.setExitFadeDuration(2500)
        animDrawable.start()

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(binding.rootLayout.id, BuildingType()).commit()
        binding.navButtons.bringToFront()
        setContentView(binding.root)


    }

//    override fun onSupportNavigateUp(): Boolean {
////        val navController = findNavController(R.id.nav_host_fragment_content_add_listing)
////        return navController.navigateUp(appBarConfiguration)
////                || super.onSupportNavigateUp()
//    }

//    private fun replaceFragment(fragment: Fragment){
//        binding = ActivityAddListingBinding.inflate(layoutInflater)
//
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
//        fragmentTransaction.replace(binding.fragmentContainer.id, fragment)
//        fragmentTransaction.commit()
//    }
}