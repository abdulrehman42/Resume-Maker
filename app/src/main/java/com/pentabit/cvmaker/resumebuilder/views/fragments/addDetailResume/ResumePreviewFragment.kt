package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentResumePreviewBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseDownload
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.shareAppMethod
import com.pentabit.cvmaker.resumebuilder.utils.Helper.saveHtmlAsPdf
import com.pentabit.cvmaker.resumebuilder.utils.ImageFileUtils
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.activities.ChoiceTemplate
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
            binding.resumePreviewImage.settings.apply {
                setSupportZoom(true)
                loadWithOverviewMode=true
            }
           binding.resumePreviewImage.loadData(it, "text/html", "UTF-8")

            binding.resumePreviewImage.setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    // Capture and save the WebView content as an image
                    val bitmap = captureWebView(view)
                    if (sharePref.readBoolean(Constants.IS_RESUME,false))
                    {
                        ImageFileUtils.getInstance().saveImageToHiddenStorage(bitmap,false,Constants.RESUME,80,sharePref.readString(Constants.PROFILE_ID).toString())
                    }else{
                        ImageFileUtils.getInstance().saveImageToHiddenStorage(bitmap,false,Constants.COVER_LETTER,80,sharePref.readString(Constants.PROFILE_ID).toString())

                    }
                   // saveBitmapAsImage(bitmap, "webview_image.png")
                }
            })
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

    fun captureWebView(webView: WebView): Bitmap {
        // Measure the WebView's content height
        // Create a bitmap with the WebView's content width and height
        val bitmap = Bitmap.createBitmap(
            webView.getWidth(),
            (webView.getContentHeight() * webView.getScale()).toInt(), Bitmap.Config.ARGB_8888
        )
        // Create a canvas to draw the WebView's content on the bitmap
        val canvas = Canvas(bitmap)
        webView.draw(canvas)
        return bitmap
    }

    private fun onclick() {

        binding.includeTool.backbtn.setOnClickListener {
            sharePref.deleteItemSharePref(Constants.IS_FIRST_TIME)
            currentActivity().finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            sharePref.deleteItemSharePref(Constants.TEMPLATE_ID)
            //sharePref.deleteItemSharePref(Constants.PROFILE_ID)

            currentActivity().finish()
        }
        binding.changeTemplate.setOnClickListener {
            val intent= Intent(currentActivity(), ChoiceTemplate::class.java)
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
                startActivity(Intent(currentActivity(), AddDetailResume::class.java))
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
                object : com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.StringValueDialogCallback {
                    override fun onButtonClick(value: String) {
                        if (value== Constants.JPG)
                        {

                        }else{
                            saveHtmlAsPdf(currentActivity(),value,getString(R.string.app_name))
                        }
                    }

                })

        }
    }

}
