package com.github.kevinschildhorn.platformplugintemplatetest

import com.android.tools.idea.wizard.template.Constraint
import com.android.tools.idea.wizard.template.stringParameter
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.idea.KotlinLanguage


fun String.save(srcDir: PsiDirectory, subDirPath: String, fileName: String) {
    try {
        val destDir = subDirPath.split(".").toDir(srcDir)
        val psiFile = PsiFileFactory
                .getInstance(srcDir.project)
                .createFileFromText(fileName, KotlinLanguage.INSTANCE, this)
        destDir.add(psiFile)
        val log = Logger.getInstance("debug")
        log.info("Saving files file: ${srcDir.toString()} $subDirPath $fileName")
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

val defaultPackageNameParameter get() = stringParameter {
    name = "Package name"
    visible = { !isNewModule }
    default = "com.mycompany.myapp"
    constraints = listOf(Constraint.PACKAGE)
    suggest = { packageName }
}


fun addKoinModule(srcDir: PsiDirectory,
                  subDirPath: String,
                  viewModelName:String){
    val log = Logger.getInstance("debug")
    try {
        val destDir = subDirPath.split(".").toDir(srcDir)
        log.info("Searching Files ${destDir.files.size}")
        destDir.files.forEach {
            log.info("Found files: $srcDir $subDirPath ${it.name}")
            if(it.text.contains("Application()") && !it.text.contains(viewModelName)){
                log.info("Found Application File")
                var newFileString = it.text
                log.info("With Text:${it.text}")
                newFileString = newFileString.replace("import org.koin.dsl.module",
                    """import org.koin.dsl.module
                    import $subDirPath.viewmodel.$viewModelName
                    """.trimMargin())
                newFileString = newFileString.replace("module {",
                    """module {
                    viewModel {
                        $viewModelName(
                        )
                    }
                    """.trimMargin())

                CommandProcessor.getInstance().executeCommand(srcDir.project, {
                    log.info("saving Application File: ${it.name}")
                    val documentManager = PsiDocumentManager.getInstance(srcDir.project)
                    val document: Document? = documentManager.getDocument(it)
                    document?.let { doc ->
                        log.info("Found Document, setting text now")
                        doc.setText(newFileString)
                    }
                },"",null)
            }
            return@forEach
        }
    }catch (exc: Exception) {
        log.info("Error occurred")
        exc.printStackTrace()
    }
}