/*
 * Copyright (c) 2026 EITCO GmbH
 * All rights reserved.
 *
 * Created on 27.10.2026
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.Parameter;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.List;

/**
 * Builds the configuration of a mojo executed on behalf of another mojo, the same way maven does it for mojos bound
 * to the lifecycle.
 */
public final class DelegatedMojoConfiguration {

    private DelegatedMojoConfiguration() {
    }

    /**
     * Merges the given plugin configuration (as given in a pom) over the default configuration of the mojo.
     * Configuration elements that are no parameters of the mojo are dropped.
     */
    public static Xpp3Dom build(Xpp3Dom pluginConfiguration, MojoDescriptor mojoDescriptor) {

        Xpp3Dom defaults = toXpp3Dom(mojoDescriptor.getMojoConfiguration());
        Xpp3Dom result = new Xpp3Dom("configuration");

        List<Parameter> parameters = mojoDescriptor.getParameters();

        if (parameters == null) {

            return result;
        }

        for (Parameter parameter : parameters) {

            Xpp3Dom userValue = findUserValue(pluginConfiguration, parameter);
            Xpp3Dom defaultValue = defaults == null ? null : defaults.getChild(parameter.getName());

            if (userValue != null) {

                result.addChild(Xpp3Dom.mergeXpp3Dom(new Xpp3Dom(userValue, parameter.getName()), defaultValue));

            } else if (defaultValue != null) {

                result.addChild(new Xpp3Dom(defaultValue));
            }
        }

        return result;
    }

    private static Xpp3Dom findUserValue(Xpp3Dom pluginConfiguration, Parameter parameter) {

        if (pluginConfiguration == null) {

            return null;
        }

        Xpp3Dom result = pluginConfiguration.getChild(parameter.getName());

        if (result == null && parameter.getAlias() != null) {

            result = pluginConfiguration.getChild(parameter.getAlias());
        }

        return result;
    }

    private static Xpp3Dom toXpp3Dom(PlexusConfiguration configuration) {

        if (configuration == null) {

            return null;
        }

        Xpp3Dom result = new Xpp3Dom(configuration.getName());

        result.setValue(configuration.getValue(null));

        for (String attribute : configuration.getAttributeNames()) {

            result.setAttribute(attribute, configuration.getAttribute(attribute, null));
        }

        for (PlexusConfiguration child : configuration.getChildren()) {

            result.addChild(toXpp3Dom(child));
        }

        return result;
    }
}
