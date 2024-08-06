package com.example.resumemaker.views.fragments.tablayout

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeableViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // Disable touch interception to prevent swiping
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Disable touch events to prevent swiping
        return false
    }
}