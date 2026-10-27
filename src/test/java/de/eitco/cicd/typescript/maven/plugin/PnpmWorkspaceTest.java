package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PnpmWorkspaceTest {

    @TempDir
    File directory;

    @Test
    void allowBuildsAreWrittenForPnpm10AndPnpm11() throws IOException {

        Map<String, Object> yaml = parse(new PnpmWorkspace(List.of("esbuild", "@parcel/watcher"), null, List.of()));

        // pnpm 11 only reads allowBuilds, pnpm 10.16+ reads onlyBuiltDependencies
        assertEquals(Map.of("esbuild", true, "@parcel/watcher", true), yaml.get("allowBuilds"));
        assertEquals(List.of("esbuild", "@parcel/watcher"), yaml.get("onlyBuiltDependencies"));
        assertFalse(yaml.containsKey("minimumReleaseAge"));
    }

    @Test
    void minimumReleaseAgeIsWrittenAsMinutes() throws IOException {

        Map<String, Object> yaml = parse(new PnpmWorkspace(List.of(), 1440, List.of("@eitco/*")));

        assertEquals(1440, yaml.get("minimumReleaseAge"));
        assertEquals(List.of("@eitco/*"), yaml.get("minimumReleaseAgeExclude"));
        assertFalse(yaml.containsKey("allowBuilds"));
        assertFalse(yaml.containsKey("onlyBuiltDependencies"));
    }

    @Test
    void zeroMinimumReleaseAgeIsWritten() throws IOException {

        // pnpm 11 defaults to 1440 minutes - 0 must be written to disable the check
        assertEquals(0, parse(new PnpmWorkspace(null, 0, null)).get("minimumReleaseAge"));
    }

    @Test
    void emptySettings() {

        assertTrue(new PnpmWorkspace(null, null, null).isEmpty());
        assertTrue(new PnpmWorkspace(List.of(), null, List.of()).isEmpty());
        assertFalse(new PnpmWorkspace(null, 0, null).isEmpty());
    }

    @Test
    void negativeMinimumReleaseAgeIsRejected() {

        assertThrows(IllegalArgumentException.class, () -> new PnpmWorkspace(null, -1, null));
    }

    @Test
    void writesFile() throws IOException {

        File file = new File(directory, PnpmWorkspace.FILE_NAME);

        new PnpmWorkspace(List.of("esbuild"), 60, null).write(file);

        Map<?, ?> yaml = new YAMLMapper().readValue(file, Map.class);

        assertEquals(60, yaml.get("minimumReleaseAge"));
        assertFalse(Files.readString(file.toPath()).startsWith("---"));
    }

    @Test
    void writingKeepsUnmanagedSettingsAndReplacesManagedOnes() throws IOException {

        File file = new File(directory, PnpmWorkspace.FILE_NAME);

        Files.writeString(file.toPath(), "overrides:\n  foo: 1.0.0\n" +
                "allowBuilds:\n  core-js: set this to true or false\n" +
                "minimumReleaseAge: 10\n");

        new PnpmWorkspace(List.of("esbuild"), null, null).write(file);

        Map<?, ?> yaml = new YAMLMapper().readValue(file, Map.class);

        assertEquals(Map.of("foo", "1.0.0"), yaml.get("overrides"));
        assertEquals(Map.of("esbuild", true), yaml.get("allowBuilds"));
        assertFalse(yaml.containsKey("minimumReleaseAge"));
    }

    @Test
    void emptySettingsDoNotCreateFile() throws IOException {

        File file = new File(directory, PnpmWorkspace.FILE_NAME);

        new PnpmWorkspace(null, null, null).write(file);

        assertFalse(file.exists());
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parse(PnpmWorkspace workspace) throws IOException {
        return new YAMLMapper().readValue(workspace.toYaml(), Map.class);
    }
}
