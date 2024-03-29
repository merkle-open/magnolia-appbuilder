package com.namics.oss.magnolia.appbuilder.contextmenu;

import com.namics.oss.magnolia.appbuilder.action.AppActionDefinition;
import com.namics.oss.magnolia.appbuilder.action.AppActionGroupDefinition;
import com.namics.oss.magnolia.appbuilder.action.DoubleClickAction;
import info.magnolia.ui.actionbar.definition.ActionbarSectionDefinition;
import info.magnolia.ui.actionbar.definition.ConfiguredActionbarSectionDefinition;
import info.magnolia.ui.api.availability.ConfiguredAvailabilityDefinition;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContentAppContextMenuDefinition extends AbstractAppContextMenuDefinition implements AppContextMenuDefinition {
	private final String nodeType;
	@Nullable
	private final AppActionDefinition doubleClickAction;

	public ContentAppContextMenuDefinition(
			final String nodeType,
			@Nullable final AppActionDefinition doubleClickAction,
			final List<AppActionGroupDefinition> actionGroups) {
		super(
				() -> nodeType.replaceFirst("mgnl:", ""),
				() -> {
					final ConfiguredAvailabilityDefinition definition = new ConfiguredAvailabilityDefinition();
					definition.setRoot(false);
					definition.setNodes(true);
					definition.addNodeType(nodeType);
					return definition;
				},
				actionGroups
		);
		this.nodeType = nodeType;
		this.doubleClickAction = doubleClickAction;
	}

	@Override
	public Stream<ActionbarSectionDefinition> sections() {
		final ConfiguredActionbarSectionDefinition singleSectionDefinition = new ConfiguredActionbarSectionDefinition();
		singleSectionDefinition.setName(uniqueNameProvider.get());
		singleSectionDefinition.setGroups(actionbarGroupDefinitions(false).collect(Collectors.toList()));
		singleSectionDefinition.setAvailability(availabilityDefinitionProvider.get());

		final ConfiguredActionbarSectionDefinition multipleSectionDefinition = new ConfiguredActionbarSectionDefinition();
		multipleSectionDefinition.setName(uniqueNameProvider.get());
		multipleSectionDefinition.setGroups(actionbarGroupDefinitions(false).collect(Collectors.toList()));
		multipleSectionDefinition.setAvailability(availabilityDefinitionProvider.get());

		return Stream.of(singleSectionDefinition, multipleSectionDefinition);
	}

	public Optional<DoubleClickAction> doubleClickAction() {
		return Optional.ofNullable(doubleClickAction).map(action ->
				new DoubleClickAction() {
					@Override
					public String nodeType() {
						return nodeType;
					}

					@Override
					public String action() {
						return getUniqueName(action.action());
					}
				}
		);
	}
}
