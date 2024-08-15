package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isGone
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentObjectiveBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.utils.Helper.dpToPx
import com.example.resumemaker.views.adapter.ObjSampleAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class ObjectiveFragment : AddDetailsBaseFragment<FragmentObjectiveBinding>() {
    lateinit var objSampleAdapter: ObjSampleAdapter
    override val inflate: Inflate<FragmentObjectiveBinding>
        get() = FragmentObjectiveBinding::inflate

    override fun observeLiveData() {

    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.objectiveTextInputLayout.apply {
            // Adjust the layout parameters of the end icon
            endIconMode = TextInputLayout.END_ICON_CUSTOM // If not already set
            val endIconView = findViewById<ImageView>(com.google.android.material.R.id.text_input_end_icon)

            val params = endIconView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM or Gravity.END
            params.marginEnd = 8.dpToPx(context) // Adjust margin to your needs
            params.bottomMargin = 8.dpToPx(context)
            endIconView.layoutParams = params
        }
        setAdapter()
        onclick()

    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout

        binding.viewall.setOnClickListener {
            objSampleAdapter.updateMaxItemCount(Helper.getSampleList().size)

            MainScope().launch {
                binding.viewall.isGone=true
                binding.viewLess.isGone=false
            }
        }
        binding.viewless.setOnClickListener {
            objSampleAdapter.updateMaxItemCount(2)
            MainScope().launch {
                binding.viewall.isGone=false
                binding.viewless.isGone=true
            }



        }
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(0)!!.select()
        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(2)!!.select()
        }
    }

    private fun setAdapter() {

        objSampleAdapter=ObjSampleAdapter(currentActivity(),Helper.getSampleList()){
            binding.objectiveTextInput.setText(it)
        }
        binding.sampleRecyclerview.adapter=objSampleAdapter
    }
    fun isConditionMet(): Boolean {
        return !binding.objectiveTextInput.text.toString().isNullOrEmpty()
    }
}