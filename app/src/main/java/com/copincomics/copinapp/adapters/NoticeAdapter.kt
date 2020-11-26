package com.copincomics.copinapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.NoticeItem

class NoticeAdapter(
    val data: List<NoticeItem>,
    val context: Context,
    val dp: Float,
) : RecyclerView.Adapter<NoticeAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val notice_title: TextView = itemView.findViewById(R.id.notice_title)
        val notice_date: TextView = itemView.findViewById(R.id.notice_date)
        val notice_txt: TextView = itemView.findViewById(R.id.notice_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(R.layout.section_notice, parent, false)
//        resize(itemView)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        return data.size

    }

    // 레이아웃 리사이징
    fun resize(itemView: View) {
        itemView.layoutParams.height = (dp * 74).toInt()
    }

    // 데이터 바인딩
    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            val ret = data[position]
            holder.notice_title.text = ret.noticetitle
            holder.notice_date.text = ret.pubdate
            holder.notice_txt.text = ret.noticetxt


        } catch (t: Exception) {
            Toast.makeText(
                context,
                "Network Error! Try Again Please!",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("AAA banner", t.toString())
        }
    }
}