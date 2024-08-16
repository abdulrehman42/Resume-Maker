package com.example.resumemaker.views.fragments.choose

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentPremiumBinding
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.models.response.TemplateResponseModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.example.resumemaker.views.activities.LoginActivity
import com.example.resumemaker.views.adapter.TempResListAdpter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PremiumFragment : BaseFragment<FragmentPremiumBinding>() {
    lateinit var fromCalled:String
    var iscalled=false
    val templateViewModel by viewModels<TemplateViewModel>()
    override val inflate: Inflate<FragmentPremiumBinding>
        get() = FragmentPremiumBinding::inflate




    override fun observeLiveData() {
        templateViewModel.templateResponse.observe(viewLifecycleOwner) {
            //  kProgressHUD.dismiss()
            when (it) {
                is NetworkResult.Success<*> -> {
                    it.data?.let { data ->
                        data.data.let { temp ->
                            try {
                                setAdapter(temp)

                            } catch (e: NullPointerException) {
                                print(e)
                            }
                        }
                    }
                    templateViewModel.templateResponse.value = null
                }

                is NetworkResult.Loading<*> -> {
                    // kProgressHUD.show()
                }

                is NetworkResult.Error<*> -> {
                    currentActivity().showToast(it.message)
                    templateViewModel.templateResponse.value = null
                }

                else -> {
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        fromCalled= sharePref.readString(Constants.FRAGMENT_NAME).toString()
        if (!iscalled){
            callApi()
        }
    }
    private fun callApi() {
        templateViewModel.fetchTemplates("resume","standard",10,1)
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
                                currentActivity().replaceChoiceFragment(R.id.nav_add_detail)
                            } else {
                                currentActivity().replaceChoiceFragment(R.id.nav_add_detail_coverletter)
                            }

                        }
                    }
                })
        }
        templateAdapter.setOnItemClickCallback {
            if (fromCalled == Constants.CV) {
                sharePref.writeString(Constants.FRAGMENT_NAME, Constants.CHOSE_TEMPLATE)
                startActivity(Intent(currentActivity(), LoginActivity::class.java))
            }
            if (fromCalled == Constants.PROFILE) {
                currentActivity().replaceChoiceFragment(R.id.nav_profileFragment)
            }
        }
        binding.recyclerviewTemplete.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = templateAdapter
        }
    }

}