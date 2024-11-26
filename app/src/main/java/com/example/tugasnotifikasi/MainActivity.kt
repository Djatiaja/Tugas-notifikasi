package com.example.tugasnotifikasi

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.tugasnotifikasi.SharedPreference.PrefManager
import com.example.tugasnotifikasi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val usernameCredential = "a"
        val passwordCredential = "a"

        val channel = "test_channel"

        prefManager = PrefManager.getInstance(this)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifId = 90

        setContentView(binding.root)

        with(binding){
            btnLogin.setOnClickListener{
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if(username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Username dan Password tidak boleh kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if (username == usernameCredential && password == passwordCredential){
                    prefManager.saveUsername(username)
                    checkLoginStatus()
                }
                else{
                    Toast.makeText(
                        this@MainActivity,
                        "Username dan Password salah",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            btnLogout.setOnClickListener{
                prefManager.saveUsername("")
                checkLoginStatus()
            }

            btnClear.setOnClickListener{
                prefManager.clear()
                checkLoginStatus()
            }

            btnNotif.setOnClickListener{
                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                } else {
                    0
                }

                val intent = Intent(this@MainActivity, NotifyReceive::class.java)

                val pendingIntent = PendingIntent.getBroadcast(
                    this@MainActivity,
                    0,
                    intent,
                    flag
                )

                val builder = NotificationCompat.Builder(this@MainActivity, channel)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Logout")
                    .setContentText("Logout menggunakan notifikasi!")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(0, "Logout", pendingIntent)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notifChannel = NotificationChannel(channel,
                        "Notifku",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )

                    with(notificationManager) {
                        createNotificationChannel(notifChannel)
                        notify(notifId, builder.build())
                    }

                } else {
                    notificationManager.notify(notifId, builder.build())
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun checkLoginStatus(){
        val isLoggedin = prefManager.getUsername()

        if (isLoggedin.isEmpty()){
            binding.llLogged.visibility = View.GONE
            binding.llLogin.visibility = View.VISIBLE
        }else{
            binding.llLogged.visibility = View.VISIBLE
            binding.llLogin.visibility = View.GONE
        }

    }

    private val logoutReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            checkLoginStatus()
        }
    }

    override fun onStart() {
        super.onStart()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            logoutReceiver,
            IntentFilter("com.example.ACTION_LOGOUT")
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(logoutReceiver)
    }
}