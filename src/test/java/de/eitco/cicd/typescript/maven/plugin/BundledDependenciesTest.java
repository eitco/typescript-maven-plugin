package de.eitco.cicd.typescript.maven.plugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BundledDependenciesTest {

    @TempDir
    Path project;

    @TempDir
    Path dist;

    @Test
    void copiesTransitiveDependenciesOfNpmLayout() throws IOException {

        writePackage(project.resolve("node_modules/a"), "a", "1.0.0", "\"b\": \"^2.0.0\"");
        writePackage(project.resolve("node_modules/b"), "b", "2.0.0", "");
        writePackage(project.resolve("node_modules/unrelated"), "unrelated", "1.0.0", "");

        Map<String, String> versions = BundledDependencies.copy(project.toFile(), List.of("a"), dist.toFile());

        assertEquals(Map.of("a", "1.0.0"), versions);
        assertEquals("2.0.0", version(dist.resolve("node_modules/b")));
        assertTrue(Files.isRegularFile(dist.resolve("node_modules/a/index.js")));
        assertFalse(Files.exists(dist.resolve("node_modules/unrelated")));
    }

    @Test
    void copiesTransitiveDependenciesOfPnpmLayout() throws IOException {

        // pnpm links node_modules/a to its virtual store, where a's dependencies are siblings of a
        Path a = writePackage(project.resolve("node_modules/.pnpm/a@1.0.0/node_modules/a"), "a", "1.0.0", "\"@scope/b\": \"2.0.0\"");
        Path b = writePackage(project.resolve("node_modules/.pnpm/@scope+b@2.0.0/node_modules/@scope/b"), "@scope/b", "2.0.0", "");

        Files.createSymbolicLink(project.resolve("node_modules/a"), a);
        Files.createDirectories(project.resolve("node_modules/.pnpm/a@1.0.0/node_modules/@scope"));
        Files.createSymbolicLink(project.resolve("node_modules/.pnpm/a@1.0.0/node_modules/@scope/b"), b);

        Map<String, String> versions = BundledDependencies.copy(project.toFile(), List.of("a"), dist.toFile());

        assertEquals(Map.of("a", "1.0.0"), versions);
        assertEquals("2.0.0", version(dist.resolve("node_modules/@scope/b")));
        assertFalse(Files.isSymbolicLink(dist.resolve("node_modules/a")), "links must be resolved, not copied");
        assertTrue(Files.isRegularFile(dist.resolve("node_modules/a/index.js")));
    }

    @Test
    void conflictingVersionsAreNested() throws IOException {

        writePackage(project.resolve("node_modules/a"), "a", "1.0.0", "\"c\": \"1.0.0\"");
        writePackage(project.resolve("node_modules/a/node_modules/c"), "c", "1.0.0", "");
        writePackage(project.resolve("node_modules/b"), "b", "1.0.0", "\"c\": \"2.0.0\"");
        writePackage(project.resolve("node_modules/c"), "c", "2.0.0", "");

        BundledDependencies.copy(project.toFile(), List.of("a", "b"), dist.toFile());

        // node resolves c from b to b/node_modules/c and from a to the top level c
        assertEquals("1.0.0", version(dist.resolve("node_modules/c")));
        assertEquals("2.0.0", version(dist.resolve("node_modules/b/node_modules/c")));
        assertFalse(Files.exists(dist.resolve("node_modules/a/node_modules")));
    }

    @Test
    void cyclicDependenciesTerminate() throws IOException {

        writePackage(project.resolve("node_modules/a"), "a", "1.0.0", "\"b\": \"1.0.0\"");
        writePackage(project.resolve("node_modules/b"), "b", "1.0.0", "\"a\": \"1.0.0\"");

        BundledDependencies.copy(project.toFile(), List.of("a"), dist.toFile());

        assertEquals("1.0.0", version(dist.resolve("node_modules/b")));
        assertFalse(Files.exists(dist.resolve("node_modules/b/node_modules")));
    }

    @Test
    void missingOptionalDependenciesAreIgnored() throws IOException {

        Path a = project.resolve("node_modules/a");
        Files.createDirectories(a);
        Files.writeString(a.resolve("package.json"),
                "{\"name\": \"a\", \"version\": \"1.0.0\", \"optionalDependencies\": {\"fsevents\": \"2.0.0\"}}");

        assertEquals(Map.of("a", "1.0.0"), BundledDependencies.copy(project.toFile(), List.of("a"), dist.toFile()));
    }

    @Test
    void missingDependenciesAreRejected() throws IOException {

        writePackage(project.resolve("node_modules/a"), "a", "1.0.0", "\"b\": \"1.0.0\"");

        assertThrows(IOException.class, () -> BundledDependencies.copy(project.toFile(), List.of("a"), dist.toFile()));
        assertThrows(IOException.class, () -> BundledDependencies.copy(project.toFile(), List.of("missing"), dist.toFile()));
    }

    @Test
    void staleFilesOfPreviousBuildsAreRemoved() throws IOException {

        writePackage(project.resolve("node_modules/a"), "a", "1.0.0", "");
        writePackage(dist.resolve("node_modules/old"), "old", "1.0.0", "");

        BundledDependencies.copy(project.toFile(), List.of("a"), dist.toFile());

        assertFalse(Files.exists(dist.resolve("node_modules/old")));
    }

    private static Path writePackage(Path directory, String name, String version, String dependencies) throws IOException {

        Files.createDirectories(directory);
        Files.writeString(directory.resolve("package.json"),
                "{\"name\": \"" + name + "\", \"version\": \"" + version + "\", \"dependencies\": {" + dependencies + "}}");
        Files.writeString(directory.resolve("index.js"), "module.exports = {}");

        return directory;
    }

    private static String version(Path directory) throws IOException {
        return NpmDependencyMetaDataReader.makeObjectMapper().readTree(directory.resolve("package.json").toFile()).get("version").asText();
    }
}
