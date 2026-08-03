/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Mirrors the {@code directories} object of a npm {@code package.json}.
 *
 * @see <a href="https://docs.npmjs.com/cli/v12/configuring-npm/package-json#directories">package.json directories</a>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Directories {

    private String bin;
    private String man;
    private String lib;
    private String doc;
    private String example;
    private String test;

    public String getBin() {
        return bin;
    }

    public Directories setBin(String bin) {
        this.bin = bin;
        return this;
    }

    public String getMan() {
        return man;
    }

    public Directories setMan(String man) {
        this.man = man;
        return this;
    }

    public String getLib() {
        return lib;
    }

    public Directories setLib(String lib) {
        this.lib = lib;
        return this;
    }

    public String getDoc() {
        return doc;
    }

    public Directories setDoc(String doc) {
        this.doc = doc;
        return this;
    }

    public String getExample() {
        return example;
    }

    public Directories setExample(String example) {
        this.example = example;
        return this;
    }

    public String getTest() {
        return test;
    }

    public Directories setTest(String test) {
        this.test = test;
        return this;
    }
}
