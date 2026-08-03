/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Mirrors a single entry of the {@code peerDependenciesMeta} object of a npm {@code package.json},
 * e.g. {@code { "optional": true }}.
 *
 * @see <a href="https://docs.npmjs.com/cli/v12/configuring-npm/package-json#peerdependenciesmeta">package.json peerDependenciesMeta</a>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PeerDependencyMeta {

    private Boolean optional;

    public Boolean getOptional() {
        return optional;
    }

    public PeerDependencyMeta setOptional(Boolean optional) {
        this.optional = optional;
        return this;
    }
}
