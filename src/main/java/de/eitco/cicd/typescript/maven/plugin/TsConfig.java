/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 04.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TsConfig {

    public static final String COMPILE_CONFIG_FILE = "tsconfig.json";
    public static final String TEST_COMPILE_CONFIG_FILE = "tsconfig.spec.json";


    private CompilerOptions compilerOptions;
    private List<String> files;
    @JsonProperty("extends")
    private String extend;
    private List<String> include;
    private List<String> exclude;
    private List<Reference> references;

    private AngularCompilerOptions angularCompilerOptions;

    public static TsConfig mainConfig(CompilerOptions compilerOptions, List<String> includes) {

        final TsConfig result = new TsConfig();

        result.setCompilerOptions(compilerOptions);
        result.setInclude(includes);

        return result;
    }

    public static TsConfig testConfig(CompilerOptions compilerOptions, List<String> includes) {

        final TsConfig result = new TsConfig();

        result.setCompilerOptions(compilerOptions);
        result.setInclude(includes);
        result.setExtend("./" + COMPILE_CONFIG_FILE);

        return result;
    }

    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    public TsConfig setCompilerOptions(CompilerOptions compilerOptions) {
        this.compilerOptions = compilerOptions;
        return this;
    }

    public List<String> getFiles() {
        return files;
    }

    public TsConfig setFiles(List<String> files) {
        this.files = files;
        return this;
    }

    public String getExtend() {
        return extend;
    }

    public TsConfig setExtend(String extend) {
        this.extend = extend;
        return this;
    }

    public List<String> getInclude() {
        return include;
    }

    public TsConfig setInclude(List<String> include) {
        this.include = include;
        return this;
    }

    public List<String> getExclude() {
        return exclude;
    }

    public TsConfig setExclude(List<String> exclude) {
        this.exclude = exclude;
        return this;
    }

    public List<Reference> getReferences() {
        return references;
    }

    public TsConfig setReferences(List<Reference> references) {
        this.references = references;
        return this;
    }

    public AngularCompilerOptions getAngularCompilerOptions() {
        return angularCompilerOptions;
    }

    public void setAngularCompilerOptions(AngularCompilerOptions angularCompilerOptions) {
        this.angularCompilerOptions = angularCompilerOptions;
    }
}
