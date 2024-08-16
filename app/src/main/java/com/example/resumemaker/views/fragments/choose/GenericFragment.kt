package com.example.resumemaker.views.fragments.choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resumemaker.databinding.FragmentBasicBinding
import com.example.resumemaker.views.adapter.TempResListAdpter

class GenericFragment (
    private val adapter: TempResListAdpter
) : Fragment() {

    private lateinit var binding: FragmentBasicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewTemplete.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewTemplete.adapter = adapter
    }


}