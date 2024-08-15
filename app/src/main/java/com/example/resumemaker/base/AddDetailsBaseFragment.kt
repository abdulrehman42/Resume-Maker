package com.example.resumemaker.base

import androidx.viewbinding.ViewBinding

abstract class AddDetailsBaseFragment<VB : ViewBinding>() : BaseFragment<VB>() {
    abstract fun csnMoveForward(): Boolean
}