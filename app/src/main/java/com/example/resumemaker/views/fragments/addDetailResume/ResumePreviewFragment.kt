package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentResumePreviewBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes.alertboxChooseDownload
import com.example.resumemaker.utils.DialogueBoxes.shareAppMethod
import com.example.resumemaker.viewmodels.CoverLetterVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResumePreviewFragment : BaseFragment<FragmentResumePreviewBinding>() {
    lateinit var coverLetterVM: CoverLetterVM
    override val inflate: Inflate<FragmentResumePreviewBinding>
        get() = FragmentResumePreviewBinding::inflate

    override fun observeLiveData() {
        coverLetterVM.getString.observe(currentActivity()){
            binding.resumePreviewImage.loadUrl(it)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        coverLetterVM = ViewModelProvider(currentActivity())[CoverLetterVM::class.java]
        binding.includeTool.textView.text = getString(R.string.preview)
        onApi()
        onclick()
    }

    private fun onApi() {
        val id = arguments?.getString(Constants.PROFILE_ID).toString()
        val templateId = sharePref.readString(Constants.TEMPLATE_ID).toString()
        val isResume = sharePref.readBoolean(Constants.IS_RESUME, false)
        if (isResume) {
            coverLetterVM.getResumePreview(id, templateId)
        } else {
            coverLetterVM.getCLPreview(id, templateId)

        }
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().finish()

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            currentActivity().finish()
        }
        binding.includeTool.share.setOnClickListener {
            shareAppMethod(currentActivity())
        }
        binding.downloadConstraint.setOnClickListener {
            alertboxChooseDownload(currentActivity())
        }

    }

}
