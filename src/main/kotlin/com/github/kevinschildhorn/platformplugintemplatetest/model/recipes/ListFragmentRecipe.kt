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
        viewModelInclude: Boolean,
        viewModelName: String
) {
    val (projectData) = moduleData
    val project = projectInstance ?: return

    addAllKotlinDependencies(moduleData)

    val virtualFiles = ProjectRootManager.getInstance(project).contentSourceRoots
    val virtSrc = virtualFiles.first { it.path.contains("src") }
    val virtRes = virtualFiles.first { it.path.contains("res") }
    val directorySrc = PsiManager.getInstance(project).findDirectory(virtSrc)!!
    val directoryRes = PsiManager.getInstance(project).findDirectory(virtRes)!!

    createListFragment(packageName, fragmentName, layoutName, projectData, viewModelInclude, viewModelName, entityName)
            .save(directorySrc, packageName, "${fragmentName}.kt")

    createListFragmentLayout(packageName, fragmentName, entityName)
            .save(directoryRes, "layout", "${layoutName}.xml")

    if (viewModelInclude) {
        createViewModel(packageName, viewModelName, entityName, projectData)
                .save(directorySrc, packageName, "${viewModelName}.kt")
    }
}