package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.SuggestionitemBinding
import com.example.resumemaker.models.SuggestionModel

class SuggestionAdapter(val context: Context, val list: ArrayList<String>, val onclick:(String)->Unit): RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: SuggestionitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: String) {
            binding.name.text=model
            binding.name.setOnClickListener {
                onclick(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SuggestionitemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }


}