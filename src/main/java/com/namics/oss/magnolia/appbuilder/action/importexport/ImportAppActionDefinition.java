package com.namics.oss.magnolia.appbuilder.action.importexport;

import info.magnolia.cms.security.Permission;
import info.magnolia.icons.MagnoliaIcons;
import info.magnolia.ui.contentapp.browser.drop.DropConstraintDefinition;
import info.magnolia.ui.dialog.actions.OpenDialogActionDefinition;

import com.merkle.oss.magnolia.definition.builder.availability.AvailabilityDefinitionBuilder;
import com.namics.oss.magnolia.appbuilder.action.AppActionDefinition;
import com.namics.oss.magnolia.appbuilder.action.rule.JcrIsNotDeletedRuleDefinition;
import com.namics.oss.magnolia.appbuilder.action.rule.PermissionRequiredRuleDefinition;

public class ImportAppActionDefinition implements AppActionDefinition {

	@Override
	public OpenDialogActionDefinition action(final DropConstraintDefinition dropConstraint) {
		final OpenDialogActionDefinition definition = new OpenDialogActionDefinition();
		definition.setName("import");
		definition.setDialogId("ui-framework-jcr:import");
		definition.setIcon(MagnoliaIcons.IMPORT.getCssClass());
		definition.setLabel("actions.import");
		definition.setAvailability(new AvailabilityDefinitionBuilder()
				.rule(new JcrIsNotDeletedRuleDefinition())
				.rule(new PermissionRequiredRuleDefinition(Permission.WRITE))
				.build()
		);
		return definition;
	}
}
