package com.copincomics.copinapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.LibraryHistoryItem

class LibraryHistoryAdapter(
    val data: List<LibraryHistoryItem>,
    val context: Context,
    val dp: Float,
) : RecyclerView.Adapter<LibraryHistoryAdapter.VH>() {
    // Save the expanded row position

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val ic = view.findViewById<ImageView>(R.id.expandable_main_thumbnail)
        val title = view.findViewById<TextView>(R.id.expandable_main_title)
        val date = view.findViewById<TextView>(R.id.expandable_main_date)
        val amount = view.findViewById<TextView>(R.id.expandable_charged_coin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(R.layout.section_coinhistory_notexp, parent, false)
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
        holder.ic.setImageResource(R.mipmap.img_file)
        holder.date.text = ""
        holder.title.text = ""
        holder.amount.text = ""

        try {
            val ret = data[position]
            holder.date.text = "${ret.issuedate.subSequence(0, 7)}"
            holder.title.text = "${ret.amount}"
            holder.amount.text = "${ret.kind}"

        } catch (t: Exception) {
            Toast.makeText(
                context,
                "Network Error! Try Again Please!",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("BBB library history adapter", t.toString())
        }
    }
}