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
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.unity3d.services.core.webview.WebView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResumePreviewFragment : BaseFragment<FragmentResumePreviewBinding>() {
    lateinit var templateViewModel: TemplateViewModel
    override val inflate: Inflate<FragmentResumePreviewBinding>
        get() = FragmentResumePreviewBinding::inflate

    override fun observeLiveData() {
        templateViewModel.getString.observe(currentActivity()){
            binding.resumePreviewImage.loadDataWithBaseURL(Constants.BASE_URL, it, "text/html", "UTF-8", null)

        }
    }

    override fun init(savedInstanceState: Bundle?) {
        templateViewModel = ViewModelProvider(currentActivity())[TemplateViewModel::class.java]
        binding.resumePreviewImage.webViewClient=WebViewClient()
        binding.includeTool.textView.text = getString(R.string.preview)
        onApi()
        onclick()
    }

    private fun onApi() {
        val id = sharePref.readString(Constants.PROFILE_ID).toString()
        val templateId = sharePref.readString(Constants.TEMPLATE_ID).toString()
        val isResume = sharePref.readBoolean(Constants.IS_RESUME, false)
        if (isResume) {
            templateViewModel.getResumePreview(id, templateId)
        } else {
            templateViewModel.getCLPreview(id, templateId)

        }
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            sharePref.deleteAllSharedPrefs()
            currentActivity().finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            sharePref.deleteAllSharedPrefs()
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
