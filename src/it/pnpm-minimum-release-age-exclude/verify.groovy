
File baseDirectory = new File("$basedir")

// no version is old enough, but every dependency is excluded from the minimum release age
assert new File(baseDirectory, "pnpm-workspace.yaml").text.contains("minimumReleaseAge: 52560000")
assert new File(baseDirectory, "node_modules/esbuild/package.json").isFile()
assert new File(baseDirectory, "node_modules/typescript/package.json").isFile()
