package com.lc.roomie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        val animation = AnimationUtils.loadAnimation( this, R.anim.logoanimation)

        logo.startAnimation(animation)

        val splashScreenTimeout=4000
        val homeIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(homeIntent)
            finish()
        }, splashScreenTimeout.toLong())
    }
}