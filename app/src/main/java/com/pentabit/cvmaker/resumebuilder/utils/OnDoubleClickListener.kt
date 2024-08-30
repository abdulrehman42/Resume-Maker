package com.pentabit.cvmaker.resumebuilder.utils

import android.os.SystemClock
import android.view.View

class OnDoubleClickListener(
    private val onDoubleClick: (View) -> Unit,
    private val onSingleClick: (View) -> Unit = {}
) : View.OnClickListener {

    private var lastClickTime: Long = 0
    private val DOUBLE_CLICK_TIME_DELTA: Long = 300 // Milliseconds

    override fun onClick(v: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(v)
        } else {
            onSingleClick(v)
        }
        lastClickTime = clickTime
    }
}