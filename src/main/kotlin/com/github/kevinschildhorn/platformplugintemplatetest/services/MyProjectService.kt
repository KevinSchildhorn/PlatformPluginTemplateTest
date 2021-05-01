package com.github.kevinschildhorn.platformplugintemplatetest.services

import com.github.kevinschildhorn.platformplugintemplatetest.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
