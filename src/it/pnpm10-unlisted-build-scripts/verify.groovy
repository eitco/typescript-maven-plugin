
String buildLog = new File("$basedir", "build.log").text

// pnpm 10 only warns if a dependency not listed in allowBuilds has build scripts - and does not execute them
assert buildLog.contains("Ignored build scripts: esbuild") : "pnpm 10 did not ignore unlisted build scripts"
assert !buildLog.contains("esbuild postinstall")
