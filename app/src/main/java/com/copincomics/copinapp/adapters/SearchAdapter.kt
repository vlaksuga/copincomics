package com.copincomics.copinapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.SearchTag
import com.copincomics.copinapp.pages.pEpiList.EpiListActivity


class SearchAdapter(
    val searchTagList: List<SearchTag>,
    val selected: Int = 1,
    val context: Context,
    val mListener: OnListItemSelectedInterface,
    val search_edit_text: EditText,
) : RecyclerView.Adapter<SearchAdapter.VH?>() {

    val sectionSelect: Int = R.layout.section_search_selected
    val sectionUnselect: Int = R.layout.section_search_unselected
    lateinit var itemView: View


    interface OnListItemSelectedInterface {
        fun onItemSelected(v: View?, position: Int)
    }


    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchNum = itemView.findViewById<TextView>(R.id.search_num)
        val tagName = itemView.findViewById<TextView>(R.id.search_webtoon_tag)
        val tagMore = itemView.findViewById<TextView?>(R.id.search_webtoon_tag_more)

        val thumbs = listOf<ImageView?>(
            itemView.findViewById(R.id.search_thumbnail1),
            itemView.findViewById(R.id.search_thumbnail2),
            itemView.findViewById(R.id.search_thumbnail3)
        )

        init {
            itemView.setOnClickListener { v ->
                val position = adapterPosition
                mListener.onItemSelected(v, adapterPosition)
//                Log.d("test", "position = $adapterPosition")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (viewType == 1) {
            itemView = inflater.inflate(sectionSelect, parent, false)
        } else {
            itemView = inflater.inflate(sectionUnselect, parent, false)
        }
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        if (searchTagList.size > 10) {
            return 10
        } else {
            return searchTagList.size
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            val data = searchTagList[position]
            holder.searchNum.text = (position + 1).toString()
            holder.tagName.text = data.tag
            holder.tagMore?.setOnClickListener {
                val t = "#" + data.tag
                search_edit_text.setText(t)
            }
            for (i in 0..data.list.size) {
                holder.thumbs[i]?.let {
                    Glide.with(context)
                        .load(data.list[i].thumbs.thumb5x3)
                        .into(it)
                }
                holder.thumbs[i]?.visibility = View.VISIBLE
                holder.thumbs[i]?.setOnClickListener {
                    val intent = Intent(context, EpiListActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("id", data.list[i].titlepkey)
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
            }
        } catch (e: Exception) {
            Log.d("BBB search adapter : ", e.toString())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == selected) {
            1
        } else {
            0
        }
    }
}