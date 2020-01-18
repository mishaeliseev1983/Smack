package com.melyseev.smack.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Please Log In"
    }
    val text: LiveData<String> = _text


    /*
    fun sendMessageBtnClicked(view: View){

    }*/
}