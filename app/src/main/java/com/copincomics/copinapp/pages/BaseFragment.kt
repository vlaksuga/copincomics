package com.copincomics.copinapp.pages

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.copincomics.copinapp.R
import com.copincomics.copinapp.apis.RequestFactory


open class BaseFragment : Fragment() {
    var TAG = "BBB fragment"
    var DP: Float = 0.0f
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var net: RequestFactory
    var progressbar: ProgressBar? = null
    lateinit var dialog: AlertDialog

    fun init(v: View?) {
        sharedPreferences = activity!!.applicationContext.getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        DP = sharedPreferences.getFloat("DP", 360F)
        net = RequestFactory(sharedPreferences)


        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
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


    fun exceptionFlow(e: String) {
        Log.d(TAG, e)
        Toast.makeText(context, "Network Error! Try Again Please!", Toast.LENGTH_SHORT)
            .show()
    }

    fun showToast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show()
    }


}