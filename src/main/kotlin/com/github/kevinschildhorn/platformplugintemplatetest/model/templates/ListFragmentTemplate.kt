package com.github.kevinschildhorn.platformplugintemplatetest.model.templates

import com.android.tools.idea.wizard.template.*
import com.github.kevinschildhorn.platformplugintemplatetest.defaultPackageNameParameter
import com.github.kevinschildhorn.platformplugintemplatetest.model.recipes.kmmFragmentSetup
import com.github.kevinschildhorn.platformplugintemplatetest.model.recipes.kmmListFragmentSetup

val kmmListFragmentTemplate
    get() = template {
        revision = 2
        name = "KMM List Fragment Setup"
        description = "Creates a new Fragment with List along layout file and viewModel"
        minApi = 16
        minBuildApi = 16
        category = Category.Other // Check other categories
        formFactor = FormFactor.Mobile
        screens = listOf(WizardUiContext.FragmentGallery, WizardUiContext.MenuEntry,
                WizardUiContext.NewProject, WizardUiContext.NewModule)

        val packageNameParam = defaultPackageNameParameter
        val entityName = stringParameter {
            name = "Entity Name"
            default = "XYZ"
            help = "The name of the entity class to create and use in Activity"
            constraints = listOf(Constraint.NONEMPTY)
        }
        val fragmentName = stringParameter {
            name = "Fragment Name"
            default = "Fragment"
            help = "The name of the layout to create for the fragment"
            constraints = listOf(Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { "${entityName.value}ListFragment" }
        }
        val adapterName = stringParameter {
            name = "View Adapter Name"
            default = "ViewAdapter"
            help = "The name of the view adapter"
            constraints = listOf(Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { "${entityName.value}ViewAdapter" }
        }
        val adapterLayoutName = stringParameter {
            name = "View Adapter Layout Name"
            default = "my_view_adapter"
            help = "The name of the layout to create for tghe view adapter"
            constraints = listOf(Constraint.LAYOUT, Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { "${entityName.value.toLowerCase()}_view_adapter" }
        }
        val layoutName = stringParameter {
            name = "Layout Name"
            default = "my_fragment"
            help = "The name of the layout to create for the fragment"
            constraints = listOf(Constraint.LAYOUT, Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { "${entityName.value.toLowerCase()}_fragment" }
        }
        val includeViewModelBool = booleanParameter {
            name = "Include View Model"
            default = true
            help = "Whether to create a matching ViewModel"
        }
        val viewModelName = stringParameter {
            name = "ViewModel Name"
            default = "ViewModel"
            help = "The name of the ViewModel to create for the activity"
            constraints = listOf(Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { "${entityName.value}ViewModel" }
        }
        widgets(
                TextFieldWidget(entityName),
                TextFieldWidget(fragmentName),
                TextFieldWidget(layoutName),
                TextFieldWidget(adapterName),
                TextFieldWidget(adapterLayoutName),
                CheckBoxWidget(includeViewModelBool),
                TextFieldWidget(viewModelName),
                PackageNameWidget(packageNameParam)
        )

        recipe = { data: TemplateData ->
            kmmListFragmentSetup(
                    data as ModuleTemplateData,
                    packageNameParam.value,
                    entityName.value,
                    layoutName.value,
                    fragmentName.value,
                    adapterName.value,
                    adapterLayoutName.value,
                    includeViewModelBool.value,
                    viewModelName.value
            )
        }
    }