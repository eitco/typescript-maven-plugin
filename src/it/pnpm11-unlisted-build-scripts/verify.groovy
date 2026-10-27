
String buildLog = new File("$basedir", "build.log").text

// pnpm 11 fails if a dependency not listed in allowBuilds has build scripts
assert buildLog.contains("ERR_PNPM_IGNORED_BUILDS") : "pnpm 11 did not fail on unlisted build scripts"
