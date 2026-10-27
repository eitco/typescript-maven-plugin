
File baseDirectory = new File("$basedir")
String buildLog = new File(baseDirectory, "build.log").text

// the build allow list is written in the format of pnpm 10 as well as pnpm 11
def workspace = new File(baseDirectory, "pnpm-workspace.yaml")
assert workspace.isFile()
def workspaceYaml = workspace.text
assert workspaceYaml.contains("allowBuilds:") : workspaceYaml
assert workspaceYaml.contains("esbuild: true") : workspaceYaml
assert workspaceYaml.contains("onlyBuiltDependencies:") : workspaceYaml
assert workspaceYaml.contains("minimumReleaseAge: 1440") : workspaceYaml
assert workspaceYaml.contains("@eitco/*") : workspaceYaml

// dependencies were installed by pnpm, not npm
assert new File(baseDirectory, "pnpm-lock.yaml").isFile()
assert !new File(baseDirectory, "package-lock.json").exists()
assert new File(baseDirectory, "node_modules/.pnpm").isDirectory()

// esbuild is allowed to run its install script
assert !buildLog.contains("Ignored build scripts") : "esbuild build scripts were ignored"

def packages = new File(baseDirectory, "target/dist").listFiles().findAll { it.name.endsWith(".tgz") }
assert packages.size() == 1 : "expected one packed tgz in target/dist"

assert buildLog.contains("ignoring legacyPeerDeps since it is not supported by pnpm")
assert !buildLog.contains("--legacy-peer-deps")

// the lock file is attached to the project and installed
File installed = new File(localRepositoryPath, "de/eitco/build/essentials/test/typescript-test-pnpm11/1.0.0-SNAPSHOT/typescript-test-pnpm11-1.0.0-SNAPSHOT.pnpm-lock")
assert installed.isFile() : installed
