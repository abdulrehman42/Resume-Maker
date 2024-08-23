package com.pentabit.cvmaker.resumebuilder.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.pentabit.cvmaker.resumebuilder.utils.SharePref
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.activities.BoardingScreen
import com.pentabit.cvmaker.resumebuilder.views.activities.ChoiceTemplate
import com.pentabit.cvmaker.resumebuilder.views.activities.DownloadActivity
import com.pentabit.cvmaker.resumebuilder.views.activities.LoginActivity
import com.pentabit.cvmaker.resumebuilder.views.activities.MainActivity
import com.pentabit.cvmaker.resumebuilder.views.activities.ProfileActivity

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>() : Fragment() {

    abstract val inflate: Inflate<VB>
    private var _binding: VB? = null
    val binding get() = _binding!!
    lateinit var sharePref: SharePref
    private lateinit var mActivity: com.pentabit.cvmaker.resumebuilder.base.BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = obtainViewModel(viewModelClass)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        sharePref = SharePref.mInstence!!
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
        observeLiveData()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = when (context) {
            // is AuthActivity -> context
            is MainActivity -> context
            is LoginActivity -> context
            is BoardingScreen -> context
            is ChoiceTemplate -> context
            is DownloadActivity -> context
            is ProfileActivity -> context
            is AddDetailResume -> context
            else -> context as BaseActivity
        }
    }

    open fun showToast(message: String?) {
        currentActivity().showToast(message)
    }

    open fun currentActivity() = mActivity

    abstract fun observeLiveData()

    abstract fun init(savedInstanceState: Bundle?)


    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
    }
}