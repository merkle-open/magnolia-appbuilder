package com.namics.oss.magnolia.appbuilder.action.activation;

import com.namics.oss.magnolia.appbuilder.MgnlIcon;
import com.namics.oss.magnolia.appbuilder.action.AppActionDefinition;
import com.namics.oss.magnolia.appbuilder.action.AvailabilityDefinitionBuilder;
import com.namics.oss.magnolia.appbuilder.action.rule.JcrIsNotDeletedRuleDefinition;
import info.magnolia.ui.api.action.ConfiguredActionDefinition;
import info.magnolia.ui.availability.rule.JcrPublishedRuleDefinition;
import info.magnolia.ui.contentapp.action.JcrCommandActionDefinition;

public class DeactivateAppActionDefinition implements AppActionDefinition {

	@Override
	public ConfiguredActionDefinition action() {
		final JcrCommandActionDefinition definition = new JcrCommandActionDefinition();
		definition.setName("deactivate");
		definition.setLabel("actions.deactivate");
		definition.setIcon(MgnlIcon.UNPUBLISH);
		definition.setCommand("unpublish");
		definition.setAvailability(new AvailabilityDefinitionBuilder()
				.access("editor", "publisher")
				.writePermissionRequired(true)
				.rule(new JcrIsNotDeletedRuleDefinition())
				.rule(new JcrPublishedRuleDefinition())
				.build());
		return definition;
	}

	@Override
	public boolean multiple() {
		return true;
	}
}
