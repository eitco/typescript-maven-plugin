package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AngularCompilerOptions {

    private String annotationsAs;
    private Boolean annotateForClosureCompiler;

    private CompilationMode compilationMode;

    private Boolean disableExpressionLowering;

    private Boolean disableTypeScriptVersionCheck;

    private Boolean enableI18nLegacyMessageIdFormat;

    private Boolean enableResourceInlining;

    private Boolean enableLegacyTemplate;

    private String flatModuleId;

    private Boolean flatModuleOutFile;

    private Boolean fullTemplateTypeCheck;

    private Boolean generateCodeForLibraries;

    private Boolean preserveWhitespaces;

    private Boolean skipMetadataEmit;

    private Boolean skipTemplateCodegen;

    private Boolean strictMetadataEmit;

    private Boolean strictInjectionParameters;

    private Boolean strictTemplates;

    private Boolean trace;


    public String getAnnotationsAs() {
        return annotationsAs;
    }

    public Boolean getAnnotateForClosureCompiler() {
        return annotateForClosureCompiler;
    }

    public CompilationMode getCompilationMode() {
        return compilationMode;
    }

    public Boolean getDisableExpressionLowering() {
        return disableExpressionLowering;
    }

    public Boolean getDisableTypeScriptVersionCheck() {
        return disableTypeScriptVersionCheck;
    }

    public Boolean getEnableI18nLegacyMessageIdFormat() {
        return enableI18nLegacyMessageIdFormat;
    }

    public Boolean getEnableResourceInlining() {
        return enableResourceInlining;
    }

    public Boolean getEnableLegacyTemplate() {
        return enableLegacyTemplate;
    }

    public String getFlatModuleId() {
        return flatModuleId;
    }

    public Boolean getFlatModuleOutFile() {
        return flatModuleOutFile;
    }

    public Boolean getFullTemplateTypeCheck() {
        return fullTemplateTypeCheck;
    }

    public Boolean getGenerateCodeForLibraries() {
        return generateCodeForLibraries;
    }

    public Boolean getPreserveWhitespaces() {
        return preserveWhitespaces;
    }

    public Boolean getSkipMetadataEmit() {
        return skipMetadataEmit;
    }

    public Boolean getSkipTemplateCodegen() {
        return skipTemplateCodegen;
    }

    public Boolean getStrictMetadataEmit() {
        return strictMetadataEmit;
    }

    public Boolean getStrictInjectionParameters() {
        return strictInjectionParameters;
    }

    public Boolean getStrictTemplates() {
        return strictTemplates;
    }

    public Boolean getTrace() {
        return trace;
    }

    public void setAnnotationsAs(String annotationsAs) {
        this.annotationsAs = annotationsAs;
    }

    public void setAnnotateForClosureCompiler(Boolean annotateForClosureCompiler) {
        this.annotateForClosureCompiler = annotateForClosureCompiler;
    }

    public void setCompilationMode(CompilationMode compilationMode) {
        this.compilationMode = compilationMode;
    }

    public void setDisableExpressionLowering(Boolean disableExpressionLowering) {
        this.disableExpressionLowering = disableExpressionLowering;
    }

    public void setDisableTypeScriptVersionCheck(Boolean disableTypeScriptVersionCheck) {
        this.disableTypeScriptVersionCheck = disableTypeScriptVersionCheck;
    }

    public void setEnableI18nLegacyMessageIdFormat(Boolean enableI18nLegacyMessageIdFormat) {
        this.enableI18nLegacyMessageIdFormat = enableI18nLegacyMessageIdFormat;
    }

    public void setEnableResourceInlining(Boolean enableResourceInlining) {
        this.enableResourceInlining = enableResourceInlining;
    }

    public void setEnableLegacyTemplate(Boolean enableLegacyTemplate) {
        this.enableLegacyTemplate = enableLegacyTemplate;
    }

    public void setFlatModuleId(String flatModuleId) {
        this.flatModuleId = flatModuleId;
    }

    public void setFlatModuleOutFile(Boolean flatModuleOutFile) {
        this.flatModuleOutFile = flatModuleOutFile;
    }

    public void setFullTemplateTypeCheck(Boolean fullTemplateTypeCheck) {
        this.fullTemplateTypeCheck = fullTemplateTypeCheck;
    }

    public void setGenerateCodeForLibraries(Boolean generateCodeForLibraries) {
        this.generateCodeForLibraries = generateCodeForLibraries;
    }

    public void setPreserveWhitespaces(Boolean preserveWhitespaces) {
        this.preserveWhitespaces = preserveWhitespaces;
    }

    public void setSkipMetadataEmit(Boolean skipMetadataEmit) {
        this.skipMetadataEmit = skipMetadataEmit;
    }

    public void setSkipTemplateCodegen(Boolean skipTemplateCodegen) {
        this.skipTemplateCodegen = skipTemplateCodegen;
    }

    public void setStrictMetadataEmit(Boolean strictMetadataEmit) {
        this.strictMetadataEmit = strictMetadataEmit;
    }

    public void setStrictInjectionParameters(Boolean strictInjectionParameters) {
        this.strictInjectionParameters = strictInjectionParameters;
    }

    public void setStrictTemplates(Boolean strictTemplates) {
        this.strictTemplates = strictTemplates;
    }

    public void setTrace(Boolean trace) {
        this.trace = trace;
    }
}
