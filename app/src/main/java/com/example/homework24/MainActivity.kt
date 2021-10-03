package com.example.homework24

import Adapters.MyadapterList
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.homework24.databinding.ActivityMainBinding
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_2.*
import models.Contact

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        myMethod()

    }

    private fun getContacts() {

        val list = getContactsData()

        val adapter1 = MyadapterList(
            this, list,
            object : MyadapterList.Click {
                override fun Call(number: String) {
                    myMethod2(number)
                }

                override fun Sms(number: String, name: String) {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            MainActivity2::class.java
                        ).putExtra("name", name).putExtra("number", number)
                    )
                }
            },
        )

        binding.recycleContactList.adapter = adapter1
    }


    @SuppressLint("Range")
    private fun getContactsData(): ArrayList<Contact> {
        val contactList = ArrayList<Contact>()

        val contactCursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if ((contactCursor?.columnCount ?: 0) > 0) {

            while (contactCursor != null && contactCursor.moveToNext()) {

                val rowId =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))

                val name =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                var phoneNumber = ""

                if (contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    val phoneNumberCursor =
                        contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf<String>(rowId),
                            null
                        )

                    while (phoneNumberCursor!!.moveToNext()) {

                        phoneNumber += phoneNumberCursor.getString(
                            phoneNumberCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        ) + "\n"

                        contactList.add(Contact(name, phoneNumber))
                    }

                    phoneNumberCursor.close()
                }
            }
        }
        contactCursor!!.close()
        return contactList
    }

    private fun myMethod() {
        askPermission(Manifest.permission.READ_CONTACTS) {
            getContacts()
        }.onDeclined { e ->
            if (e.hasDenied()) {
                e.askAgain(); }
            if (e.hasForeverDenied()) {
                Snackbar.make(
                    binding.root,
                    "Endi sozlamalardan dastur Kontaktlarni o'qish uchun uchun ruxsat berishingiz kerak bo'ladi aks xolda kontaktlar dasturda ko'rinmaydi !!",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") { e.goToSettings(); }.show()
            }
        }

    }

    private fun myMethod2(n: String) {
        askPermission(Manifest.permission.CALL_PHONE) {
            startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:$n")))
        }.onDeclined { e ->
            if (e.hasDenied()) {
                e.askAgain(); }
            if (e.hasForeverDenied()) {
                Snackbar.make(
                    binding.root,
                    "Endi sozlamalardan dastur Telefon qilish uchun uchun ruxsat berishingiz kerak bo'ladi aks xolda telefon qila olmaysiz !!",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") { e.goToSettings(); }.show()
            }
        }

    }
}


