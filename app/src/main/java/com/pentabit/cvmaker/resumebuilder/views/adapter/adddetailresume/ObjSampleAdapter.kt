package com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pentabit.cvmaker.resumebuilder.databinding.SampleitemsBinding
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel

class ObjSampleAdapter(
    val context: Context,
    val list: List<SampleResponseModel>,
    val onclick:(String)->Unit,
    ): RecyclerView.Adapter<ObjSampleAdapter.ViewHolder>() {
    var maxItemCount =2
    inner class ViewHolder(private val binding: SampleitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: SampleResponseModel) {
            binding.sampleText.text=model.body
            binding.sampleText.setOnClickListener {
                onclick(model.body)
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
    @SuppressLint("NotifyDataSetChanged")
    override fun getItemCount(): Int {

        return  minOf(list.size,maxItemCount)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }
    fun updateMaxItemCount(newValue: Int) {
        maxItemCount=newValue
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }


}