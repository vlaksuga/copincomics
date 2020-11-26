package com.copincomics.copinapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.PDPick
import com.copincomics.copinapp.pages.pPDpick.PdPickActivity
import com.copincomics.copinapp.tools.GlideApp

class PdPickAdapter(
    var data: List<PDPick>,
    val context: Context,
    val dp: Float,
    val height: Int = 70, // 썸네일 사이즈
    val width: Int = 110,
) : RecyclerView.Adapter<PdPickAdapter.VH>() {
    val TAG = "BBB Webtoon adapter"

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView

        val webtoonThumb: ImageView? = itemView.findViewById(R.id.section_thumbnail)
        val groupflag: TextView? = itemView.findViewById(R.id.groupflag)
        val groupname: TextView? = itemView.findViewById(R.id.groupname)
        val groupname2: TextView? = itemView.findViewById(R.id.groupname2)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(R.layout.section_pdpick, parent, false)
        resize(itemView)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        return if (data.size >= 4) {
            4
        } else {
            data.size
        }
    }

    // 레이아웃 리사이징
    fun resize(itemView: View) {
        // 기준 이미지 리사이징
        val itemViewLayout = itemView.findViewById<ImageView>(R.id.section_thumbnail)
        itemViewLayout.layoutParams.height = (dp * height).toInt()
        itemViewLayout.layoutParams.width = (dp * width).toInt()
    }

    // 데이터 바인딩
    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            holder.webtoonThumb

            holder.webtoonThumb?.setOnClickListener {
                val intent = Intent(context, PdPickActivity::class.java)
                val bundle = Bundle()
                bundle.putString("id", data[position].collagepkey)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }


            // 웹툰 이름 쓰기
            val ret = data[position]
            val media = data[position].thumbs.thumb4x3

            holder.groupflag?.text = ret.groupflag
            holder.groupname?.text = ret.groupname
            holder.groupname2?.text = ret.groupname2

            GlideApp.with(context)
                .load(media)
                .into(holder.webtoonThumb!!)

        } catch (t: java.lang.Exception) {
        }

//        holder.view.setOnClickListener {
//            val intent = Intent(context, EpiListActivity::class.java)
//            val bundle = Bundle()
//            bundle.putString("id", data[position].titlepkey)
//            intent.putExtras(bundle)
//            context.startActivity(intent)
//        }
    }
}