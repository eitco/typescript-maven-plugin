/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 03.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

public class NpmDependency {

    private String name;
    private String version;
    private DependencyType type = DependencyType.BUNDLED;

    public String getName() {
        return name;
    }

    public NpmDependency setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public NpmDependency setVersion(String version) {
        this.version = version;
        return this;
    }

    public DependencyType getType() {
        return type;
    }

    public NpmDependency setType(DependencyType type) {
        this.type = type;
        return this;
    }
}
