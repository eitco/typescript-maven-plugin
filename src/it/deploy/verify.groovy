context.get("registry")?.stop(0)

File baseDirectory = new File("$basedir")
String buildLog = new File(baseDirectory, "build.log").text
List<String> publishRequests = new File(baseDirectory, "registry-requests.log").readLines().findAll { it.startsWith("PUT ") }

String basic = "Basic " + "deployer:s3cr3t-password".bytes.encodeBase64().toString()
String token = "Bearer s3cr3t-token"

def expected = [
        "deploy-npm"   : basic,
        "deploy-pnpm10": token,
        "deploy-pnpm11": basic,
        "deploy-pnpm12": token,
]

expected.each { name, authorization ->

    String request = publishRequests.find { it.startsWith("PUT /registry/$name ") }

    assert request != null : "$name was not published: $publishRequests"
    assert request.endsWith(" $authorization") : "$name was published with wrong credentials: $request"
}

// npm and pnpm were used as configured
assert buildLog.contains("Running 'npm publish")
assert buildLog.contains("Running 'pnpm publish --no-git-checks")

// the credentials are never logged
assert !buildLog.contains("s3cr3t") : "credentials were logged"

// maven deployed the package, the package.json and the lock files as well
File repository = new File(baseDirectory, "target/deploy-repository/de/eitco/build/essentials/test")
expected.keySet().each { name ->
    def files = new File(repository, "$name/1.0.0-SNAPSHOT").list().toList()
    assert files.any { it.endsWith(".tgz") } : "$name: $files"
    assert files.any { it.endsWith(".package") } : "$name: $files"
    assert files.any { it.endsWith(name == "deploy-npm" ? ".package-lock" : ".pnpm-lock") } : "$name: $files"
}

return true
