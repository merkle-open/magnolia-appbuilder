package com.merkle.oss.magnolia.builder;

import info.magnolia.config.registry.DefinitionProvider;
import info.magnolia.config.registry.DefinitionRawView;
import info.magnolia.config.registry.Registry;
import info.magnolia.config.registry.decoration.DefinitionDecorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

public abstract class AbstractDynamicDefinitionProvider<T> implements DefinitionProvider<T> {
    private final List<Problem> problems = new ArrayList<>();
    private final List<DefinitionDecorator<T>> decorators;

    protected AbstractDynamicDefinitionProvider(final List<DefinitionDecorator<T>> decorators) {
        this.decorators = decorators;
    }

    @Override
    public List<DefinitionDecorator<T>> getDecorators() {
        return decorators;
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
    public Collection<Problem> getProblems() {
        return problems;
    }

    @Override
    public final T get() throws Registry.InvalidDefinitionException {
        problems.clear();
        return getInternal();
    }

    protected abstract T getInternal() throws Registry.InvalidDefinitionException;

    protected void addProblem(final Exception e) {
        problems.add(
                Problem.severe()
                        .withRelatedException(e)
                        .withDetails(e.getClass().getName() + " " + ExceptionUtils.getRootCauseMessage(e))
                        .withLocation(getMetadata().getLocation())
                        .withTitle(getMetadata().getName())
                        .build()
        );
    }
}