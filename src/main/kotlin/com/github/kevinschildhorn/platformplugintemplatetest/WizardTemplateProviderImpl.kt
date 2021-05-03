package com.github.kevinschildhorn.platformplugintemplatetest

import com.android.tools.idea.wizard.template.Template
import com.android.tools.idea.wizard.template.WizardTemplateProvider
import com.github.kevinschildhorn.platformplugintemplatetest.model.templates.kmmFragmentTemplate
import com.github.kevinschildhorn.platformplugintemplatetest.model.templates.kmmListFragmentTemplate

class WizardTemplateProviderImpl  : WizardTemplateProvider() {

    override fun getTemplates(): List<Template> = listOf(kmmFragmentTemplate, kmmListFragmentTemplate)
}