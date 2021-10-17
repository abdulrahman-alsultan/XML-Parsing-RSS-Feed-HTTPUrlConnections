package com.example.xmlparsingrssfeedhttpurlconnections

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var timeLeft = 1500L
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        startTime()
    }

    private fun startTime(){
        countDownTimer = object : CountDownTimer(timeLeft, 1000){
            override fun onTick(p0: Long) {
                timeLeft = p0
            }

            override fun onFinish() {
                startActivity(Intent(this@MainActivity, DisplayData::class.java))
                finish()
            }
        }.start()
    }
}