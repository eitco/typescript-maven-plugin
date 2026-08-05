package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PathMapping {

    private String key;
    private List<String> paths = new ArrayList<>();

    public PathMapping() {
    }

    public PathMapping(String key, List<String> paths) {
        this.key = key;
        this.paths = paths;
    }

    public String getKey() {
        return key;
    }

    public PathMapping setKey(String key) {
        this.key = key;
        return this;
    }

    public List<String> getPaths() {
        return paths;
    }

    public PathMapping setPaths(List<String> paths) {
        this.paths = paths;
        return this;
    }

    /**
     * Converts the configured list of key/value pairs into the object form expected by
     * {@code tsconfig.json}: {@code { "<key>": ["<path>", ...], ... }}. Modelling {@code paths} as a
     * list (instead of a {@code Map}) keeps the keys configurable via Maven XML, where a map key
     * would become an element name and TypeScript path keys ({@code @app/*}, {@code *}, ...) are
     * not valid XML element names.
     */
    public static class ToMapConverter extends StdConverter<List<PathMapping>, Map<String, List<String>>> {
        @Override
        public Map<String, List<String>> convert(List<PathMapping> value) {

            Map<String, List<String>> result = new LinkedHashMap<>();

            value.forEach(mapping -> result.put(mapping.getKey(), mapping.getPaths()));

            return result;
        }
    }
}
