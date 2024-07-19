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
import org.apache.maven.shared.utils.io.FileUtils;
import org.eclipse.aether.RepositorySystemSession;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(name = "package", threadSafe = true, defaultPhase = LifecyclePhase.PACKAGE)
public class NpmPackageMojo extends DistributionDirectoryNpmExecutionMojo {

    public static final String MODULES_SUB_DIRECTORY = "node_modules";

    @Component
    private MavenProjectHelper projectHelper;

    protected String getArguments() {
        return "pack ";
    }

    @Override
    public void execute() throws MojoFailureException {

        try {
            File moduleDirectory = new File(workingDirectory, MODULES_SUB_DIRECTORY);

            if (moduleDirectory.isDirectory()) {

                final Path targetDirectory = Path.of(distributionDirectory.getAbsolutePath(), MODULES_SUB_DIRECTORY);

                try {

                    Files.createLink(targetDirectory, moduleDirectory.toPath());

                } catch (IOException e) {

                    final List<NpmDependency> bundledDependencies = allDependencies()
                            .filter(it -> it.getType() == DependencyType.BUNDLED).collect(Collectors.toList());

                    for (NpmDependency bundledDependency : bundledDependencies) {

                        FileUtils.copyDirectoryStructure(new File(moduleDirectory, bundledDependency.getName()), new File(targetDirectory.toFile(), bundledDependency.getName()));
                    }
                }
            }

            super.execute();

            Artifact projectArtifact = project.getArtifact();

            projectArtifact.setFile(getPackageFile());

            getLog().info("artifact file is " + project.getArtifact().getFile());

            projectHelper.attachArtifact(project, "package", new File(distributionDirectory, "package.json"));

            final File packageLock = new File(project.getBasedir(), "package-lock.json");

            if (packageLock.isFile()) {

                projectHelper.attachArtifact(project, "package-lock", packageLock);
            }

        } catch (IOException e) {

            throw new MojoFailureException(e.getMessage(), e);
        }
    }

}
