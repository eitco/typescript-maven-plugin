package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(converter = PathList.Converter.class)
public class PathList {

    private List<String> elements = new ArrayList<>();

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public static class Converter extends StdConverter<PathList, List<String>> {
        @Override
        public List<String> convert(PathList pathList) {
            return pathList.elements;
        }
    }
}
