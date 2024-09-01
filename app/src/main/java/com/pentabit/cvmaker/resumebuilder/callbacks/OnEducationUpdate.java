package com.pentabit.cvmaker.resumebuilder.callbacks;

import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse;

import java.util.List;

public interface OnEducationUpdate {
    void onEducationUpdated(List<ProfileModelAddDetailResponse.UserQualification> userQualificationsList);
}
