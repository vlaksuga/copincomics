package com.copincomics.copinapp.pages.pHome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.pages.BaseFragment
import com.copincomics.copinapp.pages.pAccount.LoginActivity

class FragmentLogin : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        init(view)
        TAG = "BBB login"

        val btn_login = view.findViewById<ImageView>(R.id.btn_login)

        btn_login.setOnClickListener {
//            putAppPreference("isLogin", "true")
//            val intent = Intent(view.context, LoginActivity::class.java)
            val intent = Intent(view.context, LoginActivity::class.java)
            this.startActivity(intent)
        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentLogin()
    }
}