/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 04.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;


import com.google.common.base.Strings;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.SettingsProblem;
import org.apache.maven.settings.crypto.DefaultSettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This goal deploys the npm package by calling {@code npm publish} (or {@code pnpm publish}).
 */
@Mojo(name = "deploy", threadSafe = true, defaultPhase = LifecyclePhase.INSTALL)
public class NpmDeployMojo extends DistributionDirectoryNpmExecutionMojo {

    /**
     * This parameter specifies the id of a server in the maven {@code settings.xml} whose credentials are used to
     * authenticate {@code npm publish} against the registry the package is published to. If the server specifies a
     * username, basic authentication is used, otherwise its password is used as auth token. Encrypted passwords are
     * supported. The credentials are handed to npm as environment variables and are never written to an
     * {@code .npmrc} file. This requires npm 7 or later, or pnpm. If left empty, npm's default authentication is used.
     */
    @Parameter(property = "typescript.publish.server.id")
    private String publishServerId;

    @Parameter(defaultValue = "${settings}", readonly = true)
    private Settings settings;

    @Component
    private SettingsDecrypter settingsDecrypter;

    private Map<String, String> credentialEnvironment = Map.of();

    @Override
    public void execute() throws MojoFailureException {

        if (!skipExecution() && !Strings.isNullOrEmpty(publishServerId)) {

            credentialEnvironment = resolveCredentials();
        }

        super.execute();
    }

    private Map<String, String> resolveCredentials() throws MojoFailureException {

        Server server = settings.getServer(publishServerId);

        if (server == null) {

            throw new MojoFailureException("no server with id '" + publishServerId + "' found in settings.xml");
        }

        SettingsDecryptionResult decryptionResult = settingsDecrypter.decrypt(new DefaultSettingsDecryptionRequest(server));

        String errors = decryptionResult.getProblems().stream()
                .filter(problem -> problem.getSeverity() != SettingsProblem.Severity.WARNING)
                .map(SettingsProblem::getMessage)
                .collect(Collectors.joining(", "));

        if (!errors.isEmpty()) {

            throw new MojoFailureException("failed to decrypt credentials of server '" + publishServerId + "': " + errors);
        }

        Server decryptedServer = decryptionResult.getServer();

        String registryUrl = Strings.isNullOrEmpty(publishRegistryURL) ? npmRegistryURL : publishRegistryURL;

        try {

            return NpmRegistryCredentials.toEnvironment(registryUrl, decryptedServer.getUsername(), decryptedServer.getPassword());

        } catch (IllegalArgumentException e) {

            throw new MojoFailureException("unable to authenticate with server '" + publishServerId + "': " + e.getMessage(), e);
        }
    }

    @Override
    protected Map<String, String> getAdditionalEnvironment() {
        return credentialEnvironment;
    }

    @Override
    protected String getNpmRegistryURL() {
        return "";
    }

    protected String getArguments() {
        return packageManager.publishArguments();
    }
}
