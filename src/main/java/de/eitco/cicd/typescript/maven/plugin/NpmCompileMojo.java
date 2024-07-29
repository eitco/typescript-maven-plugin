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
 * This goal compiles the typescript sources. It does so by calling the {@code compile} script, which in turn
 * simply calls {@code tsc}.
 */
@Mojo(name = "compile", threadSafe = true, defaultPhase = LifecyclePhase.COMPILE)
public class NpmCompileMojo extends AbstractNpmExecutionMojo {
    @Override
    protected String getArguments() {
        return "run compile";
    }


}
