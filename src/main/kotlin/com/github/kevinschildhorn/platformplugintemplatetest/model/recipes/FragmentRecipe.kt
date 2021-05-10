package com.github.kevinschildhorn.platformplugintemplatetest.model.recipes

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.android.tools.idea.wizard.template.impl.activities.common.addAllKotlinDependencies
import com.github.kevinschildhorn.platformplugintemplatetest.listeners.MyProjectManagerListener.Companion.projectInstance
import com.github.kevinschildhorn.platformplugintemplatetest.model.generators.*
import com.github.kevinschildhorn.platformplugintemplatetest.save
import com.github.kevinschildhorn.platformplugintemplatetest.addKoinModule
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiManager
import com.intellij.openapi.diagnostic.Logger

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

    val log = Logger.getInstance("debug")
    val virtualFiles = ProjectRootManager.getInstance(project).contentSourceRoots

    log.info("Creating Fragment! $packageName - $entityName - $layoutName - $fragmentName - $viewModelName")
    virtualFiles.forEach {
        log.info("${it.name} - ${it.path} - ${it.presentableName}")
    }
    val virtSrc = virtualFiles.first { it.path.contains("android/src/main/java") }
    val virtRes = virtualFiles.first { it.path.contains("android/src/main/res") }
    val directorySrc = PsiManager.getInstance(project).findDirectory(virtSrc)!!
    val directoryRes = PsiManager.getInstance(project).findDirectory(virtRes)!!

    val iosVirtSrc = virtualFiles.first { it.path.contains("src/iosMain") }
    val sharedDirectorySrc = PsiManager.getInstance(project).findDirectory(iosVirtSrc)!!

    if (viewModelInclude) {
        log.info("Creating Fragment With ViewModel")
        createFragmentWithViewModel(packageName, fragmentName, layoutName, projectData, viewModelName, entityName)
                .save(directorySrc, packageName, "${fragmentName}.kt")

        log.info("Creating ViewModel")
        createViewModel(packageName, viewModelName, entityName)
                .save(directorySrc, "$packageName.viewmodel", "${viewModelName}.kt")

        log.info("CreatingNativeViewModel")
        val packageNameNew = packageName.replace(".android","")
        createNativeViewModel(packageName, viewModelName, entityName, projectData)
                .save(sharedDirectorySrc, "$packageNameNew.viewmodels", "Native${viewModelName}.kt")
    }else{
        log.info("Creating Fragment")
        createFragment(packageName, fragmentName, layoutName)
                .save(directorySrc, packageName, "${fragmentName}.kt")
    }

    log.info("Adding Koin Module")
    addKoinModule(directorySrc, packageName, viewModelName)

    log.info("Creating Fragment Layout")
    createFragmentLayout(packageName, fragmentName)
            .save(directoryRes, "layout", "${layoutName}.xml")
}

//https://plugins.jetbrains.com/docs/intellij/faq.html
//https://github.com/JetBrains/intellij-sdk-code-samples