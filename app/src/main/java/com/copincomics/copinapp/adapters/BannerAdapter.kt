package com.copincomics.copinapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.Banner
import com.copincomics.copinapp.pages.pEpiList.EpiListActivity
import com.copincomics.copinapp.tools.GlideApp

class BannerAdapter(
    val data: List<Banner>,
    val context: Context,
    val dp: Float,
    val height: Int = 70,
    val width: Int = 110
) : RecyclerView.Adapter<BannerAdapter.VH>() {
    val TAG = "BBB banner"

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val webtoonThumb: ImageView? = itemView.findViewById<ImageView>(R.id.section_thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(R.layout.section_banner, parent, false)
        resize(itemView)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // 레이아웃 리사이징
    fun resize(itemView: View) {
        val itemViewLayout = itemView.findViewById<ImageView>(R.id.section_thumbnail)
        itemViewLayout.layoutParams.height = (dp * height).toInt()
        itemViewLayout.layoutParams.width = (dp * width).toInt()
    }

    // 데이터 바인딩
    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            // 초기화
            holder.webtoonThumb!!.setImageResource(R.drawable.ic_blank)
//            holder.webtoonTitle?.text = data[position]
            val media = data[position].img

            // thumb 받아옴
            if (media !== null) {
                GlideApp.with(context)
                    .load(media)
                    .into(holder.webtoonThumb)
            } else {
                // 실패시 처리
                holder.webtoonThumb.setImageResource(R.drawable.ic_blank)
            }

            try {
//            "toon://90"
                if (data[position].link.substring(0, 7) == "toon://") {
                    holder.view.setOnClickListener {
                        val intent = Intent(context, EpiListActivity::class.java)
                        val bundle = Bundle()
                        val id: String = data[position].link
//                "link": "toon://90" trim string
                        bundle.putString("id", id.substring(7))
                        intent.putExtras(bundle)
                        context.startActivity(intent)
                    }
                }
            } catch (t: java.lang.Exception) {
            }


        } catch (t: Exception) {
            Toast.makeText(
                context,
                "Network Error! Try Again Please!",
                Toast.LENGTH_SHORT
            ).show()
            Log.d(TAG, t.stackTraceToString())
            Log.d(TAG, t.toString())
        }
    }
}
