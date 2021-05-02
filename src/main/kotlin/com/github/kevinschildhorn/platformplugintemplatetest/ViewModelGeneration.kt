package com.github.kevinschildhorn.platformplugintemplatetest

import com.android.tools.idea.wizard.template.ProjectTemplateData

fun createViewModel(
        packageName: String,
        viewModelName: String,
        entityName: String,
        projectData: ProjectTemplateData
) = """
package $packageName

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import ${projectData.applicationPackage}.R;

class $viewModelName : ViewModel() {

    val ${entityName.toLowerCase()}LiveData = MutableLiveData<>()

    fun get${entityName.capitalize()}(){
        viewModelScope.launch {
            ${entityName.toLowerCase()}LiveData.postValue(/**/)
        }
    }
}
"""