package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.viewpager.widget.ViewPager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.AddmorealertdialogueBinding
import com.example.resumemaker.databinding.FragmentAddDetailMainBinding
import com.example.resumemaker.models.TablayoutModel
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
                    val currentFragment = vadapter.getFragment(viewPager.currentItem)
                    if (currentFragment is InformationFragment) {
                        if (currentFragment.isConditionMet()) {
                            viewPager.currentItem = tab.position  // Allow tab switch
                        } else {
                            tabLayout.getTabAt(viewPager.currentItem)?.select()
                        }
                    }
                    if (currentFragment is ObjectiveFragment) {
                        if (currentFragment.isConditionMet()) {
                            viewPager.currentItem = tab.position  // Allow tab switch
                        } else {
                            tabLayout.getTabAt(viewPager.currentItem)?.select()
                        }
                    }

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
        vadapter.addFragment(SkillFragment(), "\nSkills")
        vadapter.addFragment(ExperienceFragment(), "\nExperience")
        vadapter.addFragment(ReferrenceFragment(), "\nReference")
       /* vadapter.addFragment(InterestFragment(), "\nInterest")
        vadapter.addFragment(LanguageFragment(), "\nLanguage")
        vadapter.addFragment(ProjectFragment(), "\nProjects")
        vadapter.addFragment(AchievementFragment(), "\nAchievements")
*/

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

        if (vadapter.checkFragments().contains("\nInterests"))
        {
            binding1.switchid.isChecked=true
        }
        if (vadapter.checkFragments().contains("\nLanguage"))
        {
            binding1.switchid1.isChecked=true

        }
        if (vadapter.checkFragments().contains("\nProjects"))
        {
            binding1.switchid2.isChecked=true

        }
        if (vadapter.checkFragments().contains("\nAchievements"))
        {
            binding1.switchid3.isChecked=true

        }
       binding1.switchid.setOnClickListener {
           if (binding1.switchid.isChecked) {
               vadapter.addFragment(InterestFragment(),"\nInterest")
               vadapter.notifyDataSetChanged()
               //tabLayout.addTab(tabLayout.newTab().setText("Interest"))
           }
       }
        binding1.switchid1.setOnClickListener {
             if (binding1.switchid1.isChecked) {
                 vadapter.addFragment(LanguageFragment(),"\nLanguage")
                 vadapter.notifyDataSetChanged()
            //     tabLayout.addTab(tabLayout.newTab().setText("Language"))
             }
        }
        binding1.switchid2.setOnClickListener {
            if (binding1.switchid2.isChecked) {
              vadapter.addFragment(ProjectFragment(),"\nProject")
                 vadapter.notifyDataSetChanged()
            //     tabLayout.addTab(tabLayout.newTab().setText("Project"))

            }
        }
        binding1.switchid3.setOnClickListener {
            if (binding1.switchid3.isChecked) {
                vadapter.addFragment(AchievementFragment(),"\nAchievement")
                vadapter.notifyDataSetChanged()
            //    tabLayout.addTab(tabLayout.newTab().setText("Achievement"))
            }
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

}