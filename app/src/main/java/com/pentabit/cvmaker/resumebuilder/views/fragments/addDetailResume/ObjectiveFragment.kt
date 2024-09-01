package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentObjectiveBinding
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SingleItemRequestModel
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ObjSampleAdapter
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectiveFragment : AddDetailsBaseFragment<FragmentObjectiveBinding>() {
    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private lateinit var objectivesSampleAdapter: ObjSampleAdapter
    private var list = ArrayList<SampleResponseModel>()

    override val inflate: Inflate<FragmentObjectiveBinding>
        get() = FragmentObjectiveBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        fetchObjectiveSamples()
        onclick()
        fetchProfileInfo()
    }

    private fun fetchProfileInfo() {
        addDetailResumeVM.getProfileDetail()
    }

    override fun observeLiveData() {
        addDetailResumeVM.getSamples.observe(this) {
            list = it as ArrayList<SampleResponseModel>
            setAdapter(it)
        }

        addDetailResumeVM.dataResponse.observe(this) {
            if (!it.objective.isNullOrEmpty())
                binding.objectiveTextInput.setText(it.objective)
        }

        addDetailResumeVM.data.observe(this) {
            binding.objectiveTextInput.setText(it.toString())
        }

    }

    private fun fetchObjectiveSamples() {
        addDetailResumeVM.getSample("objective")
    }

    private fun onclick() {
        binding.viewall.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(ObjectiveSample(list))
        }
    }

    private fun saveObjective() {
        addDetailResumeVM.editObjective(
            SingleItemRequestModel(binding.objectiveTextInput.text.toString())
        )
    }

    private fun setAdapter(sampleResponseModels: List<SampleResponseModel>) {
        objectivesSampleAdapter = ObjSampleAdapter(sampleResponseModels) {
            binding.objectiveTextInput.setText(it)
        }
        binding.sampleRecyclerview.adapter = objectivesSampleAdapter
    }

    private fun isConditionMet(): Boolean {
        return if (binding.objectiveTextInput.text.toString().trim().isEmpty()) {
            AppsKitSDKUtils.makeToast("please add objectives")
            false
        } else {
            true
        }
    }

    override fun csnMoveForward(): Boolean {
        return isConditionMet()
    }

    override fun onMoveNextClicked(): Boolean {
        if (isConditionMet()) {
            saveObjective()
        }
        return false
    }

}