
File baseDirectory = new File("$basedir")
String buildLog = new File(baseDirectory, "build.log").text

assert buildLog.contains("installing node and pnpm with frontend-maven-plugin:2.0.1:install-node-and-pnpm")
assert buildLog.contains("Installing pnpm version 10.34.9")
assert new File(baseDirectory, "pnpm-lock.yaml").isFile()
