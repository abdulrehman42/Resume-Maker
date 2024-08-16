package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.SampleitemsBinding
import com.example.resumemaker.models.SampleModel

class ObjSampleAdapter(val context: Context,val list:List<SampleModel>,val onclick:(String)->Unit,val onselect:(Boolean)->Unit): RecyclerView.Adapter<ObjSampleAdapter.ViewHolder>() {
    var maxItemCount =2
    inner class ViewHolder(private val binding: SampleitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: SampleModel) {
            binding.sampleText.text=model.objectiveText
            binding.sampleText.setOnClickListener {
                onclick(model.objectiveText)
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

        if (minOf(list.size,maxItemCount)==list.size)
        {
            onselect(true)
        }
        if (minOf(list.size,maxItemCount)==2||minOf(list.size,maxItemCount)==0)
        {
            onselect(false)
        }

        return  minOf(list.size,maxItemCount)
    }
    /*override fun getItemCount(): Int {
        return  minOf(list.size,maxItemCount)
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }
    fun updateMaxItemCount(newValue: Int) {
        maxItemCount=newValue
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }


}