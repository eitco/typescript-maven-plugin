
File baseDirectory = new File("$basedir")
String buildLog = new File(baseDirectory, "build.log").text

assert buildLog.contains("install-node-and-package-manager")
assert !buildLog.contains("Installing node version")
assert !new File(baseDirectory, "node").exists()
