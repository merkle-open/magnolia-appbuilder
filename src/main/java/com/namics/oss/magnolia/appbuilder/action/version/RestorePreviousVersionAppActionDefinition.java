package com.namics.oss.magnolia.appbuilder.action.version;

import com.namics.oss.magnolia.appbuilder.action.AppActionDefinition;
import com.namics.oss.magnolia.appbuilder.action.AvailabilityDefinitionBuilder;
import com.namics.oss.magnolia.appbuilder.action.rule.PermissionRequiredRuleDefinition;
import info.magnolia.cms.security.Permission;
import info.magnolia.ui.api.action.ConfiguredActionDefinition;
import info.magnolia.ui.availability.rule.JcrIsDeletedRuleDefinition;
import info.magnolia.ui.contentapp.action.RestoreJcrVersionActionDefinition;

public class RestorePreviousVersionAppActionDefinition implements AppActionDefinition {
	@Override
	public ConfiguredActionDefinition action() {
		final RestoreJcrVersionActionDefinition definition = new RestoreJcrVersionActionDefinition();
		definition.setName("restorePreviousVersion");
		definition.setLabel("actions.restorePreviousVersion");
		definition.setIcon("icon-undo");
		definition.setAvailability(new AvailabilityDefinitionBuilder()
				.writePermissionRequired(true)
				.rule(new JcrIsDeletedRuleDefinition())
				.rule(new PermissionRequiredRuleDefinition(Permission.WRITE))
				.build()
		);
		return definition;
	}
}
