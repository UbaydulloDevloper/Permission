package com.example.homework24

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.homework24.databinding.ActivityMain2Binding
import com.example.homework24.databinding.ActivityMainBinding
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.snackbar.Snackbar

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val number = intent.getStringExtra("number")

        binding.nameOfUser.text = name!!
        binding.numberOfUser.text = number!!

        binding.btn1.setOnClickListener {
            myMethod()
        }

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun sendSMS() {

        val name = intent.getStringExtra("name")
        val number = intent.getStringExtra("number")
        val text = binding.text.text.toString().trim()

        if (text.isNotEmpty()) {
            SmsManager.getDefault().sendTextMessage(number, null, text, null, null)
            Toast.makeText(this, "$name ga xabar jo'natildi", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Xabar yozmadingiz !!", Toast.LENGTH_SHORT).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun myMethod() {

        askPermission(Manifest.permission.SEND_SMS) { sendSMS() }.onDeclined { e ->
            if (e.hasDenied()) {
                e.askAgain(); }
            if (e.hasForeverDenied()) {
                Snackbar.make(
                    binding.root,
                    "Endi sozlamalardan dastur SMS yuborishi uchun uchun ruxsat berishingiz kerak bo'ladi aks xolda sms yubora olmaysiz",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") { e.goToSettings(); }.show()
            }
        }
    }
}

