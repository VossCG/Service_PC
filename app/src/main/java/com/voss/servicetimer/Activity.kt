package com.voss.servicetimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.voss.servicetimer.databinding.ActivityBinding
import java.sql.Time
import java.util.*

class Activity : AppCompatActivity() {
    private val timer = Timer()
    var second = 0L
    private val TAG = Activity::class.java.simpleName
    private lateinit var binding: ActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        timer = Timer()
        if (savedInstanceState != null) {
            second = savedInstanceState.getLong("time", 0)
            binding.timeText.text = second.toString()
        }
        Log.d(TAG, "OnCreat")
        setButton()
    }

    private fun setButton() {
        binding.button.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage("是否跳轉")
                .setPositiveButton("Yes") { which, dialog ->
                    startActivity(Intent(this, MainActivity::class.java))
                }.setNegativeButton("No", null)
                .show()
        }
        binding.timeBut.setOnClickListener {
            setTimerRunTask()
        }
    }

    private fun setTimerRunTask() {
        val timerTask = object : TimerTask() {
            override fun run() {
                second++
                runOnUiThread {
                    binding.timeText.text = second.toString()
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "OnStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "OnResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "OnPasue")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("time", second)
        Log.d(TAG, "onSaveInstanceState second:$second")

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "OnStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "OnReStart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestory")
        timer.cancel()
    }
}