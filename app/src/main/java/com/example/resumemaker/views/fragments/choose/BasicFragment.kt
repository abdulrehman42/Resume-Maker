package com.example.resumemaker.views.fragments.choose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.callbacks.OnTemplateSelected
import com.example.resumemaker.R
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentBasicBinding
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.models.api.TemplateModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.example.resumemaker.views.activities.AddDetailResume
import com.example.resumemaker.views.activities.LoginActivity
import com.example.resumemaker.views.adapter.TempResListAdpter
import dagger.hilt.android.AndroidEntryPoint


class BasicFragment(val list: List<TemplateModel>?) : BaseFragment<FragmentBasicBinding>() {
    private val templateAdapter = TempResListAdpter()
    private var callback: OnTemplateSelected? = null
    override val inflate: Inflate<FragmentBasicBinding>
        get() = FragmentBasicBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        setAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTemplateSelected) {
            callback = context
        }
    }

    override fun observeLiveData() {

    }

    private fun setAdapter() {
        templateAdapter.submitList(list)
        templateAdapter.setOnEyeItemClickCallback {
            sharePref.writeString(Constants.TEMPLATE_ID,it.id.toString())
            DialogueBoxes.alertbox(
                it.path,
                currentActivity(),
                object : DialogueBoxes.DialogCallback {
                    override fun onButtonClick(isConfirmed: Boolean) {
                        if (isConfirmed) {
                            if (it.contentType==1)
                            {
                                DialogueBoxes.alertboxImport(currentActivity())
                            }else{
                                callback?.onTemplateSelected(it)
                            }
                        } else {
                            currentActivity().replaceChoiceFragment(R.id.nav_add_detail_coverletter)
                        }
                    }
                }
            )
        }

        templateAdapter.setOnItemClickCallback {
            if (it.contentType==1)
            {
                DialogueBoxes.alertboxImport(currentActivity())
            }
            sharePref.writeString(Constants.TEMPLATE_ID,it.id.toString())
            callback?.onTemplateSelected(it)
        }

        binding.recyclerviewTemplete.apply {
            adapter = templateAdapter
        }
    }

}