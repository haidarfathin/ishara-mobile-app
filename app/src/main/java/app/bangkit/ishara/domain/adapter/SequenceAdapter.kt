package app.bangkit.ishara.domain.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import app.bangkit.ishara.R
import com.bumptech.glide.Glide

class SequenceAdapter(private val questionList: List<String>) : RecyclerView.Adapter<SequenceAdapter.SequenceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SequenceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sequence, parent, false)
        return SequenceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SequenceViewHolder, position: Int) {
        val questionItem = questionList[position]
        Glide.with(holder.itemView.context)
            .load(questionItem)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class SequenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_item_sequence)
    }
}
