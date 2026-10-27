/*
 * Copyright (c) 2026 EITCO GmbH
 * All rights reserved.
 *
 * Created on 27.10.2026
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The settings written to {@code pnpm-workspace.yaml}. Since pnpm 11 these settings are only read from this file.
 * The build allow list is written twice: as {@code allowBuilds}, which is read by pnpm 11 (and later versions of
 * pnpm 10), and as {@code onlyBuiltDependencies}, which is read by pnpm 10 and ignored by pnpm 11.
 * <p>
 * Only the keys listed in {@link #MANAGED_KEYS} are managed - any other setting of an existing file is kept.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"allowBuilds", "onlyBuiltDependencies", "minimumReleaseAge", "minimumReleaseAgeExclude"})
public class PnpmWorkspace {

    public static final String FILE_NAME = "pnpm-workspace.yaml";

    public static final List<String> MANAGED_KEYS = List.of(
            "allowBuilds", "onlyBuiltDependencies", "minimumReleaseAge", "minimumReleaseAgeExclude"
    );

    private static final YAMLMapper MAPPER = YAMLMapper.builder()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .build();

    private final Map<String, Boolean> allowBuilds = new LinkedHashMap<>();
    private final List<String> onlyBuiltDependencies;
    private final Integer minimumReleaseAge;
    private final List<String> minimumReleaseAgeExclude;

    public PnpmWorkspace(List<String> allowBuilds, Integer minimumReleaseAge, List<String> minimumReleaseAgeExclude) {

        if (minimumReleaseAge != null && minimumReleaseAge < 0) {

            throw new IllegalArgumentException("minimumReleaseAge must not be negative, but is " + minimumReleaseAge);
        }

        this.onlyBuiltDependencies = allowBuilds == null ? List.of() : List.copyOf(allowBuilds);
        this.onlyBuiltDependencies.forEach(it -> this.allowBuilds.put(it, true));
        this.minimumReleaseAge = minimumReleaseAge;
        this.minimumReleaseAgeExclude = minimumReleaseAgeExclude == null ? List.of() : List.copyOf(minimumReleaseAgeExclude);
    }

    public Map<String, Boolean> getAllowBuilds() {
        return allowBuilds;
    }

    public List<String> getOnlyBuiltDependencies() {
        return onlyBuiltDependencies;
    }

    public Integer getMinimumReleaseAge() {
        return minimumReleaseAge;
    }

    public List<String> getMinimumReleaseAgeExclude() {
        return minimumReleaseAgeExclude;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return onlyBuiltDependencies.isEmpty() && minimumReleaseAge == null && minimumReleaseAgeExclude.isEmpty();
    }

    public String toYaml() throws IOException {
        return MAPPER.writeValueAsString(this);
    }

    /**
     * Writes these settings to the given file. If the file exists, its managed keys are replaced and every other
     * setting is kept. If there is neither a setting nor an existing file, no file is created.
     */
    public void write(File file) throws IOException {

        if (!file.isFile()) {

            if (!isEmpty()) {

                MAPPER.writeValue(file, this);
            }

            return;
        }

        JsonNode content = file.length() == 0 ? MAPPER.createObjectNode() : MAPPER.readTree(file);

        if (!(content instanceof ObjectNode)) {

            throw new IOException(file + " does not contain a mapping of settings");
        }

        ObjectNode existing = (ObjectNode) content;

        existing.remove(MANAGED_KEYS);
        existing.setAll((ObjectNode) MAPPER.valueToTree(this));

        MAPPER.writeValue(file, existing);
    }
}
