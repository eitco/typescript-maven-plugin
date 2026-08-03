/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 03.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a npm {@code package.json}. The class models all top-level fields of the current npm
 * version (v12, see reference below) while staying backwards compatible: a valid v8 {@code package.json}
 * is still recognized. Fields that npm allows in several shapes (string-or-object, string-or-array, ...)
 * are kept as raw {@link JsonNode} so any valid form round-trips without loss; unknown fields are ignored.
 *
 * @see <a href="https://docs.npmjs.com/cli/v12/configuring-npm/package-json">package.json reference</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Package {

    // --- identification / description ---
    private String name;
    private String version;
    private String description;
    private List<String> keywords;
    private String homepage;
    private JsonNode bugs;
    private String license;

    // --- people ---
    private Person author;
    private List<Person> contributors;
    private List<Person> maintainers;
    private JsonNode funding;

    // --- publishing / flags ---
    @JsonProperty("private")
    private boolean isPrivate;
    private String type;

    // --- entry points ---
    @JsonAlias("typings")
    private String types;
    private String main;
    private JsonNode browser;
    private JsonNode bin;
    private List<String> man;
    private Directories directories;
    private JsonNode repository;
    private List<String> files;
    private JsonNode exports;
    private JsonNode imports;

    // --- scripts / config ---
    private Map<String, String> scripts;
    private JsonNode config;

    // --- dependencies ---
    private Map<String, String> dependencies;
    private Map<String, String> devDependencies;
    private Map<String, String> peerDependencies;
    private Map<String, PeerDependencyMeta> peerDependenciesMeta;
    @JsonAlias("bundleDependencies")
    private List<String> bundledDependencies;
    private Map<String, String> optionalDependencies;
    private JsonNode overrides;
    private JsonNode packageExtensions;

    // --- platform constraints ---
    private Map<String, String> engines;
    private List<String> os;
    private List<String> cpu;
    private List<String> libc;
    private JsonNode devEngines;

    // --- misc ---
    private Boolean gypfile;
    private JsonNode sideEffects;
    private JsonNode workspaces;
    private Map<String, String> publishConfig;

    // --- angular specific extension (not part of the npm spec) ---
    private String schematics;

    public String getName() {
        return name;
    }

    public Package setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Package setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Package setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Package setKeywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    public String getHomepage() {
        return homepage;
    }

    public Package setHomepage(String homepage) {
        this.homepage = homepage;
        return this;
    }

    public JsonNode getBugs() {
        return bugs;
    }

    public Package setBugs(JsonNode bugs) {
        this.bugs = bugs;
        return this;
    }

    public String getLicense() {
        return license;
    }

    public Package setLicense(String license) {
        this.license = license;
        return this;
    }

    public Person getAuthor() {
        return author;
    }

    public Package setAuthor(Person author) {
        this.author = author;
        return this;
    }

    public List<Person> getContributors() {
        return contributors;
    }

    public Package setContributors(List<Person> contributors) {
        this.contributors = contributors;
        return this;
    }

    public List<Person> getMaintainers() {
        return maintainers;
    }

    public Package setMaintainers(List<Person> maintainers) {
        this.maintainers = maintainers;
        return this;
    }

    public JsonNode getFunding() {
        return funding;
    }

    public Package setFunding(JsonNode funding) {
        this.funding = funding;
        return this;
    }

    @JsonProperty("private")
    public boolean isPrivate() {
        return isPrivate;
    }

    @JsonProperty("private")
    public Package setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
        return this;
    }

    public String getType() {
        return type;
    }

    public Package setType(String type) {
        this.type = type;
        return this;
    }

    public String getTypes() {
        return types;
    }

    public Package setTypes(String types) {
        this.types = types;
        return this;
    }

    public String getMain() {
        return main;
    }

    public Package setMain(String main) {
        this.main = main;
        return this;
    }

    public JsonNode getBrowser() {
        return browser;
    }

    public Package setBrowser(JsonNode browser) {
        this.browser = browser;
        return this;
    }

    public JsonNode getBin() {
        return bin;
    }

    public Package setBin(JsonNode bin) {
        this.bin = bin;
        return this;
    }

    public List<String> getMan() {
        return man;
    }

    public Package setMan(List<String> man) {
        this.man = man;
        return this;
    }

    public Directories getDirectories() {
        return directories;
    }

    public Package setDirectories(Directories directories) {
        this.directories = directories;
        return this;
    }

    public JsonNode getRepository() {
        return repository;
    }

    public Package setRepository(JsonNode repository) {
        this.repository = repository;
        return this;
    }

    public List<String> getFiles() {
        return files;
    }

    public Package setFiles(List<String> files) {
        this.files = files;
        return this;
    }

    public JsonNode getExports() {
        return exports;
    }

    public Package setExports(JsonNode exports) {
        this.exports = exports;
        return this;
    }

    public JsonNode getImports() {
        return imports;
    }

    public Package setImports(JsonNode imports) {
        this.imports = imports;
        return this;
    }

    public Map<String, String> getScripts() {
        return scripts;
    }

    public Package setScripts(Map<String, String> scripts) {
        this.scripts = scripts;
        return this;
    }

    public JsonNode getConfig() {
        return config;
    }

    public Package setConfig(JsonNode config) {
        this.config = config;
        return this;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public Package setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    public Map<String, String> getDevDependencies() {
        return devDependencies;
    }

    public Package setDevDependencies(Map<String, String> devDependencies) {
        this.devDependencies = devDependencies;
        return this;
    }

    public Map<String, String> getPeerDependencies() {
        return peerDependencies;
    }

    public Package setPeerDependencies(Map<String, String> peerDependencies) {
        this.peerDependencies = peerDependencies;
        return this;
    }

    public Map<String, PeerDependencyMeta> getPeerDependenciesMeta() {
        return peerDependenciesMeta;
    }

    public Package setPeerDependenciesMeta(Map<String, PeerDependencyMeta> peerDependenciesMeta) {
        this.peerDependenciesMeta = peerDependenciesMeta;
        return this;
    }

    public List<String> getBundledDependencies() {
        return bundledDependencies;
    }

    public Package setBundledDependencies(List<String> bundledDependencies) {
        this.bundledDependencies = bundledDependencies;
        return this;
    }

    public Map<String, String> getOptionalDependencies() {
        return optionalDependencies;
    }

    public Package setOptionalDependencies(Map<String, String> optionalDependencies) {
        this.optionalDependencies = optionalDependencies;
        return this;
    }

    public JsonNode getOverrides() {
        return overrides;
    }

    public Package setOverrides(JsonNode overrides) {
        this.overrides = overrides;
        return this;
    }

    public JsonNode getPackageExtensions() {
        return packageExtensions;
    }

    public Package setPackageExtensions(JsonNode packageExtensions) {
        this.packageExtensions = packageExtensions;
        return this;
    }

    public Map<String, String> getEngines() {
        return engines;
    }

    public Package setEngines(Map<String, String> engines) {
        this.engines = engines;
        return this;
    }

    public List<String> getOs() {
        return os;
    }

    public Package setOs(List<String> os) {
        this.os = os;
        return this;
    }

    public List<String> getCpu() {
        return cpu;
    }

    public Package setCpu(List<String> cpu) {
        this.cpu = cpu;
        return this;
    }

    public List<String> getLibc() {
        return libc;
    }

    public Package setLibc(List<String> libc) {
        this.libc = libc;
        return this;
    }

    public JsonNode getDevEngines() {
        return devEngines;
    }

    public Package setDevEngines(JsonNode devEngines) {
        this.devEngines = devEngines;
        return this;
    }

    public Boolean getGypfile() {
        return gypfile;
    }

    public Package setGypfile(Boolean gypfile) {
        this.gypfile = gypfile;
        return this;
    }

    public JsonNode getSideEffects() {
        return sideEffects;
    }

    public Package setSideEffects(JsonNode sideEffects) {
        this.sideEffects = sideEffects;
        return this;
    }

    public JsonNode getWorkspaces() {
        return workspaces;
    }

    public Package setWorkspaces(JsonNode workspaces) {
        this.workspaces = workspaces;
        return this;
    }

    public Map<String, String> getPublishConfig() {
        return publishConfig;
    }

    public Package setPublishConfig(Map<String, String> publishConfig) {
        this.publishConfig = publishConfig;
        return this;
    }

    public String getSchematics() {
        return schematics;
    }

    public Package setSchematics(String schematics) {
        this.schematics = schematics;
        return this;
    }

    /**
     * Merges this package (primary) with the given secondary package. For every field the primary
     * (non-null) value wins; collections and maps are combined, with the primary entries taking
     * precedence over colliding secondary ones.
     */
    @NotNull
    @Contract("null -> this")
    public Package merge(@Nullable Package secondary) {

        if (secondary == null) {

            return this;
        }

        return new Package()
                .setName(merge(name, secondary.name))
                .setVersion(merge(version, secondary.version))
                .setDescription(merge(description, secondary.description))
                .setKeywords(mergeCollection(keywords, secondary.keywords))
                .setHomepage(merge(homepage, secondary.homepage))
                .setBugs(merge(bugs, secondary.bugs))
                .setLicense(merge(license, secondary.license))
                .setAuthor(merge(author, secondary.author))
                .setContributors(mergeCollection(contributors, secondary.contributors))
                .setMaintainers(mergeCollection(maintainers, secondary.maintainers))
                .setFunding(merge(funding, secondary.funding))
                .setPrivate(merge(isPrivate, secondary.isPrivate))
                .setType(merge(type, secondary.type))
                .setTypes(merge(types, secondary.types))
                .setMain(merge(main, secondary.main))
                .setBrowser(merge(browser, secondary.browser))
                .setBin(merge(bin, secondary.bin))
                .setMan(mergeCollection(man, secondary.man))
                .setDirectories(merge(directories, secondary.directories))
                .setRepository(merge(repository, secondary.repository))
                .setFiles(mergeCollection(files, secondary.files))
                .setExports(merge(exports, secondary.exports))
                .setImports(merge(imports, secondary.imports))
                .setScripts(mergeMap(scripts, secondary.scripts))
                .setConfig(merge(config, secondary.config))
                .setDependencies(mergeMap(dependencies, secondary.dependencies))
                .setDevDependencies(mergeMap(devDependencies, secondary.devDependencies))
                .setPeerDependencies(mergeMap(peerDependencies, secondary.peerDependencies))
                .setPeerDependenciesMeta(mergeMap(peerDependenciesMeta, secondary.peerDependenciesMeta))
                .setBundledDependencies(mergeCollection(bundledDependencies, secondary.bundledDependencies))
                .setOptionalDependencies(mergeMap(optionalDependencies, secondary.optionalDependencies))
                .setOverrides(merge(overrides, secondary.overrides))
                .setPackageExtensions(merge(packageExtensions, secondary.packageExtensions))
                .setEngines(mergeMap(engines, secondary.engines))
                .setOs(mergeCollection(os, secondary.os))
                .setCpu(mergeCollection(cpu, secondary.cpu))
                .setLibc(mergeCollection(libc, secondary.libc))
                .setDevEngines(merge(devEngines, secondary.devEngines))
                .setGypfile(merge(gypfile, secondary.gypfile))
                .setSideEffects(merge(sideEffects, secondary.sideEffects))
                .setWorkspaces(merge(workspaces, secondary.workspaces))
                .setPublishConfig(mergeMap(publishConfig, secondary.publishConfig))
                .setSchematics(merge(schematics, secondary.schematics));
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

    /**
     * Computes the difference between this package (the actual {@code package.json}) and the given
     * {@code expectedPackage} (the configuration produced by the build). Scalar / object fields are taken
     * from the expected package, while collection and map fields keep only the entries present in the
     * actual package but not already generated by the build.
     */
    @Nullable
    public Package diff(@Nullable Package expectedPackage) {

        if (expectedPackage == null) {

            return this;
        }

        return new Package()
                .setName(expectedPackage.name)
                .setVersion(expectedPackage.version)
                .setDescription(expectedPackage.description)
                .setKeywords(minus(keywords, expectedPackage.keywords))
                .setHomepage(expectedPackage.homepage)
                .setBugs(expectedPackage.bugs)
                .setLicense(expectedPackage.license)
                .setAuthor(expectedPackage.author)
                .setContributors(minus(contributors, expectedPackage.contributors))
                .setMaintainers(minus(maintainers, expectedPackage.maintainers))
                .setFunding(expectedPackage.funding)
                .setPrivate(expectedPackage.isPrivate())
                .setType(expectedPackage.type)
                .setTypes(expectedPackage.types)
                .setMain(expectedPackage.main)
                .setBrowser(expectedPackage.browser)
                .setBin(expectedPackage.bin)
                .setMan(minus(man, expectedPackage.man))
                .setDirectories(expectedPackage.directories)
                .setRepository(expectedPackage.repository)
                .setFiles(minus(files, expectedPackage.files))
                .setExports(expectedPackage.exports)
                .setImports(expectedPackage.imports)
                .setScripts(minus(scripts, expectedPackage.scripts))
                .setConfig(expectedPackage.config)
                .setDependencies(minus(dependencies, expectedPackage.dependencies))
                .setDevDependencies(minus(devDependencies, expectedPackage.devDependencies))
                .setPeerDependencies(minus(peerDependencies, expectedPackage.peerDependencies))
                .setPeerDependenciesMeta(minus(peerDependenciesMeta, expectedPackage.peerDependenciesMeta))
                .setBundledDependencies(minus(bundledDependencies, expectedPackage.bundledDependencies))
                .setOptionalDependencies(minus(optionalDependencies, expectedPackage.optionalDependencies))
                .setOverrides(expectedPackage.overrides)
                .setPackageExtensions(expectedPackage.packageExtensions)
                .setEngines(minus(engines, expectedPackage.engines))
                .setOs(minus(os, expectedPackage.os))
                .setCpu(minus(cpu, expectedPackage.cpu))
                .setLibc(minus(libc, expectedPackage.libc))
                .setDevEngines(expectedPackage.devEngines)
                .setGypfile(expectedPackage.gypfile)
                .setSideEffects(expectedPackage.sideEffects)
                .setWorkspaces(expectedPackage.workspaces)
                .setPublishConfig(minus(publishConfig, expectedPackage.publishConfig))
                .setSchematics(expectedPackage.schematics);
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
