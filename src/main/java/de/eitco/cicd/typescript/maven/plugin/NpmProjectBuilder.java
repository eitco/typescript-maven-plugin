/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 03.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import org.apache.maven.model.License;
import org.apache.maven.project.MavenProject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NpmProjectBuilder {

    public static final String EXTENSION_TYPESCRIPT_HEADER = ".d.ts";
    public static final String EXTENSION_JAVASCRIPT = ".js";
    private final String name;
    private final String version;
    private String types;
    private String main;
    private List<String> files = List.of("**");
    private String registry;
    private Map<String, String> scripts = new LinkedHashMap<>(Map.of(
            "compile", "tsc"
    ));
    private MavenProject mavenProject;
    private List<String> keywords;
    private Package defaultValues;
    private String schematics;


    private NpmProjectBuilder(String name, String version) {
        this.name = name;
        this.version = version;
    }

    private Map<String, String> dependencies = new LinkedHashMap<>();
    private Map<String, String> devDependencies = new LinkedHashMap<>();
    private Map<String, String> peerDependencies = new LinkedHashMap<>();
    private List<String> bundledDependencies = new ArrayList<>();

    public static NpmProjectBuilder identifiedBy(String name, String version) {
        return new NpmProjectBuilder(name, version);
    }

    public NpmProjectBuilder withDefaultValues(@NotNull Package defaultValues) {

        this.defaultValues = defaultValues;

        return this;
    }

    public NpmProjectBuilder fromProject(MavenProject mavenProject) {

        this.mavenProject = mavenProject;

        return this;
    }

    public NpmProjectBuilder withKeyWords(List<String> keywords) {

        this.keywords = keywords;

        return this;
    }

    public NpmProjectBuilder withEntryPoint(String entryPoint) {

        this.types = entryPoint + EXTENSION_TYPESCRIPT_HEADER;
        this.main = entryPoint + EXTENSION_JAVASCRIPT;

        return this;
    }

    public NpmProjectBuilder publishedTo(String registry) {

        this.registry = registry;

        return this;
    }


    public void addPeerDependency(String name, String buildVersion, String peerVersion) {

        dependencies.put(name, buildVersion);
        peerDependencies.put(name, peerVersion);
    }

    public void addDependency(NpmDependency npmDependency) {

        if (npmDependency.getType() == DependencyType.DEV) {

            putDependency(devDependencies, npmDependency);
        }

        if (npmDependency.getType() == DependencyType.BUNDLED) {

            putDependency(dependencies, npmDependency);
            bundledDependencies.add(npmDependency.getName());
        }

        if (npmDependency.getType() == DependencyType.PEER) {

            putDependency(dependencies, npmDependency);
            putDependency(peerDependencies, npmDependency);
        }

    }

    private void putDependency(Map<String, String> devDependencies, NpmDependency npmDependency) {
        devDependencies.put(npmDependency.getName(), npmDependency.getVersion());
    }

    public Package makeBuildPackage() {

        return mergeDefaultValues(new Package(
                name,
                version,
                mavenProject.getDescription(),
                keywords,
                mavenProject.getUrl(),
                convertLicenses(),
                getAuthor(),
                getContributors(),
                false,
                null,
                null,
                null,
                scripts,
                dependencies,
                devDependencies,
                null,
                null,
                bundledDependencies,
                schematics
        ));
    }

    private Package mergeDefaultValues(Package result) {

        if (defaultValues == null) {

            return result;
        }

        return result.merge(defaultValues);
    }

    private Person getAuthor() {

        if (mavenProject.getOrganization() == null) {

            return null;
        }

        final Person author = new Person();

        author.setName(mavenProject.getOrganization().getName());
        author.setUrl(mavenProject.getOrganization().getUrl());

        return author;
    }

    @NotNull
    private List<Person> getContributors() {

        if (mavenProject.getDevelopers() == null) {

            return null;
        }

        return mavenProject.getDevelopers().stream().map(it -> {

            final Person result = new Person();

            result.setName(it.getName());
            result.setEmail(it.getEmail());
            result.setUrl(it.getUrl());

            return result;
        }).collect(Collectors.toList());
    }

    @NotNull
    private String convertLicenses() {

        if (mavenProject.getLicenses() == null || mavenProject.getLicenses().isEmpty()) {

            return null;
        }

        if (mavenProject.getLicenses().size() == 1) {

            return mavenProject.getLicenses().get(0).getName();
        }

        return "(" + mavenProject.getLicenses().stream().map(License::getName).collect(Collectors.joining(" OR ")) + ")";
    }

    public Package makeDeployPackage() {

        return mergeDefaultValues(new Package(
                name,
                version,
                mavenProject.getDescription(),
                keywords,
                mavenProject.getUrl(),
                convertLicenses(),
                getAuthor(),
                getContributors(),
                false,
                types,
                main,
                files,
                null,
                null,
                null,
                peerDependencies,
                (registry != null) ? Map.of("registry", registry) : null,
                bundledDependencies,
                schematics));
    }

    public NpmProjectBuilder addScript(String name, String content) {

        scripts.put(name, content);

        return this;
    }

    public String getName() {
        return name;
    }

    public NpmProjectBuilder withSchematics(String schematics) {
        this.schematics = schematics;
        return this;
    }
}
