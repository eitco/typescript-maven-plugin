import java.nio.charset.StandardCharsets
import java.nio.file.Files

//File log = new File(basedir, "build.log");

def checkFile(File file) {

    assert file.isFile()
}

checkFile(new File(basedir, "tsconfig.json"));
checkFile(new File(basedir, "tsconfig.spec.json"));
checkFile(new File(basedir, "package.json"));
checkFile(new File(basedir, "target/dist/package.json"));

def packaje = new File(basedir, "package.json");

def packageJson = Files.readString(packaje.toPath())

assert packageJson.contains("\"socket.io\" : \"4.7.4\"") : packageJson
assert packageJson.contains("\"@angular/core\" : \"17.1.2\"") : packageJson
assert packageJson.contains("\"wham\" : \"0.2.4\"") : packageJson

// modern package.json fields provided via defaultValues must be merged into the generated package.json
assert packageJson.contains("\"type\" : \"module\"") : packageJson
assert packageJson.contains("\"node\" : \">=18\"") : packageJson
assert packageJson.contains("https://github.com/eitco/example.git") : packageJson
assert packageJson.contains("\"exports\"") : packageJson

// unknown/unmodelled properties from defaultValues must be carried into the generated package.json
assert packageJson.contains("\"customField\" : \"customValue\"") : packageJson
