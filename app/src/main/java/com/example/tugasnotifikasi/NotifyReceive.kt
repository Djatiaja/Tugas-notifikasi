package com.example.tugasnotifikasi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.tugasnotifikasi.SharedPreference.PrefManager

class NotifyReceive : BroadcastReceiver() {

    override fun onReceive(pcontext: Context, intent: Intent) {
        val prefManager = PrefManager.getInstance(pcontext)
        prefManager.clear()

        val intentLogout = Intent("com.example.ACTION_LOGOUT")

        LocalBroadcastManager.getInstance(pcontext).sendBroadcast(intentLogout)

        Toast.makeText(pcontext, "Berhasil logout", Toast.LENGTH_SHORT).show()

    }
}