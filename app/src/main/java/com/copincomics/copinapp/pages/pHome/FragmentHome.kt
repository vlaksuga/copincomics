package com.copincomics.copinapp.pages.pHome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.BannerAdapter
import com.copincomics.copinapp.adapters.PdPickAdapter
import com.copincomics.copinapp.adapters.Toon4ListAdapter
import com.copincomics.copinapp.adapters.ToonListAdapter
import com.copincomics.copinapp.data.PageMain
import com.copincomics.copinapp.pages.BaseFragment
import com.copincomics.copinapp.pages.pPDpick.PdPickActivity
import com.copincomics.copinapp.tools.GridSpacingItemDecoration
import com.copincomics.copinapp.tools.SpacesItemHorizontalDecoration
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_main.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentHome : BaseFragment() {
    lateinit var home_banner: ViewPager2
    lateinit var home_banner_indicator: TabLayout

    lateinit var home_new_vp: ViewPager2
    lateinit var home_new_indicator: TabLayout
    lateinit var home_new_more: TextView

    lateinit var home_freetoread_rv: RecyclerView
    lateinit var home_wait_more: TextView

    lateinit var home_week_rv: RecyclerView
    lateinit var home_week_more: TextView

    lateinit var home_pdpick_more: TextView
    lateinit var home_pdpick_rv: RecyclerView
    lateinit var callback: OnMoreSelectedListener

    fun setOnMoreSelectedListener(onMoreSelectedListener: OnMoreSelectedListener) {
        this.callback = onMoreSelectedListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        init(view)
        TAG = "BBB Main Home"

        home_banner = view.findViewById(R.id.home_banner)
        home_banner_indicator = view.findViewById(R.id.home_banner_indicator)
        home_banner.layoutParams.height = (DP * 220).toInt()

        home_new_vp = view.findViewById(R.id.home_new_vp)
        home_new_indicator = view.findViewById(R.id.home_new_indicator)
        home_new_more = view.findViewById(R.id.home_new_more)

        home_freetoread_rv = view.findViewById(R.id.home_freetoread_rv)
        home_wait_more = view.findViewById(R.id.home_wait_more)

        home_week_rv = view.findViewById(R.id.home_week_rv)
        home_week_more = view.findViewById(R.id.home_week_more)


        home_pdpick_more = view.findViewById(R.id.home_pdpick_more)
        home_pdpick_rv = view.findViewById(R.id.home_pdpick_rv)


        home_freetoread_rv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        home_freetoread_rv.addItemDecoration(SpacesItemHorizontalDecoration((10 * DP).toInt()))

        home_week_rv.addItemDecoration(SpacesItemHorizontalDecoration((10 * DP).toInt()))
        home_week_rv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)


        home_pdpick_rv.layoutManager = GridLayoutManager(activity, 2)
        home_pdpick_rv.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                (16 * DP).toInt(),
                (16 * DP).toInt(),
                (16 * DP).toInt()
            )
        )

        home_new_more.setOnClickListener {
            startActivity(Intent(context, DetailListActivity::class.java))
        }

        home_wait_more.setOnClickListener {
            callback.onSelected("free")
        }

        home_week_more.setOnClickListener {
            callback.onSelected("daily")
        }

        home_pdpick_more.setOnClickListener {
            startActivity(Intent(context, PdPickActivity::class.java))
        }



        update()
        return view
    }

    interface OnMoreSelectedListener {
        fun onSelected(position: String)
    }


    override fun onResume() {
        super.onResume()
        update()
    }

    private fun update() {
        dialog.show()
        try {
            net.serviceAPIs.pageMain()
                .enqueue(object : Callback<PageMain> {
                    override fun onResponse(call: Call<PageMain>, response: Response<PageMain>) {
                        val ret = response.body()!!.body

                        home_banner.adapter = BannerAdapter(ret.mainbanner, context!!, DP, 220, 361)
                        TabLayoutMediator(home_banner_indicator, home_banner)
                        { _, _ -> }.attach()

                        home_new_vp.adapter = Toon4ListAdapter(R.layout.section_a4, ret.listRecent, context!!, DP, "1x1")
                        TabLayoutMediator(home_new_indicator, home_new_vp)
                        { _, _ -> }.attach()
                        val new_more_txt = "+ ${ret.listRecent.size}"
                        home_new_more.text = new_more_txt

                        home_freetoread_rv.adapter = ToonListAdapter(R.layout.section_a, ret.listTermGift, context!!, DP, 154, 154, "all", "1x1")
                        home_week_rv.adapter = ToonListAdapter(R.layout.section_a, ret.listDayOfWeek, context!!, DP, 154, 154, "all", "1x1")
                        home_pdpick_rv.adapter = PdPickAdapter(ret.listPDPick, context!!, DP, 212, 154)


                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<PageMain>, t: Throwable) {
                        exceptionFlow(t.toString())
                        dialog.dismiss()
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
        fun newInstance() = FragmentHome()
    }
}