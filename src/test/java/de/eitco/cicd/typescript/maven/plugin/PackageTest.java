package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PackageTest {

    private final ObjectMapper mapper = NpmDependencyMetaDataReader.makeObjectMapper();

    @Test
    void deserializesValidV8PackageJson() throws Exception {

        String json = """
                {
                  "name": "my-lib",
                  "version": "1.2.3",
                  "description": "a lib",
                  "keywords": ["a", "b"],
                  "homepage": "https://example.com",
                  "license": "MIT",
                  "author": { "name": "Jan", "email": "j@eitco.de" },
                  "private": false,
                  "main": "index.js",
                  "types": "index.d.ts",
                  "files": ["**"],
                  "scripts": { "compile": "tsc" },
                  "dependencies": { "socket.io": "4.7.4" },
                  "devDependencies": { "typescript": "5.0.0" },
                  "peerDependencies": { "@angular/core": "17.0.0" },
                  "bundledDependencies": ["socket.io"]
                }
                """;

        Package p = mapper.readValue(json, Package.class);

        assertEquals("my-lib", p.getName());
        assertEquals("1.2.3", p.getVersion());
        assertEquals("a lib", p.getDescription());
        assertEquals(List.of("a", "b"), p.getKeywords());
        assertEquals("https://example.com", p.getHomepage());
        assertEquals("MIT", p.getLicense());
        assertNotNull(p.getAuthor());
        assertEquals("Jan", p.getAuthor().getName());
        assertEquals("j@eitco.de", p.getAuthor().getEmail());
        assertFalse(p.isPrivate());
        assertEquals("index.js", p.getMain());
        assertEquals("index.d.ts", p.getTypes());
        assertEquals(List.of("**"), p.getFiles());
        assertEquals(Map.of("compile", "tsc"), p.getScripts());
        assertEquals(Map.of("socket.io", "4.7.4"), p.getDependencies());
        assertEquals(Map.of("typescript", "5.0.0"), p.getDevDependencies());
        assertEquals(Map.of("@angular/core", "17.0.0"), p.getPeerDependencies());
        assertEquals(List.of("socket.io"), p.getBundledDependencies());
    }

    @Test
    void deserializesBundleDependenciesAlias() throws Exception {

        // npm honours both spellings; the array form must still parse.
        Package p = mapper.readValue("""
                { "name": "x", "version": "1.0.0", "bundleDependencies": ["foo"] }
                """, Package.class);

        assertEquals(List.of("foo"), p.getBundledDependencies());
    }

    @Test
    void singleStringParsesAsArray() throws Exception {

        // a valid package.json may specify a single string where an array is expected.
        Package p = mapper.readValue("""
                { "name": "x", "version": "1.0.0", "os": "linux", "keywords": "cli" }
                """, Package.class);

        assertEquals(List.of("linux"), p.getOs());
        assertEquals(List.of("cli"), p.getKeywords());
    }

    @Test
    void readsAndRoundTripsModernFields() throws Exception {

        String json = """
                {
                  "name": "modern",
                  "version": "2.0.0",
                  "type": "module",
                  "engines": { "node": ">=18" },
                  "os": ["linux", "darwin"],
                  "cpu": ["x64"],
                  "optionalDependencies": { "fsevents": "2.3.3" },
                  "repository": { "type": "git", "url": "https://github.com/eitco/x.git" },
                  "bugs": "https://github.com/eitco/x/issues",
                  "funding": { "type": "individual", "url": "https://donate.example" },
                  "exports": { ".": "./index.js", "./sub": "./sub.js" },
                  "imports": { "#dep": "./dep.js" },
                  "overrides": { "foo": "1.0.0" },
                  "directories": { "lib": "lib" },
                  "peerDependenciesMeta": { "@angular/core": { "optional": true } }
                }
                """;

        Package p = mapper.readValue(json, Package.class);

        assertEquals("module", p.getType());
        assertEquals(Map.of("node", ">=18"), p.getEngines());
        assertEquals(List.of("linux", "darwin"), p.getOs());
        assertEquals(List.of("x64"), p.getCpu());
        assertEquals(Map.of("fsevents", "2.3.3"), p.getOptionalDependencies());
        assertNotNull(p.getRepository());
        assertEquals("git", p.getRepository().get("type").asText());
        assertNotNull(p.getBugs());
        assertEquals("https://github.com/eitco/x/issues", p.getBugs().asText());
        assertNotNull(p.getFunding());
        assertNotNull(p.getExports());
        assertNotNull(p.getImports());
        assertNotNull(p.getOverrides());
        assertNotNull(p.getDirectories());
        assertEquals("lib", p.getDirectories().getLib());
        assertNotNull(p.getPeerDependenciesMeta());
        assertTrue(p.getPeerDependenciesMeta().get("@angular/core").getOptional());

        // round-trip: re-serialize and re-read, no data loss for the modern fields.
        String serialized = mapper.writeValueAsString(p);
        JsonNode reparsed = mapper.readTree(serialized);

        assertEquals("module", reparsed.get("type").asText());
        assertTrue(reparsed.has("exports"));
        assertTrue(reparsed.has("imports"));
        assertTrue(reparsed.has("overrides"));
        assertTrue(reparsed.has("repository"));
        assertTrue(reparsed.has("engines"));
        assertTrue(reparsed.has("directories"));
        assertTrue(reparsed.has("funding"));
        assertTrue(reparsed.has("peerDependenciesMeta"));
        assertEquals("https://github.com/eitco/x/issues", reparsed.get("bugs").asText());
    }

    @Test
    void authorAcceptsShortStringForm() throws Exception {

        Package p = mapper.readValue("""
                {
                  "name": "x",
                  "version": "1.0.0",
                  "author": "Jan Schwarz <j@eitco.de> (https://eitco.de)"
                }
                """, Package.class);

        assertNotNull(p.getAuthor());
        assertEquals("Jan Schwarz", p.getAuthor().getName());
        assertEquals("j@eitco.de", p.getAuthor().getEmail());
        assertEquals("https://eitco.de", p.getAuthor().getUrl());
    }

    @Test
    void contributorAcceptsShortStringForm() throws Exception {

        Package p = mapper.readValue("""
                {
                  "name": "x",
                  "version": "1.0.0",
                  "contributors": ["Barney Rubble <b@rubble.com>"]
                }
                """, Package.class);

        assertNotNull(p.getContributors());
        assertEquals(1, p.getContributors().size());
        assertEquals("Barney Rubble", p.getContributors().get(0).getName());
        assertEquals("b@rubble.com", p.getContributors().get(0).getEmail());
    }

    @Test
    void mergePreservesModernSecondaryFields() throws Exception {

        Package primary = new Package().setName("x").setVersion("1.0.0");

        Package secondary = mapper.readValue("""
                {
                  "engines": { "node": ">=18" },
                  "type": "module",
                  "exports": { ".": "./index.js" },
                  "repository": { "type": "git", "url": "https://example/x.git" }
                }
                """, Package.class);

        Package merged = primary.merge(secondary);

        assertEquals("x", merged.getName());
        assertEquals("1.0.0", merged.getVersion());
        assertEquals("module", merged.getType());
        assertEquals(Map.of("node", ">=18"), merged.getEngines());
        assertNotNull(merged.getExports());
        assertNotNull(merged.getRepository());
    }

    @Test
    void diffContainsExtraModernFields() throws Exception {

        Package actual = mapper.readValue("""
                {
                  "name": "x", "version": "1.0.0",
                  "engines": { "node": ">=18" },
                  "optionalDependencies": { "fsevents": "2.3.3" }
                }
                """, Package.class);

        Package expected = mapper.readValue("""
                { "name": "x", "version": "1.0.0" }
                """, Package.class);

        Package diff = actual.diff(expected);

        assertNotNull(diff);
        assertEquals(Map.of("node", ">=18"), diff.getEngines());
        assertEquals(Map.of("fsevents", "2.3.3"), diff.getOptionalDependencies());
    }
}
