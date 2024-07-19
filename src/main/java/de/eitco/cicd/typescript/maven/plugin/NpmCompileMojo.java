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

@Mojo(name = "compile", threadSafe = true, defaultPhase = LifecyclePhase.COMPILE)
public class NpmCompileMojo extends AbstractNpmExecutionMojo {
    @Override
    protected String getArguments() {
        return "run compile";
    }


}
