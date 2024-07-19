/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 04.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

public class Reference {

    private String path;

    public String getPath() {
        return path;
    }

    public Reference setPath(String path) {
        this.path = path;
        return this;
    }
}
