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
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.voss.servicetimer.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import java.sql.Connection
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var viewModel: TimeViewModel
    private var isBind: Boolean = false
    private lateinit var myService: MyService
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private lateinit var timeTextView: TextView
    private lateinit var serviceIntent: Intent
    private lateinit var binding: ActivityMainBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private var sec = 0L
    private var min = 0L

    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "OnCreat")
        //---- bind layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //---- bind layout
        setViewModel()
        // service intent
        serviceIntent = Intent(this, MyService::class.java)
        // service intent

        bindItem()
        setTimeOnClick()
        handler = Handler(Looper.getMainLooper())
        setRunnable()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(TimeViewModel::class.java)
        viewModel.secondLiveData.observe(this, androidx.lifecycle.Observer {
            min = it / 60
            sec = it % 60
            timeTextView.text = String.format(Locale.getDefault(), "%02d:%02d", min, sec)
        })
    }


    private fun setRunnable() {
        runnable = object : Runnable {
            override fun run() {
                handler.postDelayed(runnable, 500)
                viewModel.secondLiveData.postValue(myService.second)

            }
        }
    }

    private fun bindItem() {
        startButton = binding.startButton
        stopButton = binding.stopButton
        resetButton = binding.resetButton
        timeTextView = binding.timeView

    }

    private fun setTimeOnClick() {
        //---  start button ---
        startButton.setOnClickListener {
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
            handler.postDelayed(runnable, 1000)
        }
        //---  start button ---

        //---  stop button ---
        stopButton.setOnClickListener {
            if (isBind) {
                unbindService(connection)
                myService.timer.cancel()
                getSharedPreferences("timeData", MODE_PRIVATE)
                    .edit()
                    .putLong("second", myService.second)
                    .apply()
                isBind = false
                handler.removeCallbacks(runnable)
                Log.d(TAG, "Remove callback")
            }
        }
        //---  stop button ---

        resetButton.setOnClickListener {
            if (isBind) {
                myService.second = 0
                getSharedPreferences("timeData", MODE_PRIVATE)
                    .edit()
                    .putLong("second", myService.second)
                    .apply()
                unbindService(connection)
                myService.timer.cancel()
                viewModel.secondLiveData.postValue(myService.second)
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d(TAG, "Service Connected")
            val binder = service as MyService.MyBinder
            myService = binder.getService()
            myService.runTimes()
            isBind = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d(TAG, "Service Disconnected")
            isBind = false
        }
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
        myService.second = 0
        getSharedPreferences("timeData", MODE_PRIVATE)
            .edit()
            .putLong("second", myService.second)
            .apply()
        Log.d(TAG, "OnDestory")
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
}