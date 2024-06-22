package app.bangkit.ishara.ui.main.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.droidsonroids.gif.GifImageView
import app.bangkit.ishara.R

class GifWordAdapter(private val gifList: List<GifItem>) : RecyclerView.Adapter<GifWordAdapter.GifViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gif_sign, parent, false)
        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val gifItem = gifList[position]
        holder.gifImageView.setImageResource(gifItem.gifResource)
        holder.textView.text = gifItem.label
    }

    override fun getItemCount(): Int {
        return gifList.size
    }

    class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gifImageView: GifImageView = itemView.findViewById(R.id.gif_item)
        val textView: TextView = itemView.findViewById(R.id.tv_today_sign)
    }
}

data class GifItem(val gifResource: Int, val label: String)
