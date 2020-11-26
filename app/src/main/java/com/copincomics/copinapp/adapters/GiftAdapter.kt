package com.copincomics.copinapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.GiftItem
import com.copincomics.copinapp.pages.pEpiList.EpiListActivity
import com.copincomics.copinapp.tools.GlideApp

class GiftAdapter(
    val data: List<GiftItem>,
    val context: Context,
    val dp: Float,
    val height: Int = 70,
    val width: Int = 110,
    val cellClickListener: CellClickListener
) : RecyclerView.Adapter<GiftAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val webtoonThumb: ImageView? = itemView.findViewById<ImageView>(R.id.section_thumbnail)
        val section_title: TextView? = itemView.findViewById<TextView>(R.id.section_title)
        val gift_cnt: TextView? = itemView.findViewById<TextView>(R.id.gift_cnt)
        val gift_date: TextView? = itemView.findViewById<TextView>(R.id.gift_date)
        val gift_btn: ImageView? = itemView.findViewById<ImageView>(R.id.gift_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(R.layout.section_gift, parent, false)
        resize(itemView)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // 레이아웃 리사이징
    fun resize(itemView: View) {
        val itemViewLayout = itemView.findViewById<ImageView>(R.id.gift_box)
        val thumbs = itemView.findViewById<ImageView>(R.id.section_thumbnail)
        itemViewLayout.layoutParams.height = (dp * height).toInt()
        thumbs.layoutParams.width = (dp * height).toInt()
        thumbs.layoutParams.height = (dp * height).toInt()
//        itemViewLayout.layoutParams.width = (dp * width).toInt()
    }

    // 데이터 바인딩
    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            // 초기화
            holder.webtoonThumb!!.setImageResource(R.drawable.ic_blank)
//            holder.webtoonTitle?.text = data[position]
            val media = data[position].thumbs.thumb1x1

            // thumb 받아옴
            if (media !== null) {
                GlideApp.with(context)
                    .load(media)
                    .into(holder.webtoonThumb)
            } else {
                // 실패시 처리
                holder.webtoonThumb.setImageResource(R.drawable.ic_blank)
            }

            holder.section_title!!.text = data[position].titlename
            holder.gift_cnt!!.text = data[position].ticketcount
            holder.gift_date!!.text = data[position].enddate


            holder.webtoonThumb.setOnClickListener {
                val intent = Intent(context, EpiListActivity::class.java)
                val bundle = Bundle()
                val id: String = data[position].titlepkey
//                "link": "toon://90" trim string
                bundle.putString("id", id)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
            if (data[position].remaincnt == "-1") {
                holder.gift_btn?.setImageResource(R.mipmap.btn_receive)
                holder.gift_btn?.setOnClickListener {
                    cellClickListener.onCellClickListener(position, data[position].promotionpkey)
                }
            } else {
                holder.gift_cnt.text = data[position].remaincnt
                holder.gift_btn?.setImageResource(R.mipmap.btn_read)
                holder.gift_btn?.setOnClickListener {
                    val intent = Intent(context, EpiListActivity::class.java)
                    val bundle = Bundle()
                    val id: String = data[position].titlepkey
//                "link": "toon://90" trim string
                    bundle.putString("id", id)
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
            }


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