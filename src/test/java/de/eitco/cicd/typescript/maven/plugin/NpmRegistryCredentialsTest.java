package de.eitco.cicd.typescript.maven.plugin;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NpmRegistryCredentialsTest {

    @Test
    void nerfDartKeepsPathWithTrailingSlash() {

        assertEquals("//nexus.example.com/repository/npm-private/",
                NpmRegistryCredentials.nerfDart("https://nexus.example.com/repository/npm-private/"));
    }

    @Test
    void nerfDartTreatsLastSegmentAsDirectoryWithoutTrailingSlash() {

        assertEquals("//nexus.example.com/repository/npm-private/",
                NpmRegistryCredentials.nerfDart("https://nexus.example.com/repository/npm-private"));
    }

    @Test
    void nerfDartKeepsPortAndIgnoresScheme() {

        assertEquals("//localhost:8081/repository/npm/",
                NpmRegistryCredentials.nerfDart("http://localhost:8081/repository/npm/"));
    }

    @Test
    void nerfDartOfHostOnlyUrl() {

        assertEquals("//registry.npmjs.org/", NpmRegistryCredentials.nerfDart("https://registry.npmjs.org"));
    }

    @Test
    void nerfDartDropsQueryAndCredentials() {

        assertEquals("//registry.npmjs.org/", NpmRegistryCredentials.nerfDart("https://user@registry.npmjs.org/?a=b"));
    }

    @Test
    void passwordWithoutUsernameIsUsedAsToken() {

        Map<String, String> environment = NpmRegistryCredentials.toEnvironment(
                "https://registry.npmjs.org/", null, "npm_secretToken");

        assertEquals(Map.of("npm_config_//registry.npmjs.org/:_authToken", "npm_secretToken"), environment);
    }

    @Test
    void usernameAndPasswordAreUsedAsBasicAuth() {

        Map<String, String> environment = NpmRegistryCredentials.toEnvironment(
                "https://nexus.example.com/repository/npm-private/", "deployer", "s3cr:t");

        String expected = Base64.getEncoder().encodeToString("deployer:s3cr:t".getBytes(StandardCharsets.UTF_8));

        assertEquals(Map.of("npm_config_//nexus.example.com/repository/npm-private/:_auth", expected), environment);
    }

    @Test
    void missingPasswordIsRejected() {

        assertThrows(IllegalArgumentException.class,
                () -> NpmRegistryCredentials.toEnvironment("https://registry.npmjs.org/", "deployer", null));
    }

    @Test
    void missingRegistryIsRejected() {

        assertThrows(IllegalArgumentException.class,
                () -> NpmRegistryCredentials.toEnvironment("", "deployer", "secret"));
    }
}
