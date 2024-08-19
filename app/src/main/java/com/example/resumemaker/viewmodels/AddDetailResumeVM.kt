package com.example.resumemaker.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddDetailResumeVM @Inject constructor():ViewModel() {

    var fragment=MutableLiveData<Fragment>()
    var isHide=MutableLiveData<Boolean>()


}