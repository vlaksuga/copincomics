package com.copincomics.copinapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.CoinHistoryItem

class CoinHistoryAdapterNotExp(
    val data: List<CoinHistoryItem>,
    val context: Context,
    val dp: Float,
) : RecyclerView.Adapter<CoinHistoryAdapterNotExp.VH>() {
    // Save the expanded row position

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val coindate = view.findViewById<TextView>(R.id.expandable_main_date)
        val coinamount = view.findViewById<TextView>(R.id.expandable_charged_coin)
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
//        try {
        val ret = data[position]

        holder.coindate.text = "Coin ${ret.issuedate}"
        holder.coinamount.text = "Coin ${ret.amount}"

//        } catch (t: Exception) {
//            Toast.makeText(
//                context,
//                "Network Error! Try Again Please!",
//                Toast.LENGTH_SHORT
//            ).show()
//            Log.d("BBB Coinhistory adapter", t.toString())
//        }
    }
}