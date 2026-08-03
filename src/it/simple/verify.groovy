
File baseDirectory = new File("$basedir");

assert new File(baseDirectory, "tsconfig.json").isFile()
assert new File(baseDirectory, "tsconfig.spec.json").isFile()
assert new File(baseDirectory, "package.json").isFile()
assert new File(baseDirectory, "target/dist/package.json").isFile()

// modern compilerOptions configured in the pom must reach the generated tsconfig.json
def tsconfig = new File(baseDirectory, "tsconfig.json").text
assert tsconfig.contains("\"noUncheckedSideEffectImports\" : true") : tsconfig

