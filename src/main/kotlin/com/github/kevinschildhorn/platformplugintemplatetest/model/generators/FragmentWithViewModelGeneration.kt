package com.github.kevinschildhorn.platformplugintemplatetest.model.generators

import com.android.tools.idea.wizard.template.ProjectTemplateData

const val viewModelImportPlaceholder = "//ViewModelImport"
const val viewModelPlaceholder = "//ViewModelVal"

fun createFragmentWithViewModel(
        packageName: String,
        fragmentName: String,
        layoutName: String,
        projectData: ProjectTemplateData,
        viewModelName:String,
        entityName:String
):String {
    val fileString = """package $packageName

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.android.viewmodel.ext.android.viewModel
import ${projectData.applicationPackage}.android.viewmodel.$viewModelName

class $fragmentName : Fragment() {

    private val viewModel: $viewModelName by viewModel()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.${layoutName.toLowerCase()}, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachLiveDataObservers()
        viewModel.fetch${entityName.capitalize()}()
    }
    
    private fun attachLiveDataObservers() {
        viewModel.${entityName.toLowerCase()}LiveData.observe(
            viewLifecycleOwner, this::handle${entityName.capitalize()}Result
        )
    }
    
    private fun handle${entityName.capitalize()}Result(results:Any){
        // TODO
    }
}
"""
    return  fileString
}