package com.pentabit.cvmaker.resumebuilder.views.fragments.download

import android.os.Bundle
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentJPGBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.ImageFileUtils
import com.pentabit.cvmaker.resumebuilder.views.adapter.download.DownloadImgAdapter
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager


class JPGFragment : BaseFragment<FragmentJPGBinding>() {
    override val inflate: Inflate<FragmentJPGBinding>
        get() = FragmentJPGBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        setAdapter()

    }

    private fun setAdapter() {
        var name = Constants.RESUME
        if (AppsKitSDKPreferencesManager.getInstance()
                .getBooleanPreferences(Constants.IS_RESUME, false)
        ) {
            name = Constants.RESUME
        } else {
            name = Constants.COVER_LETTER
        }
//        if (ImageFileUtils.getInstance().loadImageFromHiddenStorage(AppsKitSDKPreferencesManager.getInstance().getStringPreferences(Constants.PROFILE_ID),name).size!=0)
//        {
//            binding.text.isGone=true
////            val templateAdapter= DownloadImgAdapter(ImageFileUtils.getInstance().loadImageFromHiddenStorage(sharePref.readString(Constants.PROFILE_ID),name))
////            binding.recyclerviewTemplete.apply {
////                layoutManager= GridLayoutManager(requireActivity(),2)
////                adapter = templateAdapter
////            }
//        }else{
//            binding.text.isGone=false
//        }

    }


}