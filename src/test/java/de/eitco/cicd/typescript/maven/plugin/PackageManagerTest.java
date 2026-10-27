package de.eitco.cicd.typescript.maven.plugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageManagerTest {

    @Test
    void installGoals() {

        assertEquals("install-node-and-npm", PackageManager.NPM.installGoal());
        assertEquals("install-node-and-pnpm", PackageManager.PNPM.installGoal());
    }

    @Test
    void lockFiles() {

        assertEquals("package-lock.json", PackageManager.NPM.lockFileName());
        assertEquals("package-lock", PackageManager.NPM.lockFileArtifactType());
        assertEquals("pnpm-lock.yaml", PackageManager.PNPM.lockFileName());
        assertEquals("pnpm-lock", PackageManager.PNPM.lockFileArtifactType());
    }

    @Test
    void npmInstallArguments() {

        assertEquals("install ", PackageManager.NPM.installArguments(false, false));
        assertEquals("install --force --legacy-peer-deps ", PackageManager.NPM.installArguments(true, true));
    }

    @Test
    void pnpmInstallDropsLegacyPeerDeps() {

        assertEquals("install ", PackageManager.PNPM.installArguments(false, true));
        assertEquals("install --force ", PackageManager.PNPM.installArguments(true, true));
    }

    @Test
    void pnpmPublishSkipsGitChecks() {

        assertEquals("publish ", PackageManager.NPM.publishArguments());
        assertEquals("publish --no-git-checks ", PackageManager.PNPM.publishArguments());
    }

}
