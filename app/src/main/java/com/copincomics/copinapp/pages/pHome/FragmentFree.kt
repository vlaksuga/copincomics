package com.copincomics.copinapp.pages.pHome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.ToonListAdapter
import com.copincomics.copinapp.adapters.ToonLoopListAdapter
import com.copincomics.copinapp.data.PageContents
import com.copincomics.copinapp.pages.BaseFragment
import com.copincomics.copinapp.tools.GridSpacingItemDecoration
import com.copincomics.copinapp.tools.SpacesItemHorizontalDecoration
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentFree : BaseFragment() {
    lateinit var banner: RecyclerView
    lateinit var banner_indicator: TabLayout
    lateinit var rv: RecyclerView
    val snapHelper = PagerSnapHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rv_rv, container, false)
        init(view)
        TAG = "BBB Free"

        banner = view.findViewById(R.id.banner)
        rv = view.findViewById(R.id.rv)

//        banner.layoutParams.height = (DP * 150).toInt()
        banner.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        banner.addItemDecoration(SpacesItemHorizontalDecoration((20 * DP).toInt()))



        rv.layoutManager = GridLayoutManager(activity, 2)
        rv.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                (18 * DP).toInt(),
                (15 * DP).toInt(),
                (16 * DP).toInt()
            )
        )



        update()
        return view
    }


    override fun onResume() {
        super.onResume()
        update()
    }


    fun update() {
        dialog.show()
        try {
            net.serviceAPIs.tflist()
                .enqueue(object : Callback<PageContents> {
                    override fun onResponse(call: Call<PageContents>, response: Response<PageContents>) {
                        try {

                            val ret = response.body()!!.body

                            banner.adapter = ToonLoopListAdapter(R.layout.section_c, ret.list, context!!, DP, 160, 240, "all", "5x3")
                            snapHelper.attachToRecyclerView(banner)
                            banner.layoutManager!!.scrollToPosition(50)

                            rv.adapter = ToonListAdapter(R.layout.section_a, ret.list, context!!, DP, 154, 154, "all", "1x1")


                        } catch (t: Exception) {
                            showToast("Network Error! Try Again Please!")
                            Log.d(TAG, t.stackTraceToString())
                        }
                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<PageContents>, t: Throwable) {
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
        fun newInstance() = FragmentFree()
    }
}