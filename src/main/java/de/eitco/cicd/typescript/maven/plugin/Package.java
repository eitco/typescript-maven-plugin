/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 03.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Package {

    // see https://docs.npmjs.com/cli/v8/configuring-npm/package-json

    private final String name;
    private final String version;
    private final String description;
    private final List<String> keywords;
    private final String homepage;
    private final String license;
    private final Person author;
    private final List<Person> contributors;
    @JsonProperty("private")
    private final boolean isPrivate;
    private final String types;
    private final String main;
    private final List<String> files;
    private final Map<String, String> scripts;
    private final Map<String, String> dependencies;
    private final Map<String, String> devDependencies;
    private final Map<String, String> peerDependencies;
    private final Map<String, String> publishConfig;
    private final List<String> bundledDependencies;

    private final String schematics;


    @JsonCreator
    public Package(
            String name,
            String version,
            String description,
            List<String> keywords,
            String homepage,
            String license,
            Person author, List<Person> contributors,
            @JsonProperty("private") boolean isPrivate,
            String types,
            String main,
            List<String> files,
            Map<String, String> scripts,
            Map<String, String> dependencies,
            Map<String, String> devDependencies,
            Map<String, String> peerDependencies,
            Map<String, String> publishConfig,
            List<String> bundledDependencies,
            String schematics) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.keywords = keywords;
        this.homepage = homepage;
        this.license = license;
        this.author = author;
        this.contributors = contributors;
        this.isPrivate = isPrivate;
        this.types = types;
        this.main = main;
        this.files = files;
        this.scripts = scripts;
        this.dependencies = dependencies;
        this.devDependencies = devDependencies;
        this.peerDependencies = peerDependencies;
        this.publishConfig = publishConfig;
        this.bundledDependencies = bundledDependencies;
        this.schematics = schematics;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @JsonProperty("private")
    public boolean isPrivate() {
        return isPrivate;
    }

    public String getTypes() {
        return types;
    }

    public String getMain() {
        return main;
    }

    public List<String> getFiles() {
        return files;
    }

    public Map<String, String> getScripts() {
        return scripts;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public Map<String, String> getDevDependencies() {
        return devDependencies;
    }

    public Map<String, String> getPeerDependencies() {
        return peerDependencies;
    }

    public Map<String, String> getPublishConfig() {
        return publishConfig;
    }

    public List<String> getBundledDependencies() {
        return bundledDependencies;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getLicense() {
        return license;
    }

    public Person getAuthor() {
        return author;
    }

    public List<Person> getContributors() {
        return contributors;
    }

    public String getSchematics() {
        return schematics;
    }

    @NotNull
    @Contract("null -> this")
    public Package merge(@Nullable Package secondary) {

        if (secondary == null) {

            return this;
        }

        return new Package(
                merge(name, secondary.name),
                merge(version, secondary.version),
                merge(description, secondary.description),
                mergeCollection(keywords, secondary.keywords),
                merge(homepage, secondary.homepage),
                merge(license, secondary.license),
                merge(author, secondary.author),
                mergeCollection(contributors, secondary.contributors),
                merge(isPrivate, secondary.isPrivate),
                merge(types, secondary.types),
                merge(main, secondary.main),
                mergeCollection(files, secondary.files),
                mergeMap(scripts, secondary.scripts),
                mergeMap(dependencies, secondary.dependencies),
                mergeMap(devDependencies, secondary.devDependencies),
                mergeMap(peerDependencies, secondary.peerDependencies),
                mergeMap(publishConfig, secondary.publishConfig),
                mergeCollection(bundledDependencies, secondary.bundledDependencies),
                merge(schematics, secondary.schematics)
        );
    }

    @Nullable
    @Contract("null, null -> null; !null, _ -> !null; _, !null -> !null")
    private static <Type> Type merge(@Nullable Type a, @Nullable Type b) {

        if (a != null) {

            return a;
        }

        return b;
    }

    @NotNull
    private static <KeyType, ValueType> Map<KeyType, ValueType> mergeMap(@Nullable Map<KeyType, ValueType> a, @Nullable Map<KeyType, ValueType> b) {

        LinkedHashMap<KeyType, ValueType> result = new LinkedHashMap<>();

        if (a != null) result.putAll(a);
        if (b != null) result.putAll(b);
        if (a != null) result.putAll(a);

        return result;
    }

    @NotNull
    private static <Type> List<Type> mergeCollection(@Nullable Collection<Type> a, @Nullable Collection<Type> b) {

        ArrayList<Type> result = new ArrayList<>();

        if (a != null) result.addAll(a);
        if (b != null) result.addAll(b);

        return result;
    }

    @Nullable
    public Package diff(@Nullable Package expectedPackage) {

        if (expectedPackage == null) {

            return this;
        }

        return new Package(
                expectedPackage.name,
                expectedPackage.version,
                expectedPackage.description,
                minus(keywords, expectedPackage.keywords),
                expectedPackage.homepage,
                expectedPackage.license,
                expectedPackage.author,
                minus(contributors, expectedPackage.contributors),
                expectedPackage.isPrivate(),
                expectedPackage.types,
                expectedPackage.main,
                minus(files, expectedPackage.files),
                minus(scripts, expectedPackage.scripts),
                minus(dependencies, expectedPackage.dependencies),
                minus(devDependencies, expectedPackage.devDependencies),
                minus(peerDependencies, expectedPackage.peerDependencies),
                minus(publishConfig, expectedPackage.publishConfig),
                minus(bundledDependencies, expectedPackage.bundledDependencies),
                expectedPackage.schematics
        );
    }

    @NotNull
    public <ElementType> List<ElementType> minus(@Nullable List<ElementType> minuend, @Nullable List<ElementType> subtrahend) {

        if (minuend == null) return List.of();

        ArrayList<ElementType> difference = new ArrayList<>(minuend);

        if (subtrahend != null) difference.removeAll(subtrahend);

        return difference;
    }

    @NotNull
    public <KeyType, ValueType> Map<KeyType, ValueType> minus(@Nullable Map<KeyType, ValueType> minuend, @Nullable Map<KeyType, ValueType> subtrahend) {

        if (minuend == null) return Map.of();

        Map<KeyType, ValueType> difference = new LinkedHashMap<>(minuend);

        if (subtrahend != null) subtrahend.keySet().forEach(difference::remove);

        return difference;
    }

}
