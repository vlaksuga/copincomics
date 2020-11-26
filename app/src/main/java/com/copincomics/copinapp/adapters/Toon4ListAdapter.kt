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

class Toon4ListAdapter(
    val section: Int = R.layout.section_a4,
    var data: List<WebtoonItem>,
    val context: Context,
    val DP: Float,
    val imgtype: String = "1x1",

    ) : RecyclerView.Adapter<Toon4ListAdapter.VH>() {
    val TAG = "BBB Webtoon grid adapter"

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView

        val a = itemView.findViewById<View>(R.id.sub_a)
        val b = itemView.findViewById<View>(R.id.sub_b)
        val c = itemView.findViewById<View>(R.id.sub_c)
        val d = itemView.findViewById<View>(R.id.sub_d)
        val listofView = listOf<View>(a, b, c, d)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(section, parent, false)

        val a = itemView.findViewById<View>(R.id.sub_a)
        val b = itemView.findViewById<View>(R.id.sub_b)
        val c = itemView.findViewById<View>(R.id.sub_c)
        val d = itemView.findViewById<View>(R.id.sub_d)
        val listofView = listOf<View>(a, b, c, d)

        for (v in listofView) {
            // 이미지 사이즈 조정
            v.findViewById<ImageView>(R.id.section_thumbnail).layoutParams.height = (154 * DP).toInt()
            v.findViewById<ImageView>(R.id.section_thumbnail).layoutParams.width = (154 * DP).toInt()

//            v.findViewById<ImageView>(R.id.section_tag_new).layoutParams.width = (30 * DP).toInt()
            v.findViewById<ImageView>(R.id.section_tag_wait).layoutParams.height = (30 * DP).toInt()
            v.findViewById<ImageView>(R.id.section_tag_up).layoutParams.width = (28 * DP).toInt()
        }


        return VH(itemView)
    }

    override fun getItemCount(): Int {
        if (data.size / 4 > 3) {
            return 3
        } else (return kotlin.math.ceil(data.size.toFloat() / 4.0f).toInt())
    }


    // 데이터 바인딩
    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            holder.listofView.forEachIndexed { i, v ->
                val thumb = v.findViewById<ImageView>(R.id.section_thumbnail)
                val tag_new = v.findViewById<ImageView>(R.id.section_tag_new)
                val tag_wait = v.findViewById<ImageView>(R.id.section_tag_wait)
                val tag_up = v.findViewById<ImageView>(R.id.section_tag_up)
                val title = v.findViewById<TextView>(R.id.section_title)
                val genre = v.findViewById<TextView>(R.id.section_genre)

                val index = position * 4 + i
                if (index >= data.size) {
                    tag_new?.visibility = View.INVISIBLE
                    tag_wait?.visibility = View.INVISIBLE
                    tag_up?.visibility = View.INVISIBLE
                    title.text = ""
                    genre.text = ""
                    thumb.visibility = View.INVISIBLE

                    return@forEachIndexed
                }

//            Log.d(TAG, "total : ${data.size} sub page: $i, index : $index")


                // 태그별
                if (data[index].isnew == "Y") {
                    tag_new?.visibility = View.VISIBLE
                } else {
                    tag_new?.visibility = View.INVISIBLE
                }
                if (data[index].cantermgift == "Y") {
                    tag_wait?.visibility = View.VISIBLE
                } else {
                    tag_wait?.visibility = View.INVISIBLE
                }
                if (data[index].isup == "Y") {
                    tag_up?.visibility = View.VISIBLE
                } else {
                    tag_up?.visibility = View.INVISIBLE
                }


                // 웹툰 이름 쓰기
                var titletxt = ""
                if (data[index].isnew == "Y") {
                    titletxt = "       " + data[index].titlename
                } else {
                    titletxt = data[index].titlename
                }
                title.text = titletxt


                // 장르 쓰기
                var genrestxt: String = ""
                try {
                    for (tag in data[index].systemtag) {
                        if (tag.tagkind == "-2") {
                            genrestxt += tag.name + " "
                        }
                    }
                } catch (e: java.lang.Exception) {
                    Log.d(TAG, "genre not informed, position : $position")
                }

                genre.text = genrestxt


                var media = data[index].thumbs.thumb5x3
                when (imgtype) {
                    "1x1" -> media = data[index].thumbs.thumb1x1
                    "2x1" -> media = data[index].thumbs.thumb2x1
                    "3x2" -> media = data[index].thumbs.thumb3x2
                    "3x4" -> media = data[index].thumbs.thumb3x4
                    "4x3" -> media = data[index].thumbs.thumb4x3
                    "5x3" -> media = data[index].thumbs.thumb5x3
                    else -> {
                    }
                }
                GlideApp.with(context)
                    .load(media)
                    .into(thumb)

                v.setOnClickListener {
                    val intent = Intent(context, EpiListActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("id", data[index].titlepkey)
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
            }
        } catch (t: java.lang.Exception) {
            Log.d(TAG, t.stackTraceToString())
        }
    }
}