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
