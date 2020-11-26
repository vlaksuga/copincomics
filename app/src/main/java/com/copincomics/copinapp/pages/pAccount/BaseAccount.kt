package com.copincomics.copinapp.pages.pAccount

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import com.copincomics.copinapp.data.RetLogin
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.pages.Entry
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseAccount : BaseActivity() {
    private var progressBar: ProgressBar? = null

    fun setProgressBar(bar: ProgressBar) {
        progressBar = bar
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    public override fun onStop() {
        super.onStop()
        hideProgressBar()
    }

    // update user info & restart
    fun updateUserInfo(user: FirebaseUser, type: String = "login") {
        val uid = user.uid
        try {
            user.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idtoken = task.result!!.token!!
                        Log.d(TAG, "BBB firebase id Token : $idtoken")
                        net.accountAPIs.processLoginFirebase(idtoken = idtoken).enqueue(object : Callback<RetLogin> {
                            override fun onResponse(call: Call<RetLogin>, response: Response<RetLogin>) {
                                val head = response.body()!!.head
                                if (head.status.toString() != "error") {
                                    val ret = response.body()!!.body
                                    putAppPreference("isLogin", "true")
                                    putAppPreference("lt", ret.t2)
                                    putAppPreference("t", ret.token)
                                    Log.d(TAG, "Backend auth success")
                                    hideProgressBar()

                                    //  restart
                                    val intent = Intent(applicationContext, Entry::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Log.d(TAG, "Backend auth error ")
                                    Log.d(TAG, "${head.status}")
                                    Log.d(TAG, "${head.msg}")
                                }
                            }

                            override fun onFailure(call: Call<RetLogin>, t: Throwable) {
                                exceptionFlow(t.toString())
                            }
                        })
                    }
                }

        } catch (e: Exception) {
            Log.w(TAG, "Backend Server Authentication:failure")
            Toast.makeText(parent, "Authentication Failed.", Toast.LENGTH_SHORT).show()
        }
    }


}