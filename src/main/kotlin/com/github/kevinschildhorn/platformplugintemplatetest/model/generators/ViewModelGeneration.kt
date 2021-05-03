package com.github.kevinschildhorn.platformplugintemplatetest.model.generators

import com.android.tools.idea.wizard.template.ProjectTemplateData

fun createViewModel(
        packageName: String,
        viewModelName: String,
        entityName: String,
        projectData: ProjectTemplateData
) = """
package $packageName

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class $viewModelName : ViewModel() {

    val ${entityName.toLowerCase()}LiveData = MutableLiveData<Any>()

    fun fetch${entityName.capitalize()}(){
        viewModelScope.launch {
            ${entityName.toLowerCase()}LiveData.postValue(/**/) // TODO
        }
    }
}
"""