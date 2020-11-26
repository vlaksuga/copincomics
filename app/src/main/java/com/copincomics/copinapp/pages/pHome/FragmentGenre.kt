package com.copincomics.copinapp.pages.pHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.BannerAdapter
import com.copincomics.copinapp.adapters.ToonListAdapter
import com.copincomics.copinapp.data.PageContents
import com.copincomics.copinapp.pages.BaseFragment
import com.copincomics.copinapp.tools.GridSpacingItemDecoration
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentGenre : BaseFragment() {
    lateinit var tabs: TabLayout
    lateinit var banner: ViewPager2
    lateinit var banner_indicator: TabLayout
    lateinit var rv: RecyclerView
    var curposition: Int = 0

    val genreList =
        listOf<String>(
            "Romance",
            "BL",
            "Action",
            "Comedy",
            "Fantasy",
            "GL",
            "Drama",
        )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tab_banner_rv, container, false)
        init(view)
        TAG = "BBB Genre"

        tabs = view.findViewById(R.id.tabs)
        banner = view.findViewById(R.id.banner)
        banner_indicator = view.findViewById(R.id.banner_indicator)
        rv = view.findViewById(R.id.rv)

        // setting Tab
        tabs.apply {
//            removeAllTabs()
            addTab(newTab().setText("Romance"))
            addTab(newTab().setText("BL"))
            addTab(newTab().setText("Action"))
            addTab(newTab().setText("Comedy"))
            addTab(newTab().setText("Fantasy"))
            addTab(newTab().setText("GL"))
            addTab(newTab().setText("Drama"))
        }
        tabs.tabMode = TabLayout.MODE_AUTO

        banner.layoutParams.height = (DP * 150).toInt()

        rv.layoutManager = GridLayoutManager(activity, 2)
        rv.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                (18 * DP).toInt(),
                (15 * DP).toInt(),
                (16 * DP).toInt()
            )
        )


        ///////////////////
        //// data binding
        ///////////////////
//        tabs.getTabAt(1)!!.select()
       tabs.getTabAt(1)!!.select()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                try {
                    curposition = tab.position
                    update()

                } catch (e: Exception) {
                    exceptionFlow(e.toString())
                }
            }
        }
        )

        tabs.getTabAt(0)!!.select()




        return view
    }

    override fun onResume() {
        super.onResume()
        update()
    }


    fun update() {
//        showProgressbar()
        dialog.show()
//        net.dispatcher.cancelAll()
        try {
            net.serviceAPIs.genre(genreList[curposition])
                .enqueue(object : Callback<PageContents> {
                    override fun onResponse(call: Call<PageContents>, response: Response<PageContents>) {
                        val ret = response.body()!!.body
                        banner.adapter = BannerAdapter(ret.banner, context!!, DP, 150, 360)
                        rv.adapter = ToonListAdapter(R.layout.section_a, ret.list, context!!, DP, 154, 154, "all", "1x1")
                        TabLayoutMediator(banner_indicator, banner) { _, _ -> }.attach()
                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<PageContents>, t: Throwable) {
                        exceptionFlow(t.toString())
                    }
                }
                )
        } catch (e: Exception) {
            dialog.dismiss()
            exceptionFlow(e.toString())
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentGenre()
    }
}