package com.example.resumemaker.utils

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import com.example.resumemaker.R
import com.example.resumemaker.databinding.ActivityBoardingScreenBinding
import com.example.resumemaker.models.BoardingItems
import com.example.resumemaker.models.EducationModel
import com.example.resumemaker.models.ExperienceModel
import com.example.resumemaker.models.ProfileModel
import com.example.resumemaker.models.ProfileModelData
import com.example.resumemaker.models.SampleModel
import com.example.resumemaker.models.SuggestionModel
import com.example.resumemaker.models.TemplateModel
import com.google.android.material.tabs.TabLayout

object Helper {
    fun updateCircleMarker(binding: ActivityBoardingScreenBinding, position: Int) {
        when (position) {
            0 -> {
                binding.dot1.setImageResource(R.drawable.blue_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
                binding.nextbtn.setText("Next")
                binding.skipbtn.isGone = false
            }

            1 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.blue_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
                binding.nextbtn.setText("Next")
                binding.skipbtn.isGone = false
            }

            2 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.blue_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
                binding.nextbtn.setText("Next")
                binding.skipbtn.isGone = false
            }

            3 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.blue_dot)
                binding.nextbtn.setText("Done")
                binding.skipbtn.isGone = true

            }
        }
    }

    fun getTabView(context: Context, position: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.customtablayout, null)
        val tabIcon = view.findViewById<ImageView>(R.id.nav_icon)
        val tabTitle = view.findViewById<TextView>(R.id.nav_label)

        when (position) {
            0 -> {
                tabIcon.setImageResource(R.drawable.info)
                tabTitle.text = "Info"
            }

            1 -> {
                tabIcon.setImageResource(R.drawable.objectives)
                tabTitle.text = "Objectives"
            }

            2 -> {
                tabIcon.setImageResource(R.drawable.education)
                tabTitle.text = "Education"
            }

            3 -> {
                tabIcon.setImageResource(R.drawable.skill)
                tabTitle.text = "Skill"
            }

            4 -> {
                tabIcon.setImageResource(R.drawable.experience)
                tabTitle.text = "Experience"
            }

            5 -> {
                tabIcon.setImageResource(R.drawable.referrence)
                tabTitle.text = "reference"
            }
        }
        return view
    }

    private fun setTabSelected(tab: TabLayout.Tab, isSelected: Boolean) {
        val tabView = tab.customView
        val textView = tabView?.findViewById<TextView>(R.id.nav_label)
        textView?.setTextColor(if (isSelected) Color.WHITE else Color.GRAY)
        // Optionally, change other attributes like background, text style, etc.
    }

    fun getProfileList(): List<ProfileModel> {
        val list = ArrayList<ProfileModel>()
        list.add(ProfileModel("", "Ali", "UI/UX"))
        list.add(ProfileModel("", "Abdul Rehman", "Android Developer"))
        list.add(ProfileModel("", "Numan", "Laravel Developer"))
        list.add(ProfileModel("", "Syed Ali", "IOS Developer"))
        list.add(ProfileModel("", "Zeeshan", "Mobile App Developer"))
        list.add(ProfileModel("", "Zubair", "Electrical Engineer"))
        list.add(ProfileModel("", "Hammad", "chemical Engineer"))
        list.add(ProfileModel("", "Abu Bakar", "Mechanical Engineer"))
        list.add(ProfileModel("", "Zafar Ali", "Game Developer"))
        list.add(ProfileModel("", "Ubaid", "Helper"))
        list.add(ProfileModel("", "Wali", "SQA"))
        list.add(ProfileModel("", "Mubarshir", "Web Developer"))
        return list
    }

    fun getProfileList1(): List<ProfileModelData> {
        val list = ArrayList<ProfileModelData>()
        list.add(
            ProfileModelData(
                "Ali", "Android Developer", ProfileModelData.Detail(
                    "hey im ali", "Male", "Lahore", "My objec to achieve goal",
                    getSuggestions(),
                    getExperienceList1(),
                    getDegreeList(),
                    getCertificateist(),
                )
            )
        )
        /*
                list.add(ProfileModelData("", "Abdul Rehman", "Android Developer"))
                list.add(ProfileModelData("", "Numan", "Laravel Developer"))
                list.add(ProfileModelData("", "Syed Ali", "IOS Developer"))
                list.add(ProfileModelData("", "Zeeshan", "Mobile App Developer"))
                list.add(ProfileModelData("", "Zubair", "Electrical Engineer"))
                list.add(ProfileModelData("", "Hammad", "chemical Engineer"))
                list.add(ProfileModelData("", "Abu Bakar", "Mechanical Engineer"))
                list.add(ProfileModelData("", "Zafar Ali", "Game Developer"))
                list.add(ProfileModelData("", "Ubaid", "Helper"))
                list.add(ProfileModelData("", "Wali", "SQA"))
                list.add(ProfileModelData("", "Mubarshir", "Web Developer"))
        */
        return list
    }


    fun getSampleList(): List<SampleModel> {
        val list = ArrayList<SampleModel>()
        list.add(SampleModel("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "))
        list.add(SampleModel("Brus aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "))
        list.add(SampleModel("Alif aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "))
        list.add(SampleModel("Dick aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "))
        list.add(SampleModel("Locifer aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "))
        list.add(SampleModel("Gamer aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatu Duis aute irure dolor in reprehenderit in voluptate velit esse cillum "))
        return list
    }

    fun getDegreeList(): List<EducationModel> {
        val list = ArrayList<EducationModel>()
        list.add(EducationModel("Lahore Garrision University", "BSSE", "2019", "2023"))
        list.add(EducationModel("University Of Lahore", "BSIT", "2012", "2016"))
        list.add(EducationModel("University Of Punjab", "BSCS", "2009", "2013"))
        return list
    }

    fun getCertificateist(): List<EducationModel> {
        val list = ArrayList<EducationModel>()
        list.add(EducationModel("CCNA", "Lahore Garrision University", "2019", "2023"))
        list.add(EducationModel("Android Developer", "University Of Lahore", "2012", "2016"))
        return list
    }

    fun getExperienceList(): List<EducationModel> {
        val list = ArrayList<EducationModel>()
        list.add(EducationModel("ML Bench", "Android Developer", "2023", "2024"))
        list.add(EducationModel("Synare System", "Android Developer", "2024", "2024"))
        list.add(EducationModel("Pentabit Lab", "Android Developer", "2024", "2024"))
        return list
    }

    fun getExperienceList1(): List<ExperienceModel> {
        val list = ArrayList<ExperienceModel>()
        list.add(ExperienceModel("Android Developer", "ML Bench", "August2023-Mar2024", "Lahore"))
        list.add(
            ExperienceModel(
                "Android Developer",
                "Synare System",
                "April2024-Jul2024",
                "Lahore"
            )
        )
        list.add(ExperienceModel("Android Developer", "Pentabit Lab", "Aug2024-Present", "Lahore"))
        return list
    }

    fun getreferrenceList(): List<EducationModel> {
        val list = ArrayList<EducationModel>()
        list.add(EducationModel("Ali", "Backend Developer", "ali@gmail.com", ""))
        list.add(EducationModel("wahid", "FrontEnd Developer", "wahid@gmail.com", ""))
        return list
    }

    fun getSuggestions(): List<SuggestionModel> {
        val list = ArrayList<SuggestionModel>()
        list.add(SuggestionModel("Android Developer"))
        list.add(SuggestionModel("Helper"))
        list.add(SuggestionModel("Laravel Developer"))
        list.add(SuggestionModel("Backend Developer"))
        list.add(SuggestionModel("FrontEnd Developer"))
        list.add(SuggestionModel("SQA"))
        list.add(SuggestionModel("Chemical Knowledge"))
        return list
    }

    fun getTemplateImages(): List<TemplateModel> {
        val list = ArrayList<TemplateModel>()
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        return list
    }

    fun achievementList(): List<EducationModel> {
        val list = ArrayList<EducationModel>()
        list.add(
            EducationModel(
                "LawAdvat",
                "Artificial Intelligence and robotics research",
                "2023",
                "2024"
            )
        )
        list.add(EducationModel("MyMufti App", "Product & Api Services", "2022", "2024"))
        return list
    }

    fun projectsList(): List<EducationModel> {
        val list = ArrayList<EducationModel>()
        list.add(
            EducationModel(
                "LawAdvat",
                "Artificial Intelligence and robotics research",
                "",
                ""
            )
        )
        list.add(EducationModel("MyMufti App", "Product & Api Services", "", ""))
        return list
    }

    fun getLanuages(): List<SuggestionModel> {
        val list = ArrayList<SuggestionModel>()
        list.add(SuggestionModel("English"))
        list.add(SuggestionModel("Urdu"))
        return list
    }

    fun getInterests(): List<SuggestionModel> {
        val list = ArrayList<SuggestionModel>()
        list.add(SuggestionModel("Gaming"))
        list.add(SuggestionModel("Movie"))
        list.add(SuggestionModel("Season"))
        list.add(SuggestionModel("Self Motivated"))
        return list
    }


    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    /* val endIconView = findViewById<ImageView>(com.google.android.material.R.id.text_input_end_icon)
               val params = endIconView.layoutParams as FrameLayout.LayoutParams
               params.gravity = Gravity.BOTTOM or Gravity.END
               params.marginEnd = 8.dpToPx(context) // Adjust margin to your needs
               params.bottomMargin = 8.dpToPx(context)
               endIconView.layoutParams = params*/

    fun getFilePathFromUri(context: Context,uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return null
    }
}
