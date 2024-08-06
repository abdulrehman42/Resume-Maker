package com.example.resumemaker.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.resumemaker.R
import com.example.resumemaker.databinding.FragmentBoardingBinding


class BoardingFragment : Fragment() {

    companion object {
        private const val ARG_POSITION = "ARG_POSITION"

        fun getInstance(position: Int) = BoardingFragment().apply {
            arguments = bundleOf(ARG_POSITION to position)
        }
    }

    private lateinit var binding: FragmentBoardingBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_POSITION)
        val onBoardingTitles = requireContext().resources.getStringArray(R.array.on_boarding_titles)
        val onBoardingTexts = requireContext().resources.getStringArray(R.array.on_boarding_texts)
        val onBoardingImages = getOnBoardAssetsLocation()
        with(binding) {
            onBoardingImage.setImageResource(onBoardingImages[position])
            onBoardingTextTitle.text = onBoardingTitles[position]
            onBoardingTextMsg.text = onBoardingTexts[position]
        }
    }

    private fun getOnBoardAssetsLocation(): List<Int> {
        val onBoardAssets: MutableList<Int> = ArrayList()
        onBoardAssets.add(R.drawable.img1)
        onBoardAssets.add(R.drawable.img2)
        onBoardAssets.add(R.drawable.img3)
        onBoardAssets.add(R.drawable.img4)
        return onBoardAssets
    }

}