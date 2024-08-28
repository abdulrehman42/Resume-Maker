package com.pentabit.cvmaker.resumebuilder.views.fragments.download

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentPdfBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.views.adapter.download.DownloadImgAdapter
import com.pentabit.pentabitessentials.firebase.AppsKitSDK
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import java.io.File


class PdfFragment : BaseFragment<FragmentPdfBinding>() {
    override val inflate: Inflate<FragmentPdfBinding>
        get() = FragmentPdfBinding::inflate


    lateinit var templateAdapter: DownloadImgAdapter

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        setAdapter()
    }

    private fun setAdapter() {
        templateAdapter = DownloadImgAdapter(currentActivity()){
            DialogueBoxes.alertboxPdf(
                it.path,
                currentActivity())
        }

        val cw = ContextWrapper(AppsKitSDK.getInstance().context)


        // Get the base directory
        val directory = cw.getDir(AppsKitSDKPreferencesManager.getInstance().getStringPreferences(Constants.USER_ID), Context.MODE_PRIVATE)


        // Navigate to the subdirectory (headerDir/appDirectorySubName/IMAGES)
        val targetDirectory = File(directory, "Resume/PDFs")


        // Ensure the target directory exists
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs()
        }

        val load = targetDirectory.listFiles()?.toList()
        if (!load.isNullOrEmpty()
        ) {
            templateAdapter.submitList(load)
            binding.text.isGone = true
            binding.recyclerviewTemplete.apply {
                layoutManager = GridLayoutManager(requireActivity(), 2)
                adapter = templateAdapter
            }
        } else {
            binding.text.isGone = false
        }
    }

}