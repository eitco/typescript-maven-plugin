/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 04.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eirslett.maven.plugins.frontend.lib.FrontendException;
import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import com.github.eirslett.maven.plugins.frontend.mojo.RepositoryCacheResolver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.eclipse.aether.RepositorySystemSession;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This goal packages the npm package by calling {@code npm pack}. Additionally, this attaches
 * the package created, the `package.json` file and the `package-lock.json` file to the project so
 * that the maven default install and deploy plugins will install/deploy them.
 */
@Mojo(name = "package", threadSafe = true, defaultPhase = LifecyclePhase.PACKAGE)
public class NpmPackageMojo extends DistributionDirectoryNpmExecutionMojo {

    @Component
    private MavenProjectHelper projectHelper;

    protected String getArguments() {
        return "pack ";
    }

    @Override
    public void execute() throws MojoFailureException {

        try {
            if (!skipExecution()) {

                bundleDependencies();
            }

            super.execute();

            Artifact projectArtifact = project.getArtifact();

            projectArtifact.setFile(getPackageFile());

            getLog().info("artifact file is " + project.getArtifact().getFile());

            projectHelper.attachArtifact(project, "package", new File(distributionDirectory, "package.json"));

            final File packageLock = new File(project.getBasedir(), packageManager.lockFileName());

            if (packageLock.isFile()) {

                projectHelper.attachArtifact(project, packageManager.lockFileArtifactType(), packageLock);
            }

        } catch (IOException e) {

            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    /**
     * Copies the bundled dependencies - including their transitive dependencies - into the distribution directory
     * and adds them to the dependencies of the distributed {@code package.json}. Since npm 7 bundled dependencies
     * that are not listed as dependencies are not packed.
     */
    private void bundleDependencies() throws IOException {

        final List<String> bundledDependencies = allDependencies()
                .filter(it -> it.getType() == DependencyType.BUNDLED)
                .map(NpmDependency::getName)
                .collect(Collectors.toList());

        if (bundledDependencies.isEmpty()) {

            return;
        }

        Map<String, String> versions = BundledDependencies.copy(workingDirectory, bundledDependencies, distributionDirectory);

        final ObjectMapper mapper = NpmDependencyMetaDataReader.makeObjectMapper();
        final File packageJson = new File(distributionDirectory, PACKAGE_FILE_NAME);
        final Package deployPackage = mapper.readValue(packageJson, Package.class);

        Map<String, String> dependencies = new LinkedHashMap<>();

        if (deployPackage.getDependencies() != null) {

            dependencies.putAll(deployPackage.getDependencies());
        }

        dependencies.putAll(versions);

        mapper.writeValue(packageJson, deployPackage.setDependencies(dependencies));
    }

}
