package com.pentabit.cvmaker.resumebuilder.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pentabit.cvmaker.resumebuilder.databinding.SampleitemsBinding
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel

class SampleAdapter(
    val context: Context, val list: List<SampleResponseModel>,
    val onclick:(SampleResponseModel)->Unit): RecyclerView.Adapter<SampleAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: SampleitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: SampleResponseModel) {
            binding.sampleText.apply {
                text=model.title
                setOnClickListener {
                    onclick(model)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SampleitemsBinding.inflate(
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