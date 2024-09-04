package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentObjectiveSampleBinding
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ObjSampleAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectiveSample(val list: ArrayList<SampleResponseModel>?) :
    BaseFragment<FragmentObjectiveSampleBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    val screenId = ScreenIDs.ADD_OBJECTIVE

    override val inflate: Inflate<FragmentObjectiveSampleBinding>
        get() = FragmentObjectiveSampleBinding::inflate

    override fun observeLiveData() {

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    private fun setAdapter() {
        val objSampleAdapter = list?.let {
            ObjSampleAdapter(it) {
                addDetailResumeVM.data.value = it
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        list?.let { objSampleAdapter?.updateMaxItemCount(it.size) }
        binding.objSampleRecyclerview.adapter = objSampleAdapter
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.apply {
            textView.text = getString(R.string.samples)
            setAdapter()
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        manageAds()
    }

    private fun manageAds() {
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.banner,
            Utils.createAdKeyFromScreenId(screenId)
        )
    }


}