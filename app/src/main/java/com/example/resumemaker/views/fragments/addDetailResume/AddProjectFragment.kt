package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddProjectBinding
import com.example.resumemaker.models.request.addDetailResume.Project
import com.example.resumemaker.models.request.addDetailResume.ProjectRequest
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper.dpToPx
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddProjectFragment : BaseFragment<FragmentAddProjectBinding>() {
    lateinit var addDetailResumeVM :AddDetailResumeVM
    override val inflate: Inflate<FragmentAddProjectBinding>
        get() = FragmentAddProjectBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.projectResponse.observe(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM=ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text=getString(R.string.add_project)
        val data=sharePref.readProfileProject()
        if (data!=null)
        {
            binding.projectedittext.setText(data.title)
            binding.descriptionedittext.setText(data.description)
        }
        binding.projecttitleTextInputLayout.apply {
            // Adjust the layout parameters of the end icon
            endIconMode = TextInputLayout.END_ICON_CUSTOM // If not already set
            val endIconView = findViewById<ImageView>(com.google.android.material.R.id.text_input_end_icon)

            val params = endIconView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM or Gravity.END
            params.marginEnd = 8.dpToPx(context) // Adjust margin to your needs
            params.bottomMargin = 8.dpToPx(context)
            endIconView.layoutParams = params
        }
        onclick()

    }



    private fun onclick() {
        binding.savebtn.setOnClickListener {
            if (isConditionMet()) {
                apiCall()
                MainScope().launch {
                    delay(2000)
                    addDetailResumeVM.isHide.value = true
                    currentActivity().onBackPressedDispatcher.onBackPressed()
                }
            }else{
                currentActivity().showToast(getString(R.string.field_missing_error))

            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            addDetailResumeVM.isHide.value=true
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }
        /*requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value=true
        }*/

    }
    fun isConditionMet(): Boolean {
        return !binding.projectedittext.text.toString().isNullOrEmpty()&&
                !binding.descriptionedittext.text.toString().isNullOrEmpty()
    }
    private fun apiCall() {
        val project = listOf(
            Project(binding.descriptionedittext.text.toString(),binding.projectedittext.text.toString())
        )

        val projectRequest = ProjectRequest(projects = project)

        addDetailResumeVM.editProjects(
            sharePref.readString(Constants.PROFILE_ID).toString(),projectRequest)
    }
}