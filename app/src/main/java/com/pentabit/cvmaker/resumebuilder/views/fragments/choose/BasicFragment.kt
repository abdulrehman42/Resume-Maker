package com.pentabit.cvmaker.resumebuilder.views.fragments.choose

import android.content.Context
import android.os.Bundle
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.callbacks.OnTemplateSelected
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentBasicBinding
import com.pentabit.cvmaker.resumebuilder.models.api.TemplateModel
import com.pentabit.cvmaker.resumebuilder.views.adapter.TempResListAdpter


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
            sharePref.writeString(com.pentabit.cvmaker.resumebuilder.utils.Constants.TEMPLATE_ID,it.id.toString())
            com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertbox(
                it.path,
                currentActivity(),
                object : com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.DialogCallback {
                    override fun onButtonClick(isConfirmed: Boolean) {
                        if (isConfirmed) {
                            if (it.contentType==1)
                            {
                                com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxImport(currentActivity())
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
                com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxImport(currentActivity())
            }
            sharePref.writeString(com.pentabit.cvmaker.resumebuilder.utils.Constants.TEMPLATE_ID,it.id.toString())
            callback?.onTemplateSelected(it)
        }

        binding.recyclerviewTemplete.apply {
            adapter = templateAdapter
        }
    }

}