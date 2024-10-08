package com.merkle.oss.magnolia.appbuilder.action.edit;

import info.magnolia.cms.security.Permission;
import info.magnolia.ui.contentapp.action.JcrCommandActionDefinition;
import info.magnolia.ui.contentapp.browser.drop.DropConstraintDefinition;

import com.merkle.oss.magnolia.appbuilder.action.AppActionDefinition;
import com.merkle.oss.magnolia.appbuilder.action.rule.JcrIsNotDeletedRuleDefinition;
import com.merkle.oss.magnolia.appbuilder.action.rule.PermissionRequiredRuleDefinition;
import com.merkle.oss.magnolia.definition.builder.availability.AvailabilityDefinitionBuilder;

public class DeleteAppActionDefinition implements AppActionDefinition {

	@Override
	public JcrCommandActionDefinition action(final DropConstraintDefinition dropConstraint) {
		final JcrCommandActionDefinition definition = new JcrCommandActionDefinition();
		definition.setName("delete");
		definition.setLabel("actions.delete");
		definition.setAsynchronous(true);
		definition.setCommand("delete");
		definition.setAvailability(new AvailabilityDefinitionBuilder()
				.rule(new JcrIsNotDeletedRuleDefinition())
				.rule(new PermissionRequiredRuleDefinition(Permission.REMOVE))
				.build()
		);
		return definition;
	}

	@Override
	public boolean multiple() {
		return true;
	}

	@Override
	public boolean isCallback() {
		return true;
	}
}
