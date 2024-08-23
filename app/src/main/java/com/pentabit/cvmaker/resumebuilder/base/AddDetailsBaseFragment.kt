package com.pentabit.cvmaker.resumebuilder.base

import androidx.viewbinding.ViewBinding

abstract class AddDetailsBaseFragment<VB : ViewBinding>() : com.pentabit.cvmaker.resumebuilder.base.BaseFragment<VB>() {
    abstract fun csnMoveForward(): Boolean
}