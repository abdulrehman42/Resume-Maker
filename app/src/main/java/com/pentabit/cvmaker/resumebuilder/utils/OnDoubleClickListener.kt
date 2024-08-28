package com.pentabit.cvmaker.resumebuilder.utils

import android.os.SystemClock
import android.view.View

class OnDoubleClickListener(private val doubleClickAction: (View) -> Unit) : View.OnClickListener {
    private var lastClickTime: Long = 0
    private val DOUBLE_CLICK_TIME_DELTA: Long = 300 // Time in milliseconds

    override fun onClick(v: View) {
        val clickTime = SystemClock.elapsedRealtime()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            doubleClickAction(v)
        }
        lastClickTime = clickTime
    }
}