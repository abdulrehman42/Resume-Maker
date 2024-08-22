package com.example.resumemaker.views.fragments.addDetailResume

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentResumePreviewBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.DialogueBoxes.alertboxChooseDownload
import com.example.resumemaker.utils.DialogueBoxes.shareAppMethod
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.utils.Helper.saveHtmlAsPdf
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.example.resumemaker.views.activities.AddDetailResume
import com.example.resumemaker.views.activities.ChoiceTemplate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResumePreviewFragment : BaseFragment<FragmentResumePreviewBinding>() {
    lateinit var templateViewModel: TemplateViewModel
    var cv = ""
    var isResume=false
    override val inflate: Inflate<FragmentResumePreviewBinding>
        get() = FragmentResumePreviewBinding::inflate

    override fun observeLiveData() {
        templateViewModel.loadingState.observe(currentActivity()){
            if (it)
            {
                binding.loader.isGone=false
            }else{
                binding.loader.isGone=true
            }
        }
        templateViewModel.getString.observe(currentActivity()) {
            cv = it
           binding.resumePreviewImage.loadData(it, "text/html", "UTF-8")
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.preview)
        isResume=sharePref.readBoolean(Constants.IS_RESUME,true)
        if (!isResume)
        {
            binding.editText.setText(getString(R.string.editcover))
        }
        templateViewModel = ViewModelProvider(currentActivity())[TemplateViewModel::class.java]
        binding.resumePreviewImage.webViewClient = WebViewClient()
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
        binding.changeTemplate.setOnClickListener {
            val intent= Intent(currentActivity(),ChoiceTemplate::class.java)
            if (isResume)
            {
                intent.putExtra(Constants.IS_RESUME,true)
            }else{
                intent.putExtra(Constants.IS_RESUME,false)
            }
            startActivity(intent)
            currentActivity().finish()
        }
        binding.editProfile.setOnClickListener{
            if (isResume)
            {
                startActivity(Intent(currentActivity(),AddDetailResume::class.java))
            }else{
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.homeConstraint.setOnClickListener {
            currentActivity().finish()
        }
        binding.includeTool.share.setOnClickListener {
            shareAppMethod(currentActivity())
        }
        binding.downloadConstraint.setOnClickListener {
            alertboxChooseDownload(currentActivity(),
                object : DialogueBoxes.StringValueDialogCallback {
                    override fun onButtonClick(value: String) {
                        if (value==Constants.JPG)
                        {
                         Helper.saveHtmlToInternalStorage(binding.resumePreviewImage,cv,currentActivity(),getString(R.string.app_name))
                        }else{
                            saveHtmlAsPdf(currentActivity(),value,getString(R.string.app_name))
                        }
                    }

                })

        }
    }

}
