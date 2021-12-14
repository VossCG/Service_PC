package com.voss.servicetimer

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.voss.servicetimer.databinding.ActivityMainBinding
import java.util.*

class MyService : Service() {
    private val binder = MyBinder()
    val timer by lazy { Timer() }
    var second: Long = 0L
    private val timerTask by lazy { TimeTask() }
    override fun onCreate() {
        if (second == 0L) {
            second = getSharedPreferences("timeData", MODE_PRIVATE)
                .getLong("second", 0)
        }
        Log.d(TAG, "OnCreat Second:$second")
        super.onCreate()
    }

    inner class MyBinder : Binder() {
        fun getService() = this@MyService
    }

    inner class TimeTask() : TimerTask() {
        override fun run() {
            second++

            Log.d(TAG, "Second:$second")
        }
    }


    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "Service UnBind")
        return super.onUnbind(intent)
    }

    val TAG = MainActivity::class.java.simpleName

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "OnBind Service")
        return binder
    }

    fun runTimes() {
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service Destory")

    }
}