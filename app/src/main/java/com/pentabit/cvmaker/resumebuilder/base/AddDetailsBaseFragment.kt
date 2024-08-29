package com.pentabit.cvmaker.resumebuilder.base

import androidx.viewbinding.ViewBinding

abstract class AddDetailsBaseFragment<VB : ViewBinding>() : BaseFragment<VB>() {
    abstract fun csnMoveForward(): Boolean
    abstract fun onMoveNextClicked(): Boolean
}