package com.copincomics.copinapp.pages

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.copincomics.copinapp.R
import com.copincomics.copinapp.apis.RequestFactory

open class BaseActivity : AppCompatActivity() {

    var TAG = "BBB"
    lateinit var sharedPreferences: SharedPreferences
    var DP: Float = 0.0f
    lateinit var net: RequestFactory
    lateinit var dialog: AlertDialog


    fun init() {
//        sharedPreferences = this.getSharedPreferences(
//            getString(R.string.preference_file_key),
//            Context.MODE_PRIVATE
//        )

        sharedPreferences = applicationContext.getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )


        DP = sharedPreferences.getFloat("DP", 1F)
        net = RequestFactory(sharedPreferences)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false) // if you want user to wait for some process to finish,

        builder.setView(R.layout.dialog_loading)
        dialog = builder.create()

    }

    fun putAppPreference(k: String, v: String) {
        with(sharedPreferences.edit()) {
            putString(k, v)
            commit()
        }
    }

    fun getAppPreference(k: String): String {
        return sharedPreferences.getString(k, "")!!
    }

    open fun exceptionFlow(e: String) {
        Log.d(TAG, e)
        Toast.makeText(this, "Network Error! Try Again Please!", Toast.LENGTH_SHORT)
            .show()
    }

    fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    fun log(s: String) {
        Log.d(TAG, s)
    }
}