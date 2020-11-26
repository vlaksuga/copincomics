package com.copincomics.copinapp.pages.pHome

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.copincomics.copinapp.R
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.pages.pEpiList.EpiListActivity
import com.copincomics.copinapp.pages.pMyPage.MyPageActivity
import com.copincomics.copinapp.pages.pSearch.SearchActivity
import kotlinx.android.synthetic.main.footer_home.*
import kotlinx.android.synthetic.main.header_home.*

class HomeActivity : BaseActivity(), FragmentHome.OnMoreSelectedListener {

    val pageState = listOf<String>("home", "genre", "daily", "free", "library")
    var curPage: String = "home"

    private val mainFragment = FragmentHome.newInstance()
    private val genreFragment = FragmentGenre.newInstance()
    private val weekFragment = FragmentWeek.newInstance()
    private val freeFragment = FragmentFree.newInstance()
    private val loginFragment = FragmentLogin.newInstance()
    private val libraryFragment = FragmentLibrary.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
        val intent = intent
        val id = intent.extras?.getString("id")

        TAG = "BBB Home Activity"

        // menu click listener
        header_mymenu.setOnClickListener {
            if (getAppPreference("isLogin") == "true") {
                val intent = Intent(this, MyPageActivity::class.java)
//                this.startActivity(intent)
                startActivityForResult(intent, 100)
            } else {
                switchFragment("login")
            }
        }

        header_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            this.startActivity(intent)
        }


        // Tab click listener
        header_main.setOnClickListener {
            switchFragment(id = "main")
        }
        footer_genre.setOnClickListener {
            switchFragment(id = "genre")
        }
        footer_daily.setOnClickListener {
            switchFragment(id = "daily")
        }
        footer_free.setOnClickListener {
            switchFragment(id = "free")
        }
        footer_library.setOnClickListener {
            switchFragment(id = "library")
        }


        mainFragment.setOnMoreSelectedListener(this)

        if (id != null) {
            switchFragment(id = "home")
            val intentId = Intent(this, EpiListActivity::class.java)
            val bundle = Bundle()
            bundle.putString("id", id)
            intentId.putExtras(bundle)
            this.startActivity(intentId)
        } else {
            // init fragment home
            switchFragment(id = "home")
        }


    }

    fun switchFragment(id: String) {
        Log.d(TAG, "fragment selected : $id")
        val ft = supportFragmentManager.beginTransaction()


        when (id) {
            "home" -> {
                resetFooter()
                curPage = id
                ft.replace(R.id.contents_frame, mainFragment)
            }
            "genre" -> {
                resetFooter()
                curPage = id
                ft.replace(R.id.contents_frame, genreFragment)
                footer_genre.setImageResource(R.mipmap.btn_genres_on)
            }
            "daily" -> {
                resetFooter()
                curPage = id
                ft.replace(R.id.contents_frame, weekFragment)
                footer_daily.setImageResource(R.mipmap.btn_daily_on)

            }
            "free" -> {
                resetFooter()
                curPage = id
                ft.replace(R.id.contents_frame, freeFragment)
                footer_free.setImageResource(R.mipmap.btn_free_on)
            }
            "library" -> {
                if (getAppPreference("isLogin") == "true") {
                    resetFooter()
                    curPage = id
                    ft.replace(R.id.contents_frame, libraryFragment)
                    footer_library.setImageResource(R.mipmap.btn_library_on)
                } else {
                    switchFragment("login")
                }
            }
            "login" -> {
                resetFooter()
                ft.replace(R.id.contents_frame, loginFragment)
            }
            else -> {
                resetFooter()
                curPage = id
                ft.replace(R.id.contents_frame, mainFragment)
            }
        }
//        ft.addToBackStack(null)
        ft.commit()
    }

    fun resetFooter() {
        footer_genre.setImageResource(R.mipmap.btn_genres_off)
        footer_daily.setImageResource(R.mipmap.btn_daily_off)
        footer_free.setImageResource(R.mipmap.btn_free_off)
        footer_library.setImageResource(R.mipmap.btn_library_off)
    }

//    override fun onNewIntent(intent: Intent?) {
//        val id = intent?.extras?.getString("openAcitivity")
//        if (id != null) {
//            if (id == "coinshop") {
//                startActivity(Intent(this, PayActivity::class.java))
//            }
//        } else {
//            super.onNewIntent(intent)
//        }
//    }

    override fun onBackPressed() {
        Log.d(TAG, "cur page :  $curPage")
        if (curPage != "login") {
            val builder = AlertDialog.Builder(this@HomeActivity, R.style.AlertDialogCustom)
            builder.setTitle("")
            builder.setMessage("Do you really want to quit?")


            builder.setPositiveButton(
                "Yes"
            ) { dialog, which ->
                super.onBackPressed()
            }

            builder.setNegativeButton(
                "No"
            ) { dialog, which ->
            }

            builder.show()
        } else {
            switchFragment(id = curPage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data?.getStringExtra("result") == "goLibrary") {
                    switchFragment("library")
                }
            }
        }
    }

    override fun onSelected(position: String) {
        switchFragment(position)
    }
}