package com.github.kevinschildhorn.platformplugintemplatetest.model.recipes

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.android.tools.idea.wizard.template.impl.activities.common.addAllKotlinDependencies
import com.github.kevinschildhorn.platformplugintemplatetest.listeners.MyProjectManagerListener.Companion.projectInstance
import com.github.kevinschildhorn.platformplugintemplatetest.model.generators.*
import com.github.kevinschildhorn.platformplugintemplatetest.save
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiManager

fun RecipeExecutor.kmmListFragmentSetup(
        moduleData: ModuleTemplateData,
        packageName: String,
        entityName: String,
        layoutName: String,
        fragmentName: String,
        adapterName: String,
        adapterLayoutName: String,
        viewModelInclude: Boolean,
        viewModelName: String
) {
    val (projectData) = moduleData
    val project = projectInstance ?: return

    addAllKotlinDependencies(moduleData)

    val virtualFiles = ProjectRootManager.getInstance(project).contentSourceRoots
    val virtSrc = virtualFiles.first { it.path.contains("android/src/main/java") }
    val virtRes = virtualFiles.first { it.path.contains("android/src/main/res") }
    val directorySrc = PsiManager.getInstance(project).findDirectory(virtSrc)!!
    val directoryRes = PsiManager.getInstance(project).findDirectory(virtRes)!!

    val iosVirtSrc = virtualFiles.first { it.path.contains("src/iosMain") }
    val sharedDirectorySrc = PsiManager.getInstance(project).findDirectory(iosVirtSrc)!!


    // Fragment
    createListFragment(packageName, fragmentName, layoutName, projectData, viewModelInclude, viewModelName, entityName)
            .save(directorySrc, packageName, "${fragmentName}.kt")

    createListFragmentLayout(packageName, fragmentName, entityName)
            .save(directoryRes, "layout", "${layoutName}.xml")

    // Layout
    createListViewModel(packageName, viewModelName, entityName)
            .save(directorySrc, packageName, "${viewModelName}.kt")

    val packageNameNew = packageName.replace(".android","")
    createNativeViewModel(packageName, viewModelName, entityName, projectData)
            .save(sharedDirectorySrc, "$packageNameNew.viewmodels", "Native${viewModelName}.kt")

    // View Adapter
    createViewAdapter(packageName, entityName)
            .save(directorySrc, packageName, "${adapterName}.kt")

    createViewAdapterLayout()
            .save(directorySrc, "layout", "${adapterLayoutName}.xml")
}