package com.pentabit.cvmaker.resumebuilder.callbacks;

import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse;

import java.util.List;

public interface OnLookUpResult {
    void onLookUpResult(List<LookUpResponse> list);
}
