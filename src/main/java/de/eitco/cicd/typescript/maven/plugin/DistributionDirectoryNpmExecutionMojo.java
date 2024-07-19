/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 30.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.github.eirslett.maven.plugins.frontend.lib.FrontendException;
import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import com.github.eirslett.maven.plugins.frontend.mojo.RepositoryCacheResolver;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.aether.RepositorySystemSession;

public abstract class DistributionDirectoryNpmExecutionMojo extends AbstractNpmExecutionMojo {

    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
    private RepositorySystemSession repositorySystemSession;

    @Override
    protected void execute(FrontendPluginFactory factory) throws FrontendException {

        super.execute(new FrontendPluginFactory(distributionDirectory, installDirectory, new RepositoryCacheResolver(repositorySystemSession)));
    }
}
