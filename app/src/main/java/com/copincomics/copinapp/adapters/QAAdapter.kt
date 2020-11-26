package com.copincomics.copinapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.MyQuestion
import com.copincomics.copinapp.tools.ExpandableLayout
import kotlinx.android.synthetic.main.section_qa.view.*
import java.util.*

class QAAdapter(
    val data: List<MyQuestion>,
    val context: Context,
    val dp: Float,
) : RecyclerView.Adapter<QAAdapter.VH>() {
    // Save the expanded row position
    private val expandedPositionSet: HashSet<Int> = HashSet()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(R.layout.section_qa, parent, false)
        resize(itemView)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // 레이아웃 리사이징
    fun resize(itemView: View) {

    }

    // 데이터 바인딩
    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            val ret = data[position]

            holder.itemView.qa_title.text = "Q. ${ret.title}"
            holder.itemView.qa_qdate.text = ret.issuedate
            holder.itemView.qa_qtext.text = ret.txt

            var a = ""
            if (ret.status == "Q") {
                a = "A. We will reply back as soon as possible"
            } else {
                a = "A. ${ret.answer}"
            }
            holder.itemView.qa_a.text = a

            // Expand when you click on cell
            holder.itemView.expand_layout.setOnExpandListener(object :
                ExpandableLayout.OnExpandListener {
                override fun onExpand(expanded: Boolean) {
                    if (expandedPositionSet.contains(position)) {
                        expandedPositionSet.remove(position)
                    } else {
                        expandedPositionSet.add(position)
                    }
                }
            })


        } catch (t: Exception) {
            Toast.makeText(
                context,
                "Network Error! Try Again Please!",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("BBB QA adapter", t.toString())
        }
    }
}