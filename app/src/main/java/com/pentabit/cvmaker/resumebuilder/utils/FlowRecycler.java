package com.pentabit.cvmaker.resumebuilder.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class FlowRecycler extends recycler.coverflow.RecyclerCoverFlow {
    public FlowRecycler(Context context) {
        super(context);
    }

    public FlowRecycler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = velocityX / 4;
        return super.fling(velocityX, velocityY);
    }
}
