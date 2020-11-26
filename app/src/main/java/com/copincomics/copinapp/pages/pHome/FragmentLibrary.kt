package com.copincomics.copinapp.pages.pHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.ToonListAdapter
import com.copincomics.copinapp.data.MyLibrary
import com.copincomics.copinapp.data.WebtoonItem
import com.copincomics.copinapp.pages.BaseFragment
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentLibrary : BaseFragment() {
    lateinit var tabs: TabLayout
    lateinit var banner: ViewPager2
    lateinit var banner_indicator: TabLayout
    lateinit var rv: RecyclerView
    var curposition: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tab_banner_rv, container, false)
        init(view)
        TAG = "BBB FragmentLibrary"

        tabs = view.findViewById(R.id.tabs)
        banner = view.findViewById(R.id.banner)
        banner_indicator = view.findViewById(R.id.banner_indicator)
        banner.visibility = View.GONE
        banner_indicator.visibility = View.GONE
        rv = view.findViewById(R.id.rv)

        // setting Tab
        tabs.apply {
//            removeAllTabs()
            addTab(newTab().setText("Purchases"))
            addTab(newTab().setText("Subscribe"))
            addTab(newTab().setText("Recent"))
        }
        tabs.tabMode = TabLayout.MODE_FIXED

        rv.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        // 구분선 넣기
        val itemDecorator =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.simple_divider)!!)
        rv.addItemDecoration(itemDecorator)

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
            net.myAPIs.library()
                .enqueue(object : Callback<MyLibrary> {
                    override fun onResponse(call: Call<MyLibrary>, response: Response<MyLibrary>) {
                        val ret = response.body()!!.body
                        var data: List<WebtoonItem>? = null
                        data = when (curposition) {
                            0 -> {
                                ret.ownedlist
                            }
                            1 -> {
                                ret.likelist
                            }
                            else -> {
                                ret.recentlist
                            }
                        }
                        rv.adapter = ToonListAdapter(R.layout.section_b, data, context!!, DP, 94, 94, "none", "1x1")
                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<MyLibrary>, t: Throwable) {
                        dialog.dismiss()
                        exceptionFlow(t.stackTraceToString())
                        exceptionFlow(t.cause.toString())
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
        fun newInstance() = FragmentLibrary()
    }
}