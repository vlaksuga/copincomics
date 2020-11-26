package com.copincomics.copinapp.pages.pSearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.SearchAdapter
import com.copincomics.copinapp.adapters.ToonListAdapter
import com.copincomics.copinapp.data.SearchPageData
import com.copincomics.copinapp.data.SearchQueryData
import com.copincomics.copinapp.data.SearchTag
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.tools.SpacesItemVerticalDecoration
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity(), SearchAdapter.OnListItemSelectedInterface {
    lateinit var rv: RecyclerView
    lateinit var pageAdapter: SearchAdapter
    lateinit var queryAdapter: ToonListAdapter
    lateinit var tagList: List<SearchTag>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
        TAG = "BBB Search"

        // 액티비티 이름 설정
        findViewById<TextView>(R.id.title).text = "Real Time Search"

        // 닫기 x 버튼 finish
        val btnClose = findViewById<ImageView>(R.id.btn_finish)
        btnClose.setOnClickListener {
            finish()
        }

        rv = findViewById(R.id.search_rv)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.addItemDecoration(SpacesItemVerticalDecoration((10 * DP).toInt()))

        dialog.show()
        net.searchAPIs.searchPage().enqueue(object : Callback<SearchPageData> {
            override fun onResponse(call: Call<SearchPageData>, response: Response<SearchPageData>) {
                try {
                    tagList = response.body()!!.body.taglist
                    pageAdapter = SearchAdapter(tagList, 0, this@SearchActivity, this@SearchActivity, search_edit_text)
                    rv.adapter = pageAdapter
                    dialog.dismiss()
                } catch (e: Exception) {
                    exceptionFlow(e.toString())
                    Log.d(TAG, "Search list network error")
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<SearchPageData>, t: Throwable) {
                exceptionFlow(t.toString())
                Log.d(TAG, "Search list network failure")
                Toast.makeText(applicationContext, "Search list network failure", Toast.LENGTH_LONG).show()
            }
        })



        search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (search_edit_text.text.toString() == "") {
                    rv.adapter = pageAdapter
                } else {
                    net.searchAPIs.searchQuery(query = search_edit_text.text.toString())
                        .enqueue(object : Callback<SearchQueryData> {
                            override fun onResponse(call: Call<SearchQueryData>, response: Response<SearchQueryData>) {
                                val data = response.body()!!.body.list
                                try {
                                    val data = response.body()!!.body.list
                                    queryAdapter = ToonListAdapter(R.layout.section_b, data, this@SearchActivity, DP)
                                    rv.adapter = queryAdapter
                                } catch (e: Exception) {
                                    exceptionFlow(e.toString())
                                    Log.d(TAG, "Search query network error")
                                }
                            }

                            override fun onFailure(call: Call<SearchQueryData>, t: Throwable) {
                                exceptionFlow(t.toString())
                                Log.d(TAG, "Search query network failure")
                                Toast.makeText(applicationContext, "Search query network failure", Toast.LENGTH_LONG).show()
                            }
                        })
                }
            }
        })
    }

    override fun onItemSelected(v: View?, position: Int) {
        rv.adapter = SearchAdapter(tagList, position, this, this, search_edit_text)
    }

}