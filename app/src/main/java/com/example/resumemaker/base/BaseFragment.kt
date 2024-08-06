package com.example.resumemaker.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.resumemaker.views.activities.MainActivity
import com.example.resumemaker.utils.SharePref
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.activities.MainActivty
import com.example.resumemaker.views.activities.ProfileActivity

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>() : Fragment() {

    abstract val inflate: Inflate<VB>
    private var _binding: VB? = null
    val binding get() = _binding!!
    lateinit var sharePref: SharePref
    private lateinit var mActivity: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = obtainViewModel(viewModelClass)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
//        sharePref = SharePref.mInstence!!
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
            is MainActivty->context
            is ChoiceTemplate->context
            is ProfileActivity->context
            else -> context as BaseActivity
        }
    }

    /*open fun hideLoadingBar() {
        mActivity.hideLoadingBar()
    }

    open fun showLoadingBar() {
        mActivity.showLoadingBar()
    }
*/
    open fun showToast(message: String?) {
        currentActivity().showToast(message)
    }

    open fun currentActivity() = mActivity

    abstract fun observeLiveData()
    abstract fun init(savedInstanceState: Bundle?)

/*
    fun getStringArgument(key: String) = arguments?.getString(key)
    fun getIntArgument(key: String) = arguments?.getInt(key, -1)
    fun getBooleanArgument(key: String) = arguments?.getBoolean(key)
    fun <T> getParcelableArgument(key: String) = arguments?.getParcelable<Parcelable>(key) as T
*/

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
    }
}