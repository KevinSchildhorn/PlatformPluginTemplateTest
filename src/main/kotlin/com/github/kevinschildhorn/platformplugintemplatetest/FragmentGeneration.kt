package com.github.kevinschildhorn.platformplugintemplatetest

import com.android.tools.idea.wizard.template.ProjectTemplateData
import com.android.tools.idea.wizard.template.extractLetters

const val viewModelImportPlaceholder = "//ViewModelImport"
const val viewModelPlaceholder = "//ViewModelVal"

fun createFragment(
        packageName: String,
        fragmentName: String,
        layoutName: String,
        projectData: ProjectTemplateData,
        usingViewModel: Boolean,
        viewModelName:String
):String {
    var fileString = """
package $packageName

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
$viewModelImportPlaceholder

import ${projectData.applicationPackage}.R;

class $fragmentName : Fragment() {

    $viewModelPlaceholder
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.${extractLetters(layoutName.toLowerCase())}, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
"""
    // View Model
    val viewModelImportString = if(usingViewModel) "import org.koin.android.viewmodel.ext.android.viewModel\nimport org.commonsproject.commonpass.android.$viewModelName" else ""
    val viewModelString = if(usingViewModel) "private val viewModel: $viewModelName by viewModel()" else ""
    fileString = fileString.replace(viewModelImportPlaceholder,viewModelImportString)
    fileString = fileString.replace(viewModelPlaceholder,viewModelString)
    return  fileString
}

fun createFragmentLayout(
        packageName: String,
        fragmentName: String) = """
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="${packageName}.$fragmentName">

</androidx.constraintlayout.widget.ConstraintLayout>
"""