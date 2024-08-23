package com.pentabit.cvmaker.resumebuilder.views.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pentabit.cvmaker.resumebuilder.views.fragments.BoardingFragment

class ImagePagerAdapter(activity: AppCompatActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return BoardingFragment.getInstance(position)
    }
}