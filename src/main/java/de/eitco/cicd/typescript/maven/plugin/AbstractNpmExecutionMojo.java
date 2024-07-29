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
import java.util.List;

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
                    argumentsToUse, environmentVariables);
        } else {
            getLog().info("Skipping npm install as package.json unchanged");
        }
    }

    protected String getNpmRegistryURL() {
        return npmRegistryURL;
    }
}
