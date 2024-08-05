package com.example.resumemaker.utils

import com.example.resumemaker.R
import com.example.resumemaker.databinding.ActivityBoardingScreenBinding
import com.example.resumemaker.databinding.BoardingitemBinding
import com.example.resumemaker.models.BoardingItems
import com.example.resumemaker.models.BoardingViewModel
import com.example.resumemaker.views.activities.BoardingScreen

object Helper {
    fun getBoardingItem():List<BoardingItems>{
        val list = ArrayList<BoardingItems>()
        list.add(
            BoardingItems(
                R.drawable.img1,
                "Craft Your Success Story",
                "Welcome to your shortcut for standout resumes and cover letters."
            )
        )
        list.add(
            BoardingItems(
                R.drawable.img2,
                "Let AI do your Job",
                "Easily create professional resumes and cover letters with AI. Our smart system highlights your strengths and fit job descriptions."
            )
        )
        list.add(
            BoardingItems(
                R.drawable.img3,
                "Reread the Job Advertisement",
                "The job advertisement you're replying to tells you exactly what the employer is looking for in a candidate."
            )
        )
        list.add(
            BoardingItems(
                R.drawable.img4,
                "Keep it short",
                "Unless you're a high level executive or scholar, your resume should be one page long"
            )
        )
        return list
    }

    fun checkDots(i:Int,binding: ActivityBoardingScreenBinding): BoardingViewModel {
        if (i==0)
        {
            return BoardingViewModel(binding.dot1.setImageResource(R.drawable.blue_dot),binding.dot2.setImageResource(R.drawable.grey_dot))

        }
        if (i==1)
        {
            return BoardingViewModel(binding.dot2.setImageResource(R.drawable.blue_dot),binding.dot1.setImageResource(R.drawable.grey_dot))

        }
        if (i==2)
        {
            return BoardingViewModel(binding.dot3.setImageResource(R.drawable.blue_dot),binding.dot2.setImageResource(R.drawable.grey_dot))

        }
        if (i==3)
        {
            return BoardingViewModel(binding.dot4.setImageResource(R.drawable.blue_dot),binding.dot3.setImageResource(R.drawable.grey_dot))
        }
        return BoardingViewModel(binding.dot1.setImageResource(R.drawable.blue_dot),binding.dot2.setImageResource(R.drawable.grey_dot))
    }


}