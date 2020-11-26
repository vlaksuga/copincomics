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
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.WebtoonItem
import com.copincomics.copinapp.pages.pEpiList.EpiListActivity
import com.copincomics.copinapp.tools.GlideApp

class ToonLoopListAdapter(
    val section: Int = R.layout.section_a,
    var data: List<WebtoonItem>,
    val context: Context,
    val dp: Float,
    val height: Int = 70, // 썸네일 사이즈
    val width: Int = 110,
    val tagtype: String = "all",
    val imgtype: String = "1x1",

    ) : RecyclerView.Adapter<ToonLoopListAdapter.VH>() {
    val TAG = "BBB Webtoon adapter"

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView

        val webtoonThumb: ImageView? = itemView.findViewById(R.id.section_thumbnail)
        val webtoonTitle: TextView? = itemView.findViewById(R.id.section_title)
        val webtoonGenre: TextView? = itemView.findViewById(R.id.section_genre)

        val newTag: ImageView? = itemView.findViewById(R.id.section_tag_new)
        val waitTag: ImageView? = itemView.findViewById(R.id.section_tag_wait)
        val upTag: ImageView? = itemView.findViewById(R.id.section_tag_up)

        val update_date: TextView? = itemView.findViewById(R.id.section_updatedate)
        val webtoonWriter: TextView? = itemView.findViewById(R.id.section_writer)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(section, parent, false)
        resize(itemView)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
//        return data.size * 2
        return 100
    }

    // 레이아웃 리사이징
    fun resize(itemView: View) {
        // 기준 이미지 리사이징
        val itemViewLayout = itemView.findViewById<ImageView>(R.id.section_thumbnail)
        itemViewLayout.layoutParams.height = (dp * height).toInt()
        itemViewLayout.layoutParams.width = (dp * width).toInt()

        // 태그들 리사이징
        val newTag: ImageView? = itemView.findViewById(R.id.section_tag_new)
        newTag?.layoutParams?.height = (dp * 15).toInt()
        newTag?.layoutParams?.width = (dp * 15).toInt()
        newTag?.visibility = View.INVISIBLE

        val waitTag: ImageView? = itemView.findViewById(R.id.section_tag_wait)
        waitTag?.layoutParams?.height = (dp * 30).toInt()
        waitTag?.layoutParams?.width = (dp * 30).toInt()
        waitTag?.visibility = View.INVISIBLE

        val upTag: ImageView? = itemView.findViewById(R.id.section_tag_up)
        waitTag?.layoutParams?.height = (dp * 30).toInt()
        waitTag?.layoutParams?.width = (dp * 30).toInt()
        waitTag?.visibility = View.INVISIBLE
    }

    // 데이터 바인딩
    override fun onBindViewHolder(holder: VH, position_: Int) {
        try {
            val position = position_ % data.size

            holder.webtoonThumb!!.setImageResource(R.drawable.ic_blank)

            // 태그별
            if (data[position].isnew == "Y") {
                holder.newTag?.visibility = View.VISIBLE
            } else {
                holder.newTag?.visibility = View.INVISIBLE
            }

            if (data[position].cantermgift == "Y") {
                holder.waitTag?.visibility = View.VISIBLE
            } else {
                holder.waitTag?.visibility = View.INVISIBLE
            }

            if (data[position].isup == "Y") {
                holder.upTag?.visibility = View.VISIBLE
            } else {
                holder.upTag?.visibility = View.INVISIBLE
            }

            if (tagtype == "none") {
                holder.newTag?.visibility = View.INVISIBLE
                holder.upTag?.visibility = View.INVISIBLE
                holder.waitTag?.visibility = View.INVISIBLE
            }

            // 웹툰 이름 쓰기
            var title = ""
            if (data[position].isnew == "Y") {
                title = "       " + data[position].titlename
            } else {
                title = data[position].titlename
            }
            holder.webtoonTitle?.text = title


            var media = data[position].thumbs.thumb5x3

            // 장르 쓰기
            var genres: String = ""
            try {
                for (tag in data[position].systemtag) {
                    if (tag.tagkind == "-2") {
                        genres += tag.name + " "
                    }
                }
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "genre not informed, position : $position")
            }
            holder.webtoonGenre?.text = genres

            // 작가 쓰기
            var writer: String = ""
            try {
                for (w in data[position].authors) {
                    writer += w.authorname + " "
                }
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "author not informed, position : $position")
            }

            holder.webtoonWriter?.text = writer

            holder.update_date?.text = data[position].lastupdate.subSequence(0, 8)

            when (imgtype) {
                "1x1" -> media = data[position].thumbs.thumb1x1
                "2x1" -> media = data[position].thumbs.thumb2x1
                "3x2" -> media = data[position].thumbs.thumb3x2
                "3x4" -> media = data[position].thumbs.thumb3x4
                "4x3" -> media = data[position].thumbs.thumb4x3
                "5x3" -> media = data[position].thumbs.thumb5x3
                else -> {
                }
            }

            GlideApp.with(context)
                .load(media)
                .into(holder.webtoonThumb)


            holder.view.setOnClickListener {
                val intent = Intent(context, EpiListActivity::class.java)
                val bundle = Bundle()
                bundle.putString("id", data[position].titlepkey)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
        } catch (t: java.lang.Exception) {
            Log.d(TAG, t.stackTraceToString())
        }
    }
}