/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 09.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Person {

    private String name;
    private String email;
    private String url;

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Person setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Person setUrl(String url) {
        this.url = url;
        return this;
    }
}
