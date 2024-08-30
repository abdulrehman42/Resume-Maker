package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.view.View
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
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.activities.ChoiceTemplate
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
class ResumePreviewFragment : BaseFragment<FragmentResumePreviewBinding>() {
    lateinit var templateViewModel: TemplateViewModel
    var cv = ""
    var isResume = false
    var id = ""
    var templateId = ""

    lateinit var targetDirectoryPdf:File
    lateinit var targetDirectoryImage:File
    override val inflate: Inflate<FragmentResumePreviewBinding>
        get() = FragmentResumePreviewBinding::inflate

    override fun observeLiveData() {
        templateViewModel.loadingState.observe(currentActivity()) {
            if (it) {
                binding.loader.isGone = false
            } else {
                binding.loader.isGone = true
            }
        }
        templateViewModel.getString.observe(currentActivity()) {
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

//    private fun captureWebView(view: WebView): Bitmap {
//        // Get the width and height of the WebView content
//        val width = view.width
//        val height = view.contentHeight
//
//        // Create a bitmap with the size of the WebView content
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//
//        // Draw the WebView content onto the canvas
//        view.draw(canvas)
//
//        return bitmap
//    }

    fun convertWebViewToPdf(context: Context, webView: WebView, fileName: String) {
        // Create a PrintDocumentAdapter from the WebView
        val printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter(fileName)

        // Create a PrintManager instance
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

        // Set up print attributes
        val printAttributes = PrintAttributes.Builder()
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .setMediaSize(PrintAttributes.MediaSize.NA_LETTER) // Use A4 or other sizes if needed
            .setResolution(PrintAttributes.Resolution("default", "default", 300, 300))
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()

        // Create a PrintJob with the provided name and attributes
        printManager.print(fileName, printAdapter, printAttributes)
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.preview)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAd,
            placeholder = ""
        )
        isResume = AppsKitSDKPreferencesManager.getInstance()
            .getBooleanPreferences(Constants.IS_RESUME, true)
        if (!isResume) {
            binding.editText.setText(getString(R.string.editcover))
        }
        templateViewModel = ViewModelProvider(currentActivity())[TemplateViewModel::class.java]
        binding.resumePreviewImage.webViewClient = WebViewClient()
        onApi()
        onclick()
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

    /* fun captureWebView(webView: WebView): Bitmap {
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
     }*/

    private fun onclick() {

        binding.includeTool.backbtn.setOnClickListener {
            /*sharePref.deleteItemSharePref(Constants.TEMPLATE_ID)
            sharePref.deleteItemSharePref(Constants.PROFILE_ID)
*/
            currentActivity().finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            /*sharePref.deleteItemSharePref(Constants.TEMPLATE_ID)
            sharePref.deleteItemSharePref(Constants.PROFILE_ID)
*/
            currentActivity().finish()
        }

        binding.changeTemplate.setOnClickListener {
            val intent = Intent(currentActivity(), ChoiceTemplate::class.java)
            if (isResume) {
                intent.putExtra(Constants.IS_RESUME, true)
            } else {
                intent.putExtra(Constants.IS_RESUME, false)
            }
            startActivity(intent)
            currentActivity().finish()
        }
        binding.editProfile.setOnClickListener {
            if (isResume) {
                val intent = Intent(currentActivity(), AddDetailResume::class.java)
                intent.putExtra(Constants.IS_EDIT, id)
                startActivity(intent)
            } else {
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
        if (isResume)
        {
            targetDirectoryImage= File(directory, "Resume/JPGs")

        }else{
            targetDirectoryImage= File(directory, "CoverLetter/JPGs")

        }


        // Ensure the target directory exists
        if (!targetDirectoryImage.exists()) {
            targetDirectoryImage.mkdirs()
        }


        // Create a directory to save the PDF
//        val directory: File = File(Environment.getExternalStorageDirectory(), "PDFs")
//        if (!directory.exists()) {
//            directory.mkdirs()
//        }
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
        if (isResume)
        {
            targetDirectoryPdf = File(directory, "Resume/PDFs")
        }else{
            targetDirectoryPdf=File(directory,"CoverLetter/PDFs")
        }

        // Ensure the target directory exists
        if (!targetDirectoryPdf.exists()) {
            targetDirectoryPdf.mkdirs()
        }


        // Create a directory to save the PDF
//        val directory: File = File(Environment.getExternalStorageDirectory(), "PDFs")
//        if (!directory.exists()) {
//            directory.mkdirs()
//        }
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