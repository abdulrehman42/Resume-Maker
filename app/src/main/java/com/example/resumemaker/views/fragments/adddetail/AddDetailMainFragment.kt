package com.example.resumemaker.views.fragments.adddetail

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.viewpager.widget.ViewPager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.AddmorealertdialogueBinding
import com.example.resumemaker.databinding.FragmentAddDetailMainBinding
import com.example.resumemaker.models.TablayoutModel
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.fragments.tablayout.Vadapterswipe
import com.google.android.material.tabs.TabLayout

class AddDetailMainFragment : BaseFragment<FragmentAddDetailMainBinding>() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    var itemArraylist=ArrayList<TablayoutModel>()
    lateinit var vadapter:Vadapterswipe
    override val inflate: Inflate<FragmentAddDetailMainBinding>
        get() = FragmentAddDetailMainBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        setUpTablayout()
        onclick()
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.floatingActionButton.setOnClickListener {
            alertbox()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        binding.includeTool.textView.text = "Add Detail"
    }

    private fun setUpTablayout() {
        viewPager = binding.viewPager
        tabLayout = binding.tabLayoutAdddetail
        vadapter =
            Vadapterswipe(childFragmentManager) // Use childFragmentManager for fragments within a fragment
        addItems(itemArraylist)
        /*for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                tab.customView = Helper.getTabView(requireActivity(), i)
            }
            if (i==5)
            {
            }
        }*/


        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.let {

                    if (it.position >= 3) {
                        // Open a dialog or a new activity to display additional tabs
                        binding.floatingActionButton.isGone=false
                    }else{
                        binding.floatingActionButton.isGone=true

                    }
                }
                val icon = tab.icon
                icon?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val icon = tab.icon
                icon?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.off_white),
                    PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection if needed

            }
        }

        tabLayout.addOnTabSelectedListener(tabSelectedListener)
    }
    fun addItems(list: List<TablayoutModel>){

        vadapter.addFragment(InformationFragment(), "\nInfo")
        vadapter.addFragment(ObjectiveFragment(), "\nObjectives")
        vadapter.addFragment(EducationFragment(), "\nEducation")
        vadapter.addFragment(SkillFragment(), "\nSkill")
        vadapter.addFragment(ExperienceFragment(), "\nExperience")
        vadapter.addFragment(ReferrenceFragment(), "\nReference")
        vadapter.addFragment(InterestFragment(), "\nInterest")
        vadapter.addFragment(LanguageFragment(), "\nLanguage")
        vadapter.addFragment(ProjectFragment(), "\nProjects")
        vadapter.addFragment(AchievementFragment(), "\nAchievements")


        list?.let {
            for (i in list)
            {
                vadapter.addFragment(i.fragment,i.title)
            }

           /* tabLayout.getTabAt(6)?.setIcon(it.get(0).image)
            tabLayout.getTabAt(7)?.setIcon(it.get(1).image)
            tabLayout.getTabAt(8)?.setIcon(it.get(2).image)
*/
        }
        viewPager.adapter = vadapter
        tabLayout.setupWithViewPager(viewPager)
       /* tabLayout.getTabAt(0)?.setIcon(R.drawable.info)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.objectives)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.education)
        tabLayout.getTabAt(3)?.setIcon(R.drawable.skill)
        tabLayout.getTabAt(4)?.setIcon(R.drawable.experience)
        tabLayout.getTabAt(5)?.setIcon(R.drawable.referrence)
*/

    }

    fun alertbox(
    ) {


        val binding1=AddmorealertdialogueBinding.inflate(layoutInflater)
        val dialogBuilder = Dialog(currentActivity(),R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding1.root)
       binding1.switchid.setOnClickListener {
           if (binding1.switchid.isChecked) {
               itemArraylist.add(TablayoutModel(InterestFragment(),"\nInterest",R.drawable.interest))
               addItems(itemArraylist)

           }
       }
        binding1.switchid1.setOnClickListener {
            if (binding1.switchid1.isChecked) {
                itemArraylist.add(TablayoutModel(LanguageFragment(),"\nLanguage",R.drawable.language))
                addItems(itemArraylist)

            }
        }
        binding1.switchid2.setOnClickListener {
            if (binding1.switchid2.isChecked) {
                itemArraylist.add(TablayoutModel(ProjectFragment(),"\nProject",R.drawable.project))
                addItems(itemArraylist)

            }
        }
        binding1.switchid3.setOnClickListener {
            if (binding1.switchid3.isChecked) {
                itemArraylist.add(TablayoutModel(AchievementFragment(),"\nAchievement",R.drawable.achievment))
                addItems(itemArraylist)
            }
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

}