package com.github.kevinschildhorn.platformplugintemplatetest.model.generators

import com.android.tools.idea.wizard.template.ProjectTemplateData
import com.android.tools.idea.wizard.template.extractLetters

fun createListFragment(
        packageName: String,
        fragmentName: String,
        layoutName: String,
        projectData: ProjectTemplateData,
        usingViewModel: Boolean,
        viewModelName:String,
        entityName: String
):String {
    var fileString = """package $packageName

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import $packageName.R
import $packageName.adapter.${entityName.capitalize()}Listener
import $packageName.adapter.${entityName.capitalize()}ViewAdapter
import $packageName.viewmodel.$viewModelName
import kotlinx.android.synthetic.main.${layoutName.toLowerCase()}.*

class $fragmentName : Fragment(), ${entityName.capitalize()}Listener {

    private val viewModel: $viewModelName by viewModel()

    private lateinit var viewAdapter: ${entityName.capitalize()}ViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.${layoutName.toLowerCase()}, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.fetch${entityName.capitalize()}s()
    }

    private fun reloadRecyclerView() {
        updateList(viewModel.${entityName.toLowerCase()}LiveData.value)
    }

    private fun setupRecyclerView() {
        viewManager = LinearLayoutManager(context)
        viewAdapter = ${entityName}ViewAdapter(requireContext(), this)
        ${entityName.toLowerCase()}_recycler_view.layoutManager = viewManager
        ${entityName.toLowerCase()}_recycler_view.adapter = viewAdapter
    }

    private fun attachLiveDataObservers() {
        viewModel.${entityName.toLowerCase()}LiveData.observe(
            viewLifecycleOwner, this::handle${entityName.capitalize()}Result
        )
    }

    private fun handle${entityName.capitalize()}Result(results:List<Any>){
        updateList(results)
    }

    private fun updateList(list: List<Any>?) {
        list?.let {
            viewAdapter.submitList(list) {
                viewAdapter.notifyItemChanged(list.size)
            }
        }
    }

    override fun onItemSelected(index: Int, item: Any) {
    }
    
}
"""
    // View Model
    val viewModelImportString = if(usingViewModel) "import org.koin.android.viewmodel.ext.android.viewModel\nimport ${projectData.applicationPackage}.$viewModelName" else ""
    val viewModelString = if(usingViewModel) "private val viewModel: $viewModelName by viewModel()" else ""
    fileString = fileString.replace(viewModelImportPlaceholder,viewModelImportString)
    fileString = fileString.replace(viewModelPlaceholder,viewModelString)
    return  fileString
}

fun createListFragmentLayout(
        packageName: String,
        fragmentName: String,
        entityName: String) = """<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="${packageName}.$fragmentName">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/${entityName.toLowerCase()}_recycler_view"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        />
</androidx.constraintlayout.widget.ConstraintLayout>
"""

fun createViewAdapter(
        packageName: String,
        entityName: String
):String {
    var fileString = """package $packageName

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import $packageName.R

class ${entityName.capitalize()}ViewAdapter(val context: Context,val listener: ${entityName.capitalize()}Listener) :
    ListAdapter<Any, ${entityName.capitalize()}ViewAdapter.${entityName.capitalize()}ViewHolder>(
        ruleCallback
    ) {

    inner class ${entityName.capitalize()}ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ${entityName.capitalize()}ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.${entityName.toLowerCase()}_view_adapter, viewGroup, false)
        return ${entityName.capitalize()}ViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ${entityName.capitalize()}ViewHolder, position: Int) {
        val item = getItem(position)   // TODO
        holder.itemView.setOnClickListener {
            listener.onItemSelected(position,item)
        }
    }

    companion object {
        private val ruleCallback = object : DiffUtil.ItemCallback<Any>() {
            override fun areContentsTheSame(
                oldItem: Any,
                newItem: Any
            ): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(
                oldItem: Any,
                newItem: Any
            ): Boolean =
                oldItem == newItem
        }
    }
}

interface ${entityName.capitalize()}Listener {
    fun onItemSelected(index: Int, item: Any)
}
"""
    return  fileString
}

fun createViewAdapterLayout() =
        """<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</FrameLayout>
"""