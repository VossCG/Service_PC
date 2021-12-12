package com.voss.servicetimer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.sql.Connection
import java.util.*

class MainActivity : AppCompatActivity() {
    private var myService: MyService? = null
    private var isBind: Boolean = false
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private lateinit var timeTextView: TextView
    private lateinit var serviceIntent: Intent


    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        serviceIntent = Intent(this, MyService::class.java)
        findItem()
        setTimeOnClick()
    }

    private fun findItem() {
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.reset_Button)
        timeTextView = findViewById(R.id.timeView)

    }



    private fun setTimeOnClick() {
        startButton.setOnClickListener {
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
            Toast.makeText(this, "BindService", Toast.LENGTH_SHORT).show()
        }
        stopButton.setOnClickListener {
            if (isBind) {
                unbindService(connection)
                Toast.makeText(this, "StopService", Toast.LENGTH_SHORT).show()
                isBind = false
            }
        }
        resetButton.setOnClickListener {
            unbindService(connection)
            myService?.second = 0
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d(TAG, "Service Connected")
            val binder = service as MyService.MyBinder
            myService = binder.getService()
            myService!!.runTimes()
            isBind = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d(TAG, "Service Disconnected")
            isBind = false
        }
    }


}