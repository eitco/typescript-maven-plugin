/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 04.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.github.eirslett.maven.plugins.frontend.mojo.AbstractFrontendMojo;
import com.google.common.base.Strings;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DependencyResolutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResolver;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResolverException;
import org.eclipse.aether.RepositorySystemSession;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class AbstractTypescriptMojo extends AbstractFrontendMojo {

    public static final String PACKAGE_FILE_NAME = "package.json";
    public static final Pattern DISALLOWED_VERSION_CHARACTERS = Pattern.compile("[^A-Za-z0-9-.+]");
    /**
     * This parameter holds the name of a package json file that will be merged with the package configuration given by
     * the maven parameters
     */
    @Parameter(property = "typescript.default.package.json")
    protected File defaultValues = null;

    @Parameter(defaultValue = "false", property = "ts.skip")
    private boolean skip;

    @Parameter(
        defaultValue = "${project.build.directory}/dist",
        required = false
    )
    protected File distributionDirectory;

    @Parameter(
        defaultValue = "${project.artifactId}",
        property = "typescript.project.name"
    )
    protected String projectName;

    @Parameter(
        defaultValue = "${project.version}",
        property = "typescript.project.version"
    )
    protected String projectVersion;

    @Parameter(
        property = "npmRegistryURL",
        required = false,
        defaultValue = ""
    )
    protected String npmRegistryURL;

    @Parameter(
        property = "typescript.publish.registry.url",
        required = false,
        defaultValue = ""
    )
    protected String publishRegistryURL;

    @Parameter(
        defaultValue = "import"
    )
    protected String typescriptEntryPoint;

    @Parameter
    private List<NpmDependency> dependencies = new ArrayList<>();

    @Parameter
    private List<NpmDependency> defaultDependencies = new ArrayList<>();

    private Map<String, NpmDependency> allDependencies;

    @Parameter
    protected CompilerOptions compileOptions = new CompilerOptions();

    @Parameter
    protected CompilerOptions testCompileOptions = new CompilerOptions();

    @Parameter
    protected AngularCompilerOptions angularCompilerOptions = new AngularCompilerOptions();

    @Parameter
    protected List<String> sources = List.of("src/main/ts/**.ts", "target/generated-sources/main/ts/**.ts");

    @Parameter
    protected  List<String> sourceExcludes = List.of();

    @Parameter
    protected List<String> testSources = List.of("src/test/ts/**.ts", "target/generated-sources/test/ts/**.ts");

    @Parameter
    protected List<String> testSourceExcludes = List.of();

    @Parameter(property = "typescript.version", defaultValue = "4.6.2")
    protected String typeScriptVersion;

    @Parameter
    protected List<String> keywords = new ArrayList<>();

    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true)
    protected MavenSession session;

    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
    protected RepositorySystemSession repoSession;

    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true, required = true)
    protected List<ArtifactRepository> remoteRepositories;

    @Component
    protected ProjectDependenciesResolver projectDependenciesResolver;

    @Component
    protected ArtifactResolver artifactResolver;
    /**
     * a suffix appended to the projects' version if and only if this version ends with "-SNAPSHOT"
     */
    @Parameter(property = "typescript.snapshot.version.suffix")
    private String snapshotVersionSuffix;
    @Parameter
    private Map<String, String> additionalScripts = new LinkedHashMap<>();

    @Parameter
    private String schematics = null;

    @Override
    protected boolean skipExecution() {
        return skip;
    }

    @NotNull
    protected File getPackageFile() {
        return new File(
                distributionDirectory,
                projectName.replaceAll("/", "-")
                        .replaceAll("@", "") + "-" +
                        makeProjectVersion() +
                        ".tgz"
        );
    }

    protected Stream<NpmDependency> allDependencies() {

        if (allDependencies != null) {

            return allDependencies.values().stream();
        }

        Map<String, NpmDependency> result = new LinkedHashMap<>();

        defaultDependencies.forEach(it -> result.put(it.getName(), it));
        dependencies.forEach(it -> {

            // assures that all dependencies are listed after the default dependencies
            result.remove(it.getName());

            result.put(it.getName(), it);
        });

        allDependencies = result;

        return allDependencies.values().stream();
    }

    protected String makeProjectVersion() {
        String projectVersion = this.projectVersion;

        if (projectVersion.endsWith("-SNAPSHOT") && snapshotVersionSuffix != null) {

            projectVersion = projectVersion + snapshotVersionSuffix;
        }

        Matcher matcher = DISALLOWED_VERSION_CHARACTERS.matcher(projectVersion);
        projectVersion = matcher.replaceAll("-");
        return projectVersion;
    }

    @NotNull
    protected NpmProjectBuilder makeProjectBuilder() throws DependencyResolutionException, ArtifactResolverException, IOException {

        String projectVersion = makeProjectVersion();

        NpmProjectBuilder projectBuilder = NpmProjectBuilder.identifiedBy(projectName, projectVersion);

        projectBuilder.fromProject(project);

        String targetRepo = Strings.isNullOrEmpty(publishRegistryURL) ? npmRegistryURL : publishRegistryURL;

        if (!Strings.isNullOrEmpty(targetRepo)) {

            projectBuilder.publishedTo(targetRepo);
        }

        projectBuilder.withKeyWords(keywords);
        projectBuilder.withEntryPoint(typescriptEntryPoint);
        projectBuilder.withSchematics(schematics);

        additionalScripts.forEach(projectBuilder::addScript);

        final NpmDependency typeScript = new NpmDependency();
        typeScript.setName("typescript");
        typeScript.setVersion(typeScriptVersion);
        typeScript.setType(DependencyType.DEV);

        projectBuilder.addDependency(typeScript);

        allDependencies().forEach(projectBuilder::addDependency);

        final NpmDependencyMetaDataReader metaDataReader = new NpmDependencyMetaDataReader(
                session,
                remoteRepositories,
                artifactResolver,
                project,
                repoSession,
                projectDependenciesResolver
        );

        metaDataReader.forEachDependency((dependency, packaj) -> {

            String version = packaj.getVersion().endsWith("-SNAPSHOT")
                    ? '~' + packaj.getVersion()
                    : packaj.getVersion();

            projectBuilder.addPeerDependency(
                    packaj.getName(),
                    dependency.getArtifact().getFile().toURI().toString(),
                    version
            );
        });
        return projectBuilder;
    }
}
