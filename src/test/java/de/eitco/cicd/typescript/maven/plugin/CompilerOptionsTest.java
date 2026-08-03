package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompilerOptionsTest {

    private final ObjectMapper mapper = NpmDependencyMetaDataReader.makeObjectMapper();

    @Test
    void serializesNewOptions() throws Exception {

        CompilerOptions options = new CompilerOptions()
                .setVerbatimModuleSyntax(true)
                .setModuleDetection("force")
                .setCustomConditions(List.of("my-condition"))
                .setNoCheck(true)
                .setIsolatedDeclarations(true)
                .setRewriteRelativeImportExtensions(true);

        JsonNode json = mapper.readTree(mapper.writeValueAsString(options));

        assertTrue(json.get("verbatimModuleSyntax").asBoolean());
        assertEquals("force", json.get("moduleDetection").asText());
        assertEquals("my-condition", json.get("customConditions").get(0).asText());
        assertTrue(json.get("noCheck").asBoolean());
        assertTrue(json.get("isolatedDeclarations").asBoolean());
        assertTrue(json.get("rewriteRelativeImportExtensions").asBoolean());
    }

    @Test
    void omitsUnsetOptions() throws Exception {

        JsonNode json = mapper.readTree(mapper.writeValueAsString(new CompilerOptions()));

        assertFalse(json.has("verbatimModuleSyntax"));
        assertFalse(json.has("moduleDetection"));
        assertFalse(json.has("customConditions"));
        assertFalse(json.has("strictBuiltinIteratorReturn"));
        assertFalse(json.has("allowImportingTsExtensions"));
    }

    @Test
    void additionalOptionsAreFlattenedAndTyped() throws Exception {

        LinkedHashMap<String, String> extra = new LinkedHashMap<>();
        extra.put("futureFlag", "true");
        extra.put("futureName", "hello");
        extra.put("futureList", "[\"es2020\"]");

        CompilerOptions options = new CompilerOptions().setAdditionalOptions(extra);

        JsonNode json = mapper.readTree(mapper.writeValueAsString(options));

        // flattened onto compilerOptions, not wrapped
        assertFalse(json.has("additionalOptions"));
        // values parsed into their proper JSON types
        assertTrue(json.get("futureFlag").isBoolean());
        assertTrue(json.get("futureFlag").asBoolean());
        assertEquals("hello", json.get("futureName").asText());
        assertTrue(json.get("futureList").isArray());
        assertEquals("es2020", json.get("futureList").get(0).asText());
    }
}
