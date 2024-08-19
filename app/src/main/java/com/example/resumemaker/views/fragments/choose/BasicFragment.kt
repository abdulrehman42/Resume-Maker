package com.example.resumemaker.views.fragments.choose

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentBasicBinding
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.models.response.TemplateResponseModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.example.resumemaker.views.activities.AddDetailResume
import com.example.resumemaker.views.activities.LoginActivity
import com.example.resumemaker.views.adapter.TempResListAdpter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicFragment : AddDetailsBaseFragment<FragmentBasicBinding>() {
    lateinit var templateViewModel: TemplateViewModel // by viewModels<TemplateViewModel>()
    override val inflate: Inflate<FragmentBasicBinding>
        get() = FragmentBasicBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        templateViewModel = ViewModelProvider(requireActivity())[TemplateViewModel::class]
    }


    override fun observeLiveData() {
        templateViewModel.templateResponse.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let { data ->
                        data.data?.let { temp ->
                            try {
                                templateViewModel.dataMap[templateViewModel.name.value.toString()] =
                                    it.data
                                setAdapter(temp)
                            } catch (e: NullPointerException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }

                is NetworkResult.Loading -> {
                }

                is NetworkResult.Error -> {
                    currentActivity().showToast(it.message)
                }

                else -> {
                }
            }
        }
        templateViewModel.name.observe(viewLifecycleOwner) {
            callApi(templateViewModel.name.value.toString())
        }
    }

    private fun callApi(name: String) {
        if (templateViewModel.dataMap.containsKey(name)) {
            val storedValue = templateViewModel.dataMap[name]
            setAdapter(storedValue!!.data)
        } else {
            templateViewModel.fetchTemplates("resume", name, 10, 1)
        }
    }

    private fun setAdapter(temp: List<TemplateResponseModel.Data>) {
        val fromCalled = sharePref.readString(Constants.FRAGMENT_NAME)
        val templateAdapter = TempResListAdpter()
        templateAdapter.submitList(temp)

        templateAdapter.setOnEyeItemClickCallback {
            DialogueBoxes.alertbox(
                it.path,
                currentActivity(),
                object : DialogueBoxes.DialogCallback {
                    override fun onButtonClick(isConfirmed: Boolean) {
                        if (isConfirmed) {
                            if (fromCalled == Constants.CV) {
                                currentActivity().startActivity(
                                    Intent(
                                        currentActivity(),
                                        AddDetailResume::class.java
                                    )
                                )
                            } else {
                                currentActivity().replaceChoiceFragment(R.id.nav_add_detail_coverletter)
                            }
                        }
                    }
                }
            )
        }

        templateAdapter.setOnItemClickCallback {
            if (fromCalled == Constants.CV) {
                sharePref.writeString(Constants.FRAGMENT_NAME, Constants.CHOSE_TEMPLATE)
                startActivity(Intent(currentActivity(), LoginActivity::class.java))
            } else if (fromCalled == Constants.PROFILE) {
                currentActivity().replaceChoiceFragment(R.id.nav_profileFragment)
            }
        }

        binding.recyclerviewTemplete.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = templateAdapter
        }
    }

    override fun csnMoveForward(): Boolean {
        return false
    }
}