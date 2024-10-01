package com.merkle.oss.magnolia.templatebuilder.parameter;

import info.magnolia.cms.security.MgnlUser;
import info.magnolia.cms.security.User;
import info.magnolia.context.Context;
import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.rendering.template.TemplateDefinition;

import javax.jcr.Node;

import com.merkle.oss.magnolia.builder.parameter.ParameterResolver;

/**
 * Resolves the following types:
 * <ul>
 * <li>{@link Node}
 * <li>{@link TemplateDefinition}
 * <li>{@link WebContext}
 * <li>{@link Context}
 * <li>{@link User}
 * <li>{@link MgnlUser}
 * </ul>
 */
public class DefaultAutoGeneratorParameterResolver extends ParameterResolver {
    private final Node node;
    private final TemplateDefinition templateDefinition;

    public DefaultAutoGeneratorParameterResolver(
            final Node node,
            final TemplateDefinition templateDefinition
    ) {
        super(null);
        this.node = node;
        this.templateDefinition = templateDefinition;
    }

    @Override
    public Object resolveParameter(final Class<?> parameterType) {
        if (parameterType.equals(Node.class)) {
            return node;
        }
        if (parameterType.equals(TemplateDefinition.class)) {
            return templateDefinition;
        }
        if (parameterType.isAssignableFrom(WebContext.class)) {
            return MgnlContext.getWebContext();
        }
        if (parameterType.isAssignableFrom(Context.class)) {
            return MgnlContext.getInstance();
        }
        if (parameterType.isAssignableFrom(User.class)) {
            return MgnlContext.getUser();
        }
        if (parameterType.isAssignableFrom(MgnlUser.class)) {
            return MgnlContext.getUser();
        }
        return super.resolveParameter(parameterType);
    }

    public static class Factory implements AvailabilityParameterResolverFactory {
        @Override
        public ParameterResolver create(final Node node, final TemplateDefinition templateDefinition) {
            return new DefaultAutoGeneratorParameterResolver(node, templateDefinition);
        }
    }
}
