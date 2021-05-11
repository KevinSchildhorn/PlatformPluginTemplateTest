package com.github.kevinschildhorn.platformplugintemplatetest.model.generators

import com.android.tools.idea.wizard.template.ProjectTemplateData

fun createNativeViewModel(
        packageName: String,
        viewModelName: String,
        entityName: String,
        projectData: ProjectTemplateData
) = """package $packageName

import co.touchlab.stately.ensureNeverFrozen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import ${projectData.applicationPackage}.MainScope

class Native$viewModelName (
    private val ${entityName.toLowerCase()}Update: (Any) -> Unit
) : KoinComponent {

    private val scope = MainScope(Dispatchers.Main, null)

    init {
        ensureNeverFrozen()
    }

    fun fetch${entityName.capitalize()}(){
        scope.launch {
            ${entityName.toLowerCase()}Update(null) // TODO
        }
    }

    fun onDestroy() {
        scope.onDestroy()
    }
}
"""