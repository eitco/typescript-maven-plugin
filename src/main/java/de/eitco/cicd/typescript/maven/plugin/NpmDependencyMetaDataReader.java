/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 04.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.eitco.cicd.maven.plugin.utility.JsonDependencyMetaDataReader;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResolver;
import org.eclipse.aether.RepositorySystemSession;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NpmDependencyMetaDataReader extends JsonDependencyMetaDataReader<Package> {

    public static final String NPM_ARTIFACT_EXTENSION = "tgz";
    public static final String NPM_METADATA_EXTENSION = "package";

    public NpmDependencyMetaDataReader(
        MavenSession session,
        List<ArtifactRepository> remoteRepositories,
        ArtifactResolver artifactResolver,
        MavenProject project,
        RepositorySystemSession repoSession,
        ProjectDependenciesResolver projectDependenciesResolver
    ) {
        super(
            session,
            remoteRepositories,
            artifactResolver,
            project,
            repoSession,
            projectDependenciesResolver,
            NPM_METADATA_EXTENSION,
            NPM_ARTIFACT_EXTENSION,
            Package.class,
            makeObjectMapper()
        );
    }

    @NotNull
    public static ObjectMapper makeObjectMapper() {

        final ObjectMapper result = new ObjectMapper();

        result.registerModule(new ParameterNamesModule());
        result.enable(SerializationFeature.INDENT_OUTPUT);

        return result;
    }
}
