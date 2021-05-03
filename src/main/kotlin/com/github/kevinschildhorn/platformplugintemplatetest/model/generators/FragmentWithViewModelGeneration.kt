package com.github.kevinschildhorn.platformplugintemplatetest.model.generators

import com.android.tools.idea.wizard.template.ProjectTemplateData
import com.android.tools.idea.wizard.template.extractLetters

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
    val fileString = """
package $packageName

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import org.koin.android.viewmodel.ext.android.viewModel
import ${projectData.applicationPackage}.$viewModelName

import ${projectData.applicationPackage}.R;

class $fragmentName : Fragment() {

    private val viewModel: $viewModelName by viewModel()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.${extractLetters(layoutName.toLowerCase())}, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachLiveDataObservers()
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