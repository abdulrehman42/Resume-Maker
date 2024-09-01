package com.pentabit.cvmaker.resumebuilder.views.fragments.download

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentPdfBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.application.ResumeMakerApplication
import com.pentabit.cvmaker.resumebuilder.views.activities.PreviewImageResumeActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.download.DownloadImgAdapter
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import java.io.File


class DownloadViewPaperFragment(val path: String) : BaseFragment<FragmentPdfBinding>() {
    override val inflate: Inflate<FragmentPdfBinding>
        get() = FragmentPdfBinding::inflate


    lateinit var pdfAdapter: DownloadImgAdapter

    lateinit var jpgAdapter: DownloadImgAdapter
    lateinit var pdfDirectory: File
    lateinit var jpgDirectory: File

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        setDirectories()
        setAdapter()
        handleClicks()
    }

    private fun handleClicks() {
        binding.jpg.setOnClickListener {
            jpgAdapter.isPdf=false
            handleButtonsClicks(binding.jpg, binding.pdf)
            setAdapterToRecyclerView(jpgAdapter)
        }
        binding.pdf.setOnClickListener {
            handleButtonsClicks(binding.pdf, binding.jpg)
            setAdapterToRecyclerView(pdfAdapter)
        }
    }

    private fun handleButtonsClicks(selectedView: TextView, unselectedView: TextView) {
        selectedView.background =
            ContextCompat.getDrawable(ResumeMakerApplication.instance, R.drawable.whitbground)
        unselectedView.background =
            ContextCompat.getDrawable(ResumeMakerApplication.instance, R.drawable.greybgradius)
    }

    private fun setDirectories() {
        val cw = ContextWrapper(ResumeMakerApplication.instance)

        // Get the base directory
        val directory = cw.getDir(
            AppsKitSDKPreferencesManager.getInstance().getStringPreferences(
                Constants.USER_ID
            ), Context.MODE_PRIVATE
        )

        pdfDirectory = getDirectory(directory, "$path/PDFs")
        jpgDirectory = getDirectory(directory, "$path/JPGs")

    }


    private fun getDirectory(parentDir: File, requiredChildDir: String): File {

        val targetDirectory = File(parentDir, requiredChildDir)

        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs()
        }
        return targetDirectory
    }

    private fun setAdapter() {
        pdfAdapter = DownloadImgAdapter {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(
                FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().packageName + ".provider",
                    it
                ), "application/pdf"
            )
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no PDF viewer is installed
                Toast.makeText(requireContext(), "No application available to view PDF", Toast.LENGTH_SHORT)
                    .show()
            }
//
//            startActivity(
//                Intent(
//                    currentActivity(),
//                    PreviewPdfResumeActivity::class.java
//                ).putExtra("path", it.absolutePath)
//            )
        }
        jpgAdapter = DownloadImgAdapter {
            startActivity(
                Intent(
                    currentActivity(),
                    PreviewImageResumeActivity::class.java
                ).putExtra("path", it.absolutePath)
            )
        }

        handleAdapterList(pdfAdapter, pdfDirectory)
        handleAdapterList(jpgAdapter, jpgDirectory)
        setAdapterToRecyclerView(jpgAdapter)
    }


    private fun setAdapterToRecyclerView(adapter: DownloadImgAdapter) {
        AppsKitSDKUtils.setVisibility(adapter.currentList.isEmpty(), binding.text)
        binding.recyclerviewTemplete.adapter = adapter
    }

    private fun handleAdapterList(adapter: DownloadImgAdapter, directory: File) {
        val load = directory.listFiles()?.toList()
        if (!load.isNullOrEmpty()
        ) {
            adapter.submitList(load)
        }
    }

}