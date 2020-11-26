package com.copincomics.copinapp.pages.pAccount

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.pages.dialogs.WarnDiag
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_findpw.*
import kotlinx.android.synthetic.main.merge_top_title_with_finishbtn.*

class FindpwActivity : BaseAccount() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findpw)
        init()
        btn_finish.setOnClickListener { finish() }
        findViewById<TextView>(R.id.title).text = "Find Password"
        TAG = "BBB find pw"
        edt_email.text
        btn_change_pw
        btn_change_pw.setOnClickListener {
            findpw()
        }
    }

    fun findpw() {
        var valid = false
        val email = edt_email.text.toString()

        if (TextUtils.isEmpty(email)) {
            WarnDiag().build(this, "Invalid Email Format.").show()
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            WarnDiag().build(this, "Invalid Email Format.").show()
            valid = false
        } else {
            valid = true
        }




        if (!valid) {
            return
        } else {
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        WarnDiag().build(
                            this, "E-mail has been sent.\n" +
                                    "If you can’t see the mail,\n" +
                                    "Please check your spam(junk) mailbox."
                        ).show()

                    } else {
                        Log.d(TAG, "Error Email sent.")
                        WarnDiag().build(this, "Fail to find password.\n" + "Please check your email again please!").show()
//                        finish()
                    }
                }


        }

    }
}