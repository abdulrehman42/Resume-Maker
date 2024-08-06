package com.example.resumemaker.utils

import com.example.resumemaker.R
import com.example.resumemaker.databinding.ActivityBoardingScreenBinding
import com.example.resumemaker.models.BoardingItems
import com.example.resumemaker.models.ProfileModel

object Helper {
    fun updateCircleMarker(binding: ActivityBoardingScreenBinding, position: Int) {
        when (position) {
            0 -> {
                binding.dot1.setImageResource(R.drawable.blue_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
            }
            1 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.blue_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
            }
            2 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.blue_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
            }
            3 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.blue_dot)
            }
        }
    }

    fun getProfileList():List<ProfileModel>{
        val list=ArrayList<ProfileModel>()
        list.add(ProfileModel("","Ali","UI/UX"))
        list.add(ProfileModel("","Abdul Rehman","Android Developer"))
        list.add(ProfileModel("","Numan","Laravel Developer"))
        list.add(ProfileModel("","Syed Ali","IOS Developer"))
        list.add(ProfileModel("","Zeeshan","Mobile App Developer"))
        list.add(ProfileModel("","Zubair","Electrical Engineer"))
        list.add(ProfileModel("","Hammad","chemical Engineer"))
        list.add(ProfileModel("","Abu Bakar","Mechanical Engineer"))
        list.add(ProfileModel("","Zafar Ali","Game Developer"))
        list.add(ProfileModel("","Ubaid","Helper"))
        list.add(ProfileModel("","Wali","SQA"))
        list.add(ProfileModel("","Mubarshir","Web Developer"))
        return list
    }

}