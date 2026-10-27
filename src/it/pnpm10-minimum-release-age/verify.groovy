
File baseDirectory = new File("$basedir")
String buildLog = new File(baseDirectory, "build.log").text

// no dependency version is old enough, so pnpm must refuse to resolve - and thus download - any of them
// pnpm up to 10.20 reports ERR_PNPM_NO_MATCHING_VERSION, later versions ERR_PNPM_NO_MATURE_MATCHING_VERSION
assert buildLog.contains("ERR_PNPM_NO_MATURE_MATCHING_VERSION") || buildLog.contains("ERR_PNPM_NO_MATCHING_VERSION") : "pnpm did not enforce minimumReleaseAge"
assert !new File(baseDirectory, "node_modules/esbuild").exists()
