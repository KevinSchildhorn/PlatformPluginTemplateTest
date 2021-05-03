package com.github.kevinschildhorn.platformplugintemplatetest.model.recipes

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.android.tools.idea.wizard.template.impl.activities.common.addAllKotlinDependencies
import com.github.kevinschildhorn.platformplugintemplatetest.listeners.MyProjectManagerListener.Companion.projectInstance
import com.github.kevinschildhorn.platformplugintemplatetest.model.generators.*
import com.github.kevinschildhorn.platformplugintemplatetest.save
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiManager

fun RecipeExecutor.kmmFragmentSetup(
        moduleData: ModuleTemplateData,
        packageName: String,
        entityName: String,
        layoutName: String,
        fragmentName: String,
        viewModelInclude: Boolean,
        viewModelName: String
) {
    val (projectData) = moduleData
    val project = projectInstance ?: return

    addAllKotlinDependencies(moduleData)

    val virtualFiles = ProjectRootManager.getInstance(project).contentSourceRoots
    val virtSrc = virtualFiles.first { it.name.toLowerCase() == "android" && it.path.contains("src") }
    val virtRes = virtualFiles.first { it.name.toLowerCase() == "android" && it.path.contains("res") }
    val directorySrc = PsiManager.getInstance(project).findDirectory(virtSrc)!!
    val directoryRes = PsiManager.getInstance(project).findDirectory(virtRes)!!

    val virtSrc2 = virtualFiles.first { it.name.toLowerCase() == "shared" && it.path.contains("src") }
    val sharedDirectorySrc = PsiManager.getInstance(project).findDirectory(virtSrc2)!!

    if (viewModelInclude) {
        createFragmentWithViewModel(packageName, fragmentName, layoutName, projectData, viewModelName, entityName)
                .save(directorySrc, packageName, "${fragmentName}.kt")

        createViewModel(packageName, viewModelName, entityName, projectData)
                .save(directorySrc, "$packageName/viewmodels", "${viewModelName}.kt")

        createNativeViewModel(packageName, viewModelName, entityName, projectData)
                .save(sharedDirectorySrc, "iosMain/$packageName/viewmodels", "Native${viewModelName}.kt")
    }else{
        createFragment(packageName, fragmentName, layoutName, projectData)
                .save(directorySrc, packageName, "${fragmentName}.kt")
    }

    createFragmentLayout(packageName, fragmentName)
            .save(directoryRes, "layout", "${layoutName}.xml")




}