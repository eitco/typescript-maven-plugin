/*
 * Copyright (c) 2026 EITCO GmbH
 * All rights reserved.
 *
 * Created on 27.10.2026
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.List;

/**
 * This goal installs node and the package manager given by {@code packageManager}. It does so by executing the goal
 * {@code install-node-and-npm} or {@code install-node-and-pnpm} of the frontend-maven-plugin, which is configured
 * with the configuration of the frontend-maven-plugin given in the pom - e.g. {@code nodeVersion},
 * {@code npmVersion} or {@code pnpmVersion}.
 */
@Mojo(name = "install-node-and-package-manager", threadSafe = true, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class InstallNodeAndPackageManagerMojo extends AbstractTypescriptMojo {

    public static final String FRONTEND_GROUP_ID = "com.github.eirslett";
    public static final String FRONTEND_ARTIFACT_ID = "frontend-maven-plugin";

    @Component
    private BuildPluginManager buildPluginManager;

    @Parameter(defaultValue = "${plugin}", readonly = true)
    private PluginDescriptor pluginDescriptor;

    @Parameter(defaultValue = "${mojoExecution}", readonly = true)
    private MojoExecution mojoExecution;

    @Override
    protected void execute(FrontendPluginFactory factory) {
        // will never be called since Mojo#execute() is overridden
    }

    @Override
    public void execute() throws MojoFailureException {

        if (skipExecution()) {

            getLog().info("skipping execution");

            return;
        }

        Plugin frontendPlugin = findFrontendPlugin();
        String goal = packageManager.installGoal();

        getLog().info("installing node and " + packageManager.name().toLowerCase() + " with "
                + FRONTEND_ARTIFACT_ID + ":" + frontendPlugin.getVersion() + ":" + goal);

        try {

            MojoDescriptor mojoDescriptor = buildPluginManager.getMojoDescriptor(
                    frontendPlugin, goal, project.getRemotePluginRepositories(), repoSession
            );

            MojoExecution execution = new MojoExecution(mojoDescriptor, mojoExecution.getExecutionId(), MojoExecution.Source.LIFECYCLE);

            execution.setConfiguration(DelegatedMojoConfiguration.build(
                    (Xpp3Dom) frontendPlugin.getConfiguration(), mojoDescriptor
            ));

            buildPluginManager.executeMojo(session, execution);

        } catch (MojoFailureException e) {

            throw e;

        } catch (Exception e) {

            throw new MojoFailureException("failed to execute " + FRONTEND_ARTIFACT_ID + ":" + goal + ": " + e.getMessage(), e);
        }
    }

    /**
     * Finds the frontend-maven-plugin as given in the pom - either in the build plugins or in the plugin management.
     * If it has no version, the version this plugin depends on is used.
     */
    private Plugin findFrontendPlugin() {

        String key = Plugin.constructKey(FRONTEND_GROUP_ID, FRONTEND_ARTIFACT_ID);

        Plugin configured = project.getBuild().getPluginsAsMap().get(key);

        if (configured == null && project.getPluginManagement() != null) {

            configured = project.getPluginManagement().getPluginsAsMap().get(key);
        }

        Plugin result;

        if (configured != null) {

            result = configured.clone();

        } else {

            result = new Plugin();
            result.setGroupId(FRONTEND_GROUP_ID);
            result.setArtifactId(FRONTEND_ARTIFACT_ID);
        }

        if (result.getVersion() == null) {

            result.setVersion(getDependencyVersion());
        }

        return result;
    }

    private String getDependencyVersion() {

        List<Artifact> artifacts = pluginDescriptor.getArtifacts();

        return artifacts.stream()
                .filter(it -> FRONTEND_GROUP_ID.equals(it.getGroupId()) && FRONTEND_ARTIFACT_ID.equals(it.getArtifactId()))
                .map(Artifact::getVersion)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(FRONTEND_ARTIFACT_ID + " is no dependency of " + pluginDescriptor.getId()));
    }
}
