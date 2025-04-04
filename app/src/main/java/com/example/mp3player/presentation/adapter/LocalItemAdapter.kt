package com.example.mp3player.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mp3player.R
import com.example.mp3player.domain.model.LocalItem

class LocalItemAdapter(
    private var listItem: List<LocalItem>,
    private val isAudio: Boolean = false
) : RecyclerView.Adapter<LocalItemAdapter.LocalItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocalItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.audio_item, parent, false)
        return LocalItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: LocalItemViewHolder,
        position: Int
    ) {
        val item = listItem[position]
        holder.tvTitle.text = item.name
        holder.tvAuthor.text = item.author
        holder.ivPlaceHolder.setImageResource(if (isAudio) R.drawable.ic_placeholder else R.drawable.ic_play)
    }

    override fun getItemCount(): Int = listItem.size

    class LocalItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPlaceHolder: ImageView = itemView.findViewById(R.id.ivPlaceHolder)
        val tvTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvItemAuthor)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItem(item: List<LocalItem>) {
        listItem = item
        notifyDataSetChanged()
    }
}