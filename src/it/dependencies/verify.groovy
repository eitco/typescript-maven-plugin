import groovy.json.JsonSlurper

File baseDirectory = new File("$basedir")

def tgzEntries = { String module ->
    def packages = new File(baseDirectory, "$module/target/dist").listFiles().findAll { it.name.endsWith(".tgz") }
    assert packages.size() == 1 : "expected one packed tgz in $module/target/dist"
    def process = ["tar", "tzf", packages[0].absolutePath].execute()
    def entries = process.text.readLines()
    assert process.waitFor() == 0
    return entries
}

def json = { String path -> new JsonSlurper().parse(new File(baseDirectory, path)) }


// maven dependencies are installed from the tgz file and declared as peer dependencies in the deployed package
def mavenDependencyBuild = json("maven-dependency/package.json")
assert mavenDependencyBuild.dependencies["@eitco/implementation"].startsWith("file:")
def mavenDependencyDeploy = json("maven-dependency/target/dist/package.json")
assert mavenDependencyDeploy.peerDependencies["@eitco/implementation"] == ">=1.0.0-SNAPSHOT"
assert !tgzEntries("maven-dependency").any { it.contains("node_modules") }

// peer dependencies are not bundled
assert json("typescript-peer-dependency/target/dist/package.json").peerDependencies["@eitco/implementation"]
assert !tgzEntries("typescript-peer-dependency").any { it.contains("node_modules") }

// bundled dependencies - including their transitive dependencies - are part of the package
def bundled = tgzEntries("typescript-bundled-dependency")
assert bundled.contains("package/node_modules/@eitco/implementation/package.json") : bundled
assert bundled.contains("package/node_modules/is-odd/package.json") : bundled
assert bundled.contains("package/node_modules/is-number/package.json") : "transitive dependency of a bundled dependency is missing: $bundled"
