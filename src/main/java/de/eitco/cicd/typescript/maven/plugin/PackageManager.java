/*
 * Copyright (c) 2026 EITCO GmbH
 * All rights reserved.
 *
 * Created on 27.10.2026
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import com.github.eirslett.maven.plugins.frontend.lib.ProxyConfig;
import com.github.eirslett.maven.plugins.frontend.lib.TaskRunnerException;

import java.util.Map;

/**
 * The package manager used to install dependencies, run scripts, pack and publish the package.
 */
public enum PackageManager {

    NPM("install-node-and-npm", "package-lock.json", "package-lock") {
        @Override
        public void execute(FrontendPluginFactory factory, ProxyConfig proxyConfig, String registryUrl, String arguments, Map<String, String> environment) throws TaskRunnerException {
            factory.getNpmRunner(proxyConfig, registryUrl).execute(arguments, environment);
        }
    },

    PNPM("install-node-and-pnpm", "pnpm-lock.yaml", "pnpm-lock") {
        @Override
        public void execute(FrontendPluginFactory factory, ProxyConfig proxyConfig, String registryUrl, String arguments, Map<String, String> environment) throws TaskRunnerException {
            factory.getPnpmRunner(proxyConfig, registryUrl).execute(arguments, environment);
        }

        @Override
        public String installArguments(boolean force, boolean legacyPeerDeps) {
            // pnpm has no equivalent to --legacy-peer-deps
            return super.installArguments(force, false);
        }

        @Override
        public String publishArguments() {
            // pnpm refuses to publish from a dirty working tree or a branch other than main/master
            return "publish --no-git-checks ";
        }
    };

    private final String installGoal;
    private final String lockFileName;
    private final String lockFileArtifactType;

    PackageManager(String installGoal, String lockFileName, String lockFileArtifactType) {
        this.installGoal = installGoal;
        this.lockFileName = lockFileName;
        this.lockFileArtifactType = lockFileArtifactType;
    }

    public abstract void execute(
            FrontendPluginFactory factory,
            ProxyConfig proxyConfig,
            String registryUrl,
            String arguments,
            Map<String, String> environment
    ) throws TaskRunnerException;

    /**
     * @return the goal of the frontend-maven-plugin that installs node and this package manager
     */
    public String installGoal() {
        return installGoal;
    }

    public String lockFileName() {
        return lockFileName;
    }

    public String lockFileArtifactType() {
        return lockFileArtifactType;
    }

    public String installArguments(boolean force, boolean legacyPeerDeps) {
        String ret = "install ";
        if (force) {
            ret += "--force ";
        }
        if (legacyPeerDeps) {
            ret += "--legacy-peer-deps ";
        }
        return ret;
    }

    public String publishArguments() {
        return "publish ";
    }
}
