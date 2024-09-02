package com.pentabit.cvmaker.resumebuilder.views.activities

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentResumePreviewBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseDownload
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.shareAppMethod
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.firebase.AppsKitSDK
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import com.pentabit.pentabitessentials.utils.FILE_NAME_PATTERN
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class ResumePreviewActivity : BaseActivity() {
    lateinit var templateViewModel: TemplateViewModel
    lateinit var binding: FragmentResumePreviewBinding
    var cv = ""
    var isResume = false
    var id = ""
    var templateId = ""
    private var screenId = ScreenIDs.PREVIEW_RESUME

    lateinit var targetDirectoryPdf: File
    lateinit var targetDirectoryImage: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentResumePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        observer()

    }

    private fun observer() {
        templateViewModel.loadingState.observe(this) {
            if (it) {
                binding.loader.isGone = false
            } else {
                binding.loader.isGone = true
            }
        }
        templateViewModel.getString.observe(this) {
            cv = it
            binding.resumePreviewImage.settings.apply {
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false

                loadWithOverviewMode = true
                useWideViewPort = true
            }
            binding.resumePreviewImage.loadDataWithBaseURL(
                Constants.BASE_URL_PRODUCTION,
                it,
                "text/html",
                "UTF-8",
                null
            )

        }
    }

    private fun initView() {
        binding.includeTool.textView.text = getString(R.string.preview)
        templateViewModel = ViewModelProvider(this)[TemplateViewModel::class.java]
        isResume = intent.getBooleanExtra(Constants.IS_RESUME,false)
        if (!isResume) {
            binding.editText.setText(getString(R.string.editcover))
            screenId = ScreenIDs.PREVIEW_COVER_LETTER
        }
        binding.resumePreviewImage.webViewClient = WebViewClient()
        onApi()
        onclick()
        manageAds()
    }



    override fun attachViewMode() {

    }

    override fun onResume() {
        super.onResume()
        (this as AdBaseActivity).askAdOnFragment(screenId)
    }

    override fun onInternetConnectivityChange(p0: Boolean?) {
    }

    override fun getScreenId(): ScreenIDs {
        return screenId
    }

    private fun manageAds() {
        AppsKitSDKAdsManager.showBanner(
            this,
            binding.bannerAd,
            placeholder = Utils.createAdKeyFromScreenId(screenId)
        )
    }

    private fun onApi() {
        id = AppsKitSDKPreferencesManager.getInstance().getStringPreferences(Constants.PROFILE_ID)
            .toString()
        templateId =
            AppsKitSDKPreferencesManager.getInstance().getStringPreferences(Constants.TEMPLATE_ID)
                .toString()
        val isResume = AppsKitSDKPreferencesManager.getInstance()
            .getBooleanPreferences(Constants.IS_RESUME, false)
        if (isResume) {
            templateViewModel.getResumePreview(id, templateId)
        } else {
            templateViewModel.getCLPreview(id, templateId)

        }
    }

    private fun onclick() {

        binding.includeTool.backbtn.setOnClickListener {
            finish()
        }

       onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        binding.changeTemplate.setOnClickListener {
            val intent = Intent(this, ChoiceTemplate::class.java)
            if (isResume) {
                intent.putExtra(Constants.IS_RESUME, true)
            } else {
                intent.putExtra(Constants.IS_RESUME, false)
            }
            startActivity(intent)
            finish()
        }
        binding.editProfile.setOnClickListener {
            if (isResume) {
                val intent = Intent(this, AddDetailResume::class.java)
                intent.putExtra(Constants.IS_EDIT, id)
                startActivity(intent)
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.homeConstraint.setOnClickListener {
            finish()
        }
        binding.includeTool.share.setOnClickListener {
            shareAppMethod(this)
        }
        binding.downloadConstraint.setOnClickListener {
            alertboxChooseDownload(this,
                object :
                    com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.StringValueDialogCallback {
                    override fun onButtonClick(value: String) {
                        if (value == Constants.JPG) {
                            convertWebViewToImage()
                        } else {
                            saveWebViewAsPdf(binding.resumePreviewImage)
                        }
                    }

                })

        }
    }

    private fun convertWebViewToImage() {
        val cw = ContextWrapper(AppsKitSDK.getInstance().context)


        // Get the base directory
        val directory = cw.getDir(
            AppsKitSDKPreferencesManager.getInstance().getStringPreferences(Constants.USER_ID),
            Context.MODE_PRIVATE
        )


        // Navigate to the subdirectory (headerDir/appDirectorySubName/IMAGES)
        if (isResume) {
            targetDirectoryImage = File(directory, "Resume/JPGs")

        } else {
            targetDirectoryImage = File(directory, "CoverLetter/JPGs")

        }


        // Ensure the target directory exists
        if (!targetDirectoryImage.exists()) {
            targetDirectoryImage.mkdirs()
        }

        // Define the file name and path
        val fileName =
            SimpleDateFormat(FILE_NAME_PATTERN, Locale.US).format(Calendar.getInstance().time);
        val pdfFile = File(targetDirectoryImage, "$fileName.jpg")
        saveBitmapAsImage(captureWebView(binding.resumePreviewImage), pdfFile)
    }

    fun saveBitmapAsImage(bitmap: Bitmap?, file: File?) {
        var outputStream: FileOutputStream? = null
        if (bitmap == null) {
            AppsKitSDKUtils.makeToast("Fail to  save")
            return
        }
        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                outputStream?.close()
                AppsKitSDKUtils.makeToast("Successfully Saved")
            } catch (e: IOException) {
                AppsKitSDKUtils.makeToast("Fail to  save ${e.message}")
                e.printStackTrace()
            }
        }
    }


    fun captureWebView(webView: WebView): Bitmap? {
        // Measure the webview's content size
        webView.measure(
            View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED
            ),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        webView.layout(0, 0, webView.measuredWidth, webView.measuredHeight)

        // Create a bitmap with the same size as the WebView's content
        val bitmap = Bitmap.createBitmap(
            webView.measuredWidth,
            webView.measuredHeight, Bitmap.Config.ARGB_8888
        )

        // Draw the WebView's content to the canvas
        val canvas = Canvas(bitmap)
        webView.draw(canvas)
        return bitmap
    }


    fun saveWebViewAsPdf(webView: WebView) {
        val cw = ContextWrapper(AppsKitSDK.getInstance().context)


        // Get the base directory
        val directory = cw.getDir(
            AppsKitSDKPreferencesManager.getInstance().getStringPreferences(Constants.USER_ID),
            Context.MODE_PRIVATE
        )


        // Navigate to the subdirectory (headerDir/appDirectorySubName/IMAGES)
        //  val targetDirectory = File(directory, "Resume/PDFs")
        if (isResume) {
            targetDirectoryPdf = File(directory, "Resume/PDFs")
        } else {
            targetDirectoryPdf = File(directory, "CoverLetter/PDFs")
        }

        // Ensure the target directory exists
        if (!targetDirectoryPdf.exists()) {
            targetDirectoryPdf.mkdirs()
        }


        // Define the file name and path
        val fileName =
            SimpleDateFormat(FILE_NAME_PATTERN, Locale.US).format(Calendar.getInstance().time);
        val pdfFile = File(targetDirectoryPdf, "$fileName.pdf")
        // Create a PdfDocument
        val pdfDocument = PdfDocument()
        val pageNumber = 1
        // Measure the WebView's content height
        webView.measure(
            View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            ),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        // Start a page in the PDF
        val pageInfo = PageInfo.Builder(
            webView.measuredWidth,
            webView.measuredHeight,
            pageNumber
        ).create()
        val page = pdfDocument.startPage(pageInfo)
        // Render the WebView's content onto the PDF page
        webView.draw(page.canvas)
        // Finish the page
        pdfDocument.finishPage(page)
        // Write the PDF to the file
        try {
            pdfDocument.writeTo(FileOutputStream(pdfFile))
            pdfDocument.close()
            AppsKitSDKUtils.makeToast("Successfully Saved")
        } catch (e: IOException) {
            AppsKitSDKUtils.makeToast("Error ${e.message}")
            e.printStackTrace()
        }
    }

}