package com.copincomics.copinapp.pages.pAccount

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.pages.dialogs.WarnDiag
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_change_p_w.*
import kotlinx.android.synthetic.main.merge_top_title_with_finishbtn.*

class ChangePWActivity : BaseActivity() {

    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_p_w)
        init()
        btn_finish.setOnClickListener { finish() }
        findViewById<TextView>(R.id.title).text = "Change Password"
        TAG = "BBB Change pw"
        edt_cur_pw
        edt_next_pw
        edt_next_pw2
        btn_ch_pw


        btn_ch_pw.setOnClickListener {
            updatePw()
        }
    }


    fun updatePw() {
        val email = user?.email
        if ((user == null) or (email == null)) {
            showToast("Error! Please Login again!")
            finish()
        }

        if (edt_next_pw.text.toString() != edt_next_pw2.text.toString()) {
            WarnDiag().build(this, "Please enter valid password").show()

        } else if ((edt_next_pw.text.toString() == "") or (edt_next_pw2.text.toString() == "") or (edt_cur_pw.text.toString() == "")) {
            WarnDiag().build(this, "Please enter valid password").show()
        } else {
            val credential = EmailAuthProvider
                .getCredential(email!!, edt_cur_pw.text.toString())

            user!!.reauthenticate(credential)
                .addOnSuccessListener(this, OnSuccessListener {
                    Log.d(TAG, " Success")
                    user.updatePassword(edt_next_pw.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d(TAG, "User password updated.")
                                showToast("User password updated.")
                                finish()
                            } else {
                                showToast("User password updated.")
                                WarnDiag().build(this, "Network connection error. Try again please").show()
                            }
                        }
                })
                .addOnFailureListener(this, OnFailureListener {
                    Log.d(TAG, " Failure")
                    WarnDiag().build(this, "Please enter valid password").show()
                })
        }
    }
}



