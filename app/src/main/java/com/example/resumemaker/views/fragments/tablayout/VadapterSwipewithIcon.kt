package com.example.resumemaker.views.fragments.tablayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class VadapterSwipewithIcon(getFragmentManager: FragmentManager): FragmentPagerAdapter(getFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val fragmentArrayList = ArrayList<Fragment>()
    private val fragmenttitle = ArrayList<String>()
    private val fragmentIcon = ArrayList<Int>()


    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    fun addFragment(fragment: Fragment?, title: String?, icon: Int) {
        fragmentArrayList.add(fragment!!)
        fragmenttitle.add(title!!)
        fragmentIcon.add(icon)
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return fragmenttitle[position]
    }
    fun getFragment(position: Int): Fragment {
        return fragmentArrayList[position]
    }
    fun checkFragments(): List<String> {
        return fragmenttitle
    }

    fun getFragmentName(position: Int): String {
        return when (position) {
            6 -> "Interests"
            7 -> "Language"
            8 -> "Projects"
            9 -> "Achievements"
            else -> "Unknown"
        }
    }

    fun removeFragment(title: String) {
        val index = fragmenttitle.indexOf(title)
        if (index != -1) {
            fragmentArrayList.removeAt(index)
            fragmenttitle.removeAt(index)
            fragmentIcon.removeAt(index)
            notifyDataSetChanged()
        }
    }
}