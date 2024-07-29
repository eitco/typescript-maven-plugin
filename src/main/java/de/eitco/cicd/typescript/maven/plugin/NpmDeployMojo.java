/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 04.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;


import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * This goal deploys the npm package by calling {@code npm publish}.
 */
@Mojo(name = "deploy", threadSafe = true, defaultPhase = LifecyclePhase.INSTALL)
public class NpmDeployMojo extends DistributionDirectoryNpmExecutionMojo {

    @Override
    protected String getNpmRegistryURL() {
        return "";
    }

    protected String getArguments() {
        return "publish ";
    }
}
