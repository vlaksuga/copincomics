package com.copincomics.copinapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.copincomics.copinapp.R

class NativeProductAdapter(
    val data: List<SkuDetails>,
    val context: Context,
    val dp: Float,
    val cellClickListener: CellClickListener
) : RecyclerView.Adapter<NativeProductAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val amount: TextView = itemView.findViewById(R.id.product_amount)
        val price: TextView = itemView.findViewById(R.id.product_price)
        val bonus: TextView = itemView.findViewById(R.id.product_bonus)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(R.layout.section_product, parent, false)
        resize(itemView)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
//        return 5
    }

    // 레이아웃 리사이징
    fun resize(itemView: View) {
        itemView.layoutParams.height = (dp * 90).toInt()
    }

    // 데이터 바인딩
    override fun onBindViewHolder(holder: VH, position: Int) {

        try {
            val ret = data[position]
            holder.amount.text = "${ret.description} Coins"
            holder.bonus.text = "(+ Bonus 0 Coins)"
            holder.price.text = "${ret.price}"

            holder.itemView.setOnClickListener {
                cellClickListener.onCellClickListener(position, "")
            }

        } catch (t: Exception) {
            Toast.makeText(
                context,
                "Network Error! Try Again Please!",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("BBB Product adapter", t.toString())
        }
    }
}