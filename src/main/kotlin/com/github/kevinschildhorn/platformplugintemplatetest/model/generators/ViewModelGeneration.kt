package com.github.kevinschildhorn.platformplugintemplatetest.model.generators

fun createViewModel(
        packageName: String,
        viewModelName: String,
        entityName: String
) = """package $packageName.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class $viewModelName : ViewModel() {

    val ${entityName.toLowerCase()}LiveData = MutableLiveData<Any>()

    fun fetch${entityName.capitalize()}(){
        viewModelScope.launch {
            ${entityName.toLowerCase()}LiveData.postValue(null) // TODO
        }
    }
}
"""

fun createListViewModel(
        packageName: String,
        viewModelName: String,
        entityName: String
) = """package $packageName.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class $viewModelName : ViewModel() {

    val ${entityName.toLowerCase()}LiveData = MutableLiveData<List<Any>>()

    fun fetch${entityName.capitalize()}s(){
        viewModelScope.launch {
            ${entityName.toLowerCase()}LiveData.postValue(emptyList()) // TODO
        }
    }
}
"""