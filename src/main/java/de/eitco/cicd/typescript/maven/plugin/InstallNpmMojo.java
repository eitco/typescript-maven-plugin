/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 03.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "npm-install", threadSafe = true, defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class InstallNpmMojo extends AbstractNpmExecutionMojo {

    @Parameter(defaultValue = "false", property = "typescript.install.force")
    private boolean force;

    @Parameter(defaultValue = "false", property = "typescript.install.legacyPeerDeps")
    private boolean legacyPeerDeps;

    @Override
    protected String getArguments() {
        String ret = "install ";
        if (force) {
            ret += "--force ";
        }
        if (legacyPeerDeps) {
            ret += "--legacy-peer-deps ";
        }
        return ret;
    }
}
