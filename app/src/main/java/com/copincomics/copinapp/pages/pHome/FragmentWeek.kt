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
import java.util.*

class FragmentWeek : BaseFragment() {
    lateinit var tabs: TabLayout
    lateinit var banner: ViewPager2
    lateinit var banner_indicator: TabLayout
    lateinit var rv: RecyclerView

    val weekdayList =
        listOf<String>(
            "sun",
            "mon",
            "tue",
            "wed",
            "thr",
            "fri",
            "sat",
            "term10",
            "all"
        )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tab_banner_rv, container, false)
        init(view)
        TAG = "BBB Week"

        tabs = view.findViewById(R.id.tabs)
        banner = view.findViewById(R.id.banner)
        banner_indicator = view.findViewById(R.id.banner_indicator)
        rv = view.findViewById(R.id.rv)

        // setting Tab
        tabs.apply {
//            removeAllTabs()
            addTab(newTab().setText("Sun"))
            addTab(newTab().setText("Mon"))
            addTab(newTab().setText("Tue"))
            addTab(newTab().setText("Wed"))
            addTab(newTab().setText("Thu"))
            addTab(newTab().setText("Fri"))
            addTab(newTab().setText("Sat"))
            addTab(newTab().setText("10 Days"))
            addTab(newTab().setText("Complete"))
        }
        tabs.tabMode = TabLayout.MODE_AUTO


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
        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        tabs.getTabAt(day)!!.select()
        //// 요일
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                try {
                    val position = tab.position
                    update(position)

                } catch (e: Exception) {
                    exceptionFlow(e.toString())
                }
            }
        }
        )

        tabs.getTabAt(day - 1)!!.select()



        return view
    }

    fun update(position: Int) {
//        showProgressbar()
        dialog.show()
//        net.dispatcher.cancelAll()
        try {
            net.serviceAPIs.week(weekdayList[position])
                .enqueue(object : Callback<PageContents> {
                    override fun onResponse(call: Call<PageContents>, response: Response<PageContents>) {
                        val ret = response.body()!!.body
                        banner.adapter = BannerAdapter(ret.banner, context!!, DP, 150, 360)
                        rv.adapter = ToonListAdapter(R.layout.section_a, ret.list, context!!, DP, 154, 154, "all", "1x1")
                        TabLayoutMediator(banner_indicator, banner) { _, _ -> }.attach()
                        banner.layoutParams.height = (DP * 150).toInt()

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
        fun newInstance() = FragmentWeek()
    }
}