package com.namics.oss.magnolia.appbuilder;

import info.magnolia.cms.security.operations.AccessDefinition;
import info.magnolia.config.registry.DefinitionMetadata;
import info.magnolia.config.registry.DefinitionProvider;
import info.magnolia.config.registry.DefinitionRawView;
import info.magnolia.config.registry.Registry;
import info.magnolia.config.registry.decoration.DefinitionDecorator;
import info.magnolia.icons.MagnoliaIcons;
import info.magnolia.ui.api.app.AppDescriptor;
import info.magnolia.ui.api.app.SubAppDescriptor;
import info.magnolia.ui.api.app.registry.DefinitionTypes;
import info.magnolia.ui.contentapp.ContentApp;
import info.magnolia.ui.contentapp.configuration.ContentAppDescriptor;
import info.magnolia.ui.datasource.DatasourceDefinition;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.namics.oss.magnolia.appbuilder.annotations.AppFactory;
import com.namics.oss.magnolia.appbuilder.annotations.AppPermissions;
import com.namics.oss.magnolia.appbuilder.annotations.CustomIcon;
import com.namics.oss.magnolia.appbuilder.annotations.Icon;
import com.namics.oss.magnolia.appbuilder.annotations.SubApp;

public class AppDescriptorProvider implements DefinitionProvider<AppDescriptor> {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final DefinitionMetadata metadata;
	private final Object factoryObject;
	private final AppFactory annotation;

	public AppDescriptorProvider(final Object factoryObject) {
		this.factoryObject = factoryObject;
		this.annotation = factoryObject.getClass().getAnnotation(AppFactory.class);
		this.metadata = new AppDefinitionMetaDataBuilder(factoryObject, annotation.id())
				.type(DefinitionTypes.APP)
				.build();
	}

	@Override
	public List<DefinitionDecorator<AppDescriptor>> getDecorators() {
		return Collections.emptyList();
	}

	@Override
	public DefinitionMetadata getMetadata() {
		return metadata;
	}

	@Override
	public DefinitionRawView getRaw() {
		return DefinitionRawView.EMPTY;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public long getLastModified() {
		return System.currentTimeMillis();
	}

	@Override
	public AppDescriptor get() throws Registry.InvalidDefinitionException {
		final ContentAppDescriptor<DatasourceDefinition> appDescriptor = new ContentAppDescriptor<>();
		appDescriptor.setAppClass(ContentApp.class);
		appDescriptor.setName(annotation.name());
		appDescriptor.setEnabled(annotation.isEnabled());
		getIcon().ifPresent(appDescriptor::setIcon);
		appDescriptor.setTheme(annotation.theme());
		appDescriptor.setLabel(annotation.label());
		appDescriptor.setI18nBasename(annotation.i18nBasename());
		appDescriptor.setSubApps(getSubApps());
		getPermissions().ifPresent(appDescriptor::setPermissions);
		return appDescriptor;
	}

	private Optional<String> getIcon() {
		if (factoryObject.getClass().isAnnotationPresent(Icon.class) && factoryObject.getClass().isAnnotationPresent(CustomIcon.class)) {
			throw new IllegalArgumentException("Either Icon or CustomIcon must be specified - not both!");
		}
		return Optional
				.ofNullable(factoryObject.getClass().getAnnotation(Icon.class)).map(Icon::value).map(MagnoliaIcons::getCssClass)
				.or(() ->
						Optional.ofNullable(factoryObject.getClass().getAnnotation(CustomIcon.class)).map(CustomIcon::value)
				)
				.or(() -> Optional.ofNullable(annotation.icon()).filter(Predicate.not(String::isBlank)));
	}

	private Map<String, SubAppDescriptor> getSubApps() {
		return getDefinitions(SubApp.class, SubAppDescriptor.class).collect(Collectors.toMap(
				SubAppDescriptor::getName,
				Function.identity()
		));
	}

	private Optional<AccessDefinition> getPermissions() {
		return getDefinitions(AppPermissions.class, AccessDefinition.class).findFirst();
	}

	private <T> Stream<T> getDefinitions(
			final Class<? extends Annotation> annotation,
			final Class<T> definitionType
	) {

		return Arrays.stream(factoryObject.getClass().getDeclaredMethods())
				.filter(method -> method.isAnnotationPresent(annotation))
				.map(method -> {
					try {
						//noinspection unchecked
						return (T) method.invoke(factoryObject);
					} catch (IllegalAccessException | InvocationTargetException e) {
						LOG.error("Could not build " + definitionType.getSimpleName() + ", for app " + this.annotation.name(), e);
						throw new Registry.InvalidDefinitionException(this.annotation.id());
					}
				});
	}

	protected static class AppDefinitionMetaDataBuilder extends ExplicitIdDefinitionMetaDataBuilder {
		public AppDefinitionMetaDataBuilder(final Object appFactory, final String id) {
			super(appFactory, id);
		}

		@Override
		protected String buildReferenceId() {
			return getName();
		}
	}
}
