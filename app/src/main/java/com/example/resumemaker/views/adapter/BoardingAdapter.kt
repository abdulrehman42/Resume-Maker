package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resumemaker.databinding.BoardingitemBinding
import com.example.resumemaker.models.BoardingItems

class BoardingAdapter(
    val context: Context,
    val list:List<BoardingItems>,
    val onclick: (Int) -> Unit
):RecyclerView.Adapter<BoardingAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: BoardingitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: BoardingItems, position: Int) {
            Glide.with(context).load(model.image).into(binding.imageId)
            binding.headerTitle.text = model.headerText
            binding.regularTitle.text=model.regularText
            //binding.root.setOnClickListener {
            //}
            onclick(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BoardingitemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position],position)
    }


}