/*
 * Copyright (c) 2026 EITCO GmbH
 * All rights reserved.
 *
 * Created on 26.10.2026
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.google.common.base.Strings;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * Translates registry credentials into npm configuration given as environment variables. npm (since version 7)
 * reads every environment variable prefixed with {@code npm_config_} as configuration entry and does not normalize
 * registry scoped keys starting with {@code //}. This way credentials can be handed to npm without writing them to
 * an {@code .npmrc} file.
 */
public final class NpmRegistryCredentials {

    private static final String NPM_CONFIG_PREFIX = "npm_config_";

    private NpmRegistryCredentials() {
    }

    /**
     * Computes the registry key npm uses to look up credentials ("nerf dart") - the url without scheme, user info,
     * query and fragment. In contrast to npm, a missing trailing slash is appended, so that the last path segment
     * is not dropped.
     */
    public static String nerfDart(String registryUrl) {

        String url = registryUrl.endsWith("/") ? registryUrl : registryUrl + "/";

        URI uri = URI.create(url);

        String path = Strings.isNullOrEmpty(uri.getRawPath()) ? "/" : uri.getRawPath();
        String port = uri.getPort() == -1 ? "" : ":" + uri.getPort();

        return "//" + uri.getHost() + port + path;
    }

    /**
     * Creates the environment variables authenticating npm against the given registry. If a username is given,
     * basic authentication is used, otherwise the password is used as auth token.
     */
    public static Map<String, String> toEnvironment(String registryUrl, String username, String password) {

        if (Strings.isNullOrEmpty(registryUrl)) {

            throw new IllegalArgumentException("no registry url given to authenticate against");
        }

        if (Strings.isNullOrEmpty(password)) {

            throw new IllegalArgumentException("no password or token given for registry " + registryUrl);
        }

        String key = NPM_CONFIG_PREFIX + nerfDart(registryUrl);

        if (Strings.isNullOrEmpty(username)) {

            return Map.of(key + ":_authToken", password);
        }

        String auth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

        return Map.of(key + ":_auth", auth);
    }
}
