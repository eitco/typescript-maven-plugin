/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 04.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.github.eirslett.maven.plugins.frontend.lib.FrontendException;
import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import com.github.eirslett.maven.plugins.frontend.lib.ProxyConfig;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.sonatype.plexus.build.incremental.BuildContext;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractNpmExecutionMojo extends AbstractTypescriptMojo {

    @Component
    private BuildContext buildContext;

    /**
     * This parameter specifies whether the command line option {@code verbose} should be given to any npm execution.
     */
    @Parameter(defaultValue = "false", property = "typescript.verbose")
    private boolean verbose;

    public AbstractNpmExecutionMojo() {
    }

    protected abstract String getArguments();

    @Override
    protected void execute(FrontendPluginFactory factory) throws FrontendException {

        File packageJson = new File(workingDirectory, "package.json");
        if (buildContext == null || buildContext.hasDelta(packageJson) || !buildContext.isIncremental()) {
            String argumentsToUse = getArguments();
            if (verbose) {
                argumentsToUse+= " --verbose ";
            }
            factory.getNpmRunner(new ProxyConfig(List.of()), getNpmRegistryURL()).execute(
                    argumentsToUse, makeEnvironment());
        } else {
            getLog().info("Skipping npm install as package.json unchanged");
        }
    }

    private Map<String, String> makeEnvironment() {

        Map<String, String> result = new HashMap<>();

        if (environmentVariables != null) {

            result.putAll(environmentVariables);
        }

        result.putAll(getAdditionalEnvironment());

        return result;
    }

    /**
     * Specifies environment variables to be added to the npm execution, in addition to the parameter
     * {@code environmentVariables}. These variables will not be logged.
     */
    protected Map<String, String> getAdditionalEnvironment() {
        return Map.of();
    }

    protected String getNpmRegistryURL() {
        return npmRegistryURL;
    }
}
