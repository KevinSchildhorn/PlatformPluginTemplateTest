package com.github.kevinschildhorn.platformplugintemplatetest

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.android.tools.idea.wizard.template.impl.activities.common.addAllKotlinDependencies
import com.github.kevinschildhorn.platformplugintemplatetest.listeners.MyProjectManagerListener.Companion.projectInstance
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.idea.KotlinLanguage

fun RecipeExecutor.kmmSetup(
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

    createFragment(packageName, fragmentName, layoutName, projectData,viewModelInclude,viewModelName)
            .save(directorySrc, packageName, "${fragmentName}.kt")

    createFragmentLayout(packageName, fragmentName)
            .save(directoryRes, "layout", "${layoutName}.xml")

    if(viewModelInclude){
        createViewModel(packageName, viewModelName, entityName, projectData)
                .save(directorySrc, packageName, "${viewModelName}.kt")
    }

}

fun String.save(srcDir: PsiDirectory, subDirPath: String, fileName: String) {
    try {
        val destDir = subDirPath.split(".").toDir(srcDir)
        val psiFile = PsiFileFactory
                .getInstance(srcDir.project)
                .createFileFromText(fileName, KotlinLanguage.INSTANCE, this)
        destDir.add(psiFile)
    }catch (exc: Exception) {
        exc.printStackTrace()
    }
}

fun List<String>.toDir(srcDir: PsiDirectory): PsiDirectory {
    var result = srcDir
    forEach {
        result = result.findSubdirectory(it) ?: result.createSubdirectory(it)
    }
    return result
}