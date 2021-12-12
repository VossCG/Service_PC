package com.voss.servicetimer

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

class MyService : Service() {
    private val binder = MyBinder()
    private val timer by lazy { Timer() }
    var second: Long = 0L
    private val timerTask by lazy { TimeTask() }

    inner class MyBinder : Binder() {
        fun getService() = this@MyService
    }

    inner class TimeTask : TimerTask() {
        override fun run() {
            second++
            Log.d(TAG, "Second:$second")
        }
    }

    override fun unbindService(conn: ServiceConnection) {
        timer.cancel()
        Log.d(TAG, "Service UnBind $conn")
        super.unbindService(conn)
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