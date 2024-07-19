/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 04.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CompilerOptions {

    private Boolean allowUnreachableCode;
    private Boolean allowUnusedLabels;
    private Boolean alwaysStrict;
    private Boolean exactOptionalPropertyTypes;
    private Boolean noFallthroughCasesInSwitch;
    private Boolean noImplicitAny;
    private Boolean noImplicitOverride;
    private Boolean noImplicitReturns;
    private Boolean noImplicitThis;
    private Boolean noPropertyAccessFromIndexSignature;
    private Boolean noUncheckedIndexedAccess;
    private Boolean noUnusedLocals;
    private Boolean noUnusedParameters;
    private Boolean strict;
    private Boolean strictBindCallApply;
    private Boolean strictFunctionTypes;
    private Boolean strictNullChecks;
    private Boolean strictPropertyInitialization;
    private Boolean useUnknownInCatchVariables;
    private Boolean allowUmdGlobalAccess;
    private String baseUrl;
    private String module;
    private String moduleResolution;
    private Boolean noResolve;
    private Map<String, PathList> paths;
    private Boolean resolveJsonModule;
    private String rootDir;
    private List<String> rootDirs;
    private List<String> typeRoots;
    private List<String> types;
    private Boolean declaration;
    private String declarationDir;
    private Boolean declarationMap;
    private Boolean downlevelIteration;
    private Boolean emitBOM;
    private Boolean emitDeclarationOnly;
    private Boolean importHelpers;
    private String importsNotUsedAsValues;
    private Boolean inlineSourceMap;
    private Boolean inlineSources;
    private String mapRoot;
    private String newLine;
    private Boolean noEmit;
    private Boolean noEmitHelpers;
    private Boolean noEmitOnError;
    private String outDir;
    private String outFile;
    private Boolean preserveConstEnums;
    private Boolean preserveValueImports;
    private Boolean removeComments;
    private Boolean sourceMap;
    private String sourceRoot;
    private Boolean stripInternal;
    private Boolean allowJs;
    private Boolean checkJs;
    private Integer maxNodeModuleJsDepth;
    private Boolean disableSizeLimit;
    private Boolean allowSyntheticDefaultImports;
    private Boolean esModuleInterop;
    private Boolean forceConsistentCasingInFileNames;
    private Boolean isolatedModules;
    private Boolean preserveSymlinks;
    private Boolean keyofStringsOnly;
    private Boolean noImplicitUseStrict;
    private Boolean noStrictGenericChecks;
    private Boolean suppressExcessPropertyErrors;
    private Boolean suppressImplicitAnyIndexErrors;
    private Boolean emitDecoratorMetadata;
    private Boolean experimentalDecorators;
    private String jsx;
    private String jsxFactory;
    private String jsxFragmentFactory;
    private String jsxImportSource;
    private List<String> lib;
    private Boolean noLib;
    private String reactNamespace;
    private String target;
    private Boolean useDefineForClassFields;
    private Boolean diagnostics;
    private Boolean explainFiles;
    private Boolean extendedDiagnostics;
    private Boolean listEmittedFiles;
    private Boolean listFiles;
    private Boolean traceResolution;
    private Boolean composite;
    private Boolean disableReferencedProjectLoad;
    private Boolean disableSolutionSearching;
    private Boolean disableSourceOfProjectReferenceRedirect;
    private Boolean incremental;
    private String tsBuildInfoFile;
    private Boolean noErrorTruncation;
    private Boolean preserveWatchOutput;
    private Boolean pretty;
    private Boolean skipDefaultLibCheck;
    private Boolean skipLibCheck;

    public Boolean getAllowUnreachableCode() {
        return allowUnreachableCode;
    }

    public CompilerOptions setAllowUnreachableCode(Boolean allowUnreachableCode) {
        this.allowUnreachableCode = allowUnreachableCode;
        return this;
    }

    public Boolean getAllowUnusedLabels() {
        return allowUnusedLabels;
    }

    public CompilerOptions setAllowUnusedLabels(Boolean allowUnusedLabels) {
        this.allowUnusedLabels = allowUnusedLabels;
        return this;
    }

    public Boolean getAlwaysStrict() {
        return alwaysStrict;
    }

    public CompilerOptions setAlwaysStrict(Boolean alwaysStrict) {
        this.alwaysStrict = alwaysStrict;
        return this;
    }

    public Boolean getExactOptionalPropertyTypes() {
        return exactOptionalPropertyTypes;
    }

    public CompilerOptions setExactOptionalPropertyTypes(Boolean exactOptionalPropertyTypes) {
        this.exactOptionalPropertyTypes = exactOptionalPropertyTypes;
        return this;
    }

    public Boolean getNoFallthroughCasesInSwitch() {
        return noFallthroughCasesInSwitch;
    }

    public CompilerOptions setNoFallthroughCasesInSwitch(Boolean noFallthroughCasesInSwitch) {
        this.noFallthroughCasesInSwitch = noFallthroughCasesInSwitch;
        return this;
    }

    public Boolean getNoImplicitAny() {
        return noImplicitAny;
    }

    public CompilerOptions setNoImplicitAny(Boolean noImplicitAny) {
        this.noImplicitAny = noImplicitAny;
        return this;
    }

    public Boolean getNoImplicitOverride() {
        return noImplicitOverride;
    }

    public CompilerOptions setNoImplicitOverride(Boolean noImplicitOverride) {
        this.noImplicitOverride = noImplicitOverride;
        return this;
    }

    public Boolean getNoImplicitReturns() {
        return noImplicitReturns;
    }

    public CompilerOptions setNoImplicitReturns(Boolean noImplicitReturns) {
        this.noImplicitReturns = noImplicitReturns;
        return this;
    }

    public Boolean getNoImplicitThis() {
        return noImplicitThis;
    }

    public CompilerOptions setNoImplicitThis(Boolean noImplicitThis) {
        this.noImplicitThis = noImplicitThis;
        return this;
    }

    public Boolean getNoPropertyAccessFromIndexSignature() {
        return noPropertyAccessFromIndexSignature;
    }

    public CompilerOptions setNoPropertyAccessFromIndexSignature(Boolean noPropertyAccessFromIndexSignature) {
        this.noPropertyAccessFromIndexSignature = noPropertyAccessFromIndexSignature;
        return this;
    }

    public Boolean getNoUncheckedIndexedAccess() {
        return noUncheckedIndexedAccess;
    }

    public CompilerOptions setNoUncheckedIndexedAccess(Boolean noUncheckedIndexedAccess) {
        this.noUncheckedIndexedAccess = noUncheckedIndexedAccess;
        return this;
    }

    public Boolean getNoUnusedLocals() {
        return noUnusedLocals;
    }

    public CompilerOptions setNoUnusedLocals(Boolean noUnusedLocals) {
        this.noUnusedLocals = noUnusedLocals;
        return this;
    }

    public Boolean getNoUnusedParameters() {
        return noUnusedParameters;
    }

    public CompilerOptions setNoUnusedParameters(Boolean noUnusedParameters) {
        this.noUnusedParameters = noUnusedParameters;
        return this;
    }

    public Boolean getStrict() {
        return strict;
    }

    public CompilerOptions setStrict(Boolean strict) {
        this.strict = strict;
        return this;
    }

    public Boolean getStrictBindCallApply() {
        return strictBindCallApply;
    }

    public CompilerOptions setStrictBindCallApply(Boolean strictBindCallApply) {
        this.strictBindCallApply = strictBindCallApply;
        return this;
    }

    public Boolean getStrictFunctionTypes() {
        return strictFunctionTypes;
    }

    public CompilerOptions setStrictFunctionTypes(Boolean strictFunctionTypes) {
        this.strictFunctionTypes = strictFunctionTypes;
        return this;
    }

    public Boolean getStrictNullChecks() {
        return strictNullChecks;
    }

    public CompilerOptions setStrictNullChecks(Boolean strictNullChecks) {
        this.strictNullChecks = strictNullChecks;
        return this;
    }

    public Boolean getStrictPropertyInitialization() {
        return strictPropertyInitialization;
    }

    public CompilerOptions setStrictPropertyInitialization(Boolean strictPropertyInitialization) {
        this.strictPropertyInitialization = strictPropertyInitialization;
        return this;
    }

    public Boolean getUseUnknownInCatchVariables() {
        return useUnknownInCatchVariables;
    }

    public CompilerOptions setUseUnknownInCatchVariables(Boolean useUnknownInCatchVariables) {
        this.useUnknownInCatchVariables = useUnknownInCatchVariables;
        return this;
    }

    public Boolean getAllowUmdGlobalAccess() {
        return allowUmdGlobalAccess;
    }

    public CompilerOptions setAllowUmdGlobalAccess(Boolean allowUmdGlobalAccess) {
        this.allowUmdGlobalAccess = allowUmdGlobalAccess;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public CompilerOptions setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getModule() {
        return module;
    }

    public CompilerOptions setModule(String module) {
        this.module = module;
        return this;
    }

    public String getModuleResolution() {
        return moduleResolution;
    }

    public CompilerOptions setModuleResolution(String moduleResolution) {
        this.moduleResolution = moduleResolution;
        return this;
    }

    public Boolean getNoResolve() {
        return noResolve;
    }

    public CompilerOptions setNoResolve(Boolean noResolve) {
        this.noResolve = noResolve;
        return this;
    }

    public Map<String, PathList> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, PathList> paths) {
        this.paths = paths;
    }

    public Boolean getResolveJsonModule() {
        return resolveJsonModule;
    }

    public CompilerOptions setResolveJsonModule(Boolean resolveJsonModule) {
        this.resolveJsonModule = resolveJsonModule;
        return this;
    }

    public String getRootDir() {
        return rootDir;
    }

    public CompilerOptions setRootDir(String rootDir) {
        this.rootDir = rootDir;
        return this;
    }

    public List<String> getRootDirs() {
        return rootDirs;
    }

    public CompilerOptions setRootDirs(List<String> rootDirs) {
        this.rootDirs = rootDirs;
        return this;
    }

    public List<String> getTypeRoots() {
        return typeRoots;
    }

    public CompilerOptions setTypeRoots(List<String> typeRoots) {
        this.typeRoots = typeRoots;
        return this;
    }

    public List<String> getTypes() {
        return types;
    }

    public CompilerOptions setTypes(List<String> types) {
        this.types = types;
        return this;
    }

    public Boolean getDeclaration() {
        return declaration;
    }

    public CompilerOptions setDeclaration(Boolean declaration) {
        this.declaration = declaration;
        return this;
    }

    public String getDeclarationDir() {
        return declarationDir;
    }

    public CompilerOptions setDeclarationDir(String declarationDir) {
        this.declarationDir = declarationDir;
        return this;
    }

    public Boolean getDeclarationMap() {
        return declarationMap;
    }

    public CompilerOptions setDeclarationMap(Boolean declarationMap) {
        this.declarationMap = declarationMap;
        return this;
    }

    public Boolean getDownlevelIteration() {
        return downlevelIteration;
    }

    public CompilerOptions setDownlevelIteration(Boolean downlevelIteration) {
        this.downlevelIteration = downlevelIteration;
        return this;
    }

    public Boolean getEmitBOM() {
        return emitBOM;
    }

    public CompilerOptions setEmitBOM(Boolean emitBOM) {
        this.emitBOM = emitBOM;
        return this;
    }

    public Boolean getEmitDeclarationOnly() {
        return emitDeclarationOnly;
    }

    public CompilerOptions setEmitDeclarationOnly(Boolean emitDeclarationOnly) {
        this.emitDeclarationOnly = emitDeclarationOnly;
        return this;
    }

    public Boolean getImportHelpers() {
        return importHelpers;
    }

    public CompilerOptions setImportHelpers(Boolean importHelpers) {
        this.importHelpers = importHelpers;
        return this;
    }

    public String getImportsNotUsedAsValues() {
        return importsNotUsedAsValues;
    }

    public CompilerOptions setImportsNotUsedAsValues(String importsNotUsedAsValues) {
        this.importsNotUsedAsValues = importsNotUsedAsValues;
        return this;
    }

    public Boolean getInlineSourceMap() {
        return inlineSourceMap;
    }

    public CompilerOptions setInlineSourceMap(Boolean inlineSourceMap) {
        this.inlineSourceMap = inlineSourceMap;
        return this;
    }

    public Boolean getInlineSources() {
        return inlineSources;
    }

    public CompilerOptions setInlineSources(Boolean inlineSources) {
        this.inlineSources = inlineSources;
        return this;
    }

    public String getMapRoot() {
        return mapRoot;
    }

    public CompilerOptions setMapRoot(String mapRoot) {
        this.mapRoot = mapRoot;
        return this;
    }

    public String getNewLine() {
        return newLine;
    }

    public CompilerOptions setNewLine(String newLine) {
        this.newLine = newLine;
        return this;
    }

    public Boolean getNoEmit() {
        return noEmit;
    }

    public CompilerOptions setNoEmit(Boolean noEmit) {
        this.noEmit = noEmit;
        return this;
    }

    public Boolean getNoEmitHelpers() {
        return noEmitHelpers;
    }

    public CompilerOptions setNoEmitHelpers(Boolean noEmitHelpers) {
        this.noEmitHelpers = noEmitHelpers;
        return this;
    }

    public Boolean getNoEmitOnError() {
        return noEmitOnError;
    }

    public CompilerOptions setNoEmitOnError(Boolean noEmitOnError) {
        this.noEmitOnError = noEmitOnError;
        return this;
    }

    public String getOutDir() {
        return outDir;
    }

    public CompilerOptions setOutDir(String outDir) {
        this.outDir = outDir;
        return this;
    }

    public String getOutFile() {
        return outFile;
    }

    public CompilerOptions setOutFile(String outFile) {
        this.outFile = outFile;
        return this;
    }

    public Boolean getPreserveConstEnums() {
        return preserveConstEnums;
    }

    public CompilerOptions setPreserveConstEnums(Boolean preserveConstEnums) {
        this.preserveConstEnums = preserveConstEnums;
        return this;
    }

    public Boolean getPreserveValueImports() {
        return preserveValueImports;
    }

    public CompilerOptions setPreserveValueImports(Boolean preserveValueImports) {
        this.preserveValueImports = preserveValueImports;
        return this;
    }

    public Boolean getRemoveComments() {
        return removeComments;
    }

    public CompilerOptions setRemoveComments(Boolean removeComments) {
        this.removeComments = removeComments;
        return this;
    }

    public Boolean getSourceMap() {
        return sourceMap;
    }

    public CompilerOptions setSourceMap(Boolean sourceMap) {
        this.sourceMap = sourceMap;
        return this;
    }

    public String getSourceRoot() {
        return sourceRoot;
    }

    public CompilerOptions setSourceRoot(String sourceRoot) {
        this.sourceRoot = sourceRoot;
        return this;
    }

    public Boolean getStripInternal() {
        return stripInternal;
    }

    public CompilerOptions setStripInternal(Boolean stripInternal) {
        this.stripInternal = stripInternal;
        return this;
    }

    public Boolean getAllowJs() {
        return allowJs;
    }

    public CompilerOptions setAllowJs(Boolean allowJs) {
        this.allowJs = allowJs;
        return this;
    }

    public Boolean getCheckJs() {
        return checkJs;
    }

    public CompilerOptions setCheckJs(Boolean checkJs) {
        this.checkJs = checkJs;
        return this;
    }

    public Integer getMaxNodeModuleJsDepth() {
        return maxNodeModuleJsDepth;
    }

    public CompilerOptions setMaxNodeModuleJsDepth(Integer maxNodeModuleJsDepth) {
        this.maxNodeModuleJsDepth = maxNodeModuleJsDepth;
        return this;
    }

    public Boolean getDisableSizeLimit() {
        return disableSizeLimit;
    }

    public CompilerOptions setDisableSizeLimit(Boolean disableSizeLimit) {
        this.disableSizeLimit = disableSizeLimit;
        return this;
    }

    public Boolean getAllowSyntheticDefaultImports() {
        return allowSyntheticDefaultImports;
    }

    public CompilerOptions setAllowSyntheticDefaultImports(Boolean allowSyntheticDefaultImports) {
        this.allowSyntheticDefaultImports = allowSyntheticDefaultImports;
        return this;
    }

    public Boolean getEsModuleInterop() {
        return esModuleInterop;
    }

    public CompilerOptions setEsModuleInterop(Boolean esModuleInterop) {
        this.esModuleInterop = esModuleInterop;
        return this;
    }

    public Boolean getForceConsistentCasingInFileNames() {
        return forceConsistentCasingInFileNames;
    }

    public CompilerOptions setForceConsistentCasingInFileNames(Boolean forceConsistentCasingInFileNames) {
        this.forceConsistentCasingInFileNames = forceConsistentCasingInFileNames;
        return this;
    }

    public Boolean getIsolatedModules() {
        return isolatedModules;
    }

    public CompilerOptions setIsolatedModules(Boolean isolatedModules) {
        this.isolatedModules = isolatedModules;
        return this;
    }

    public Boolean getPreserveSymlinks() {
        return preserveSymlinks;
    }

    public CompilerOptions setPreserveSymlinks(Boolean preserveSymlinks) {
        this.preserveSymlinks = preserveSymlinks;
        return this;
    }

    public Boolean getKeyofStringsOnly() {
        return keyofStringsOnly;
    }

    public CompilerOptions setKeyofStringsOnly(Boolean keyofStringsOnly) {
        this.keyofStringsOnly = keyofStringsOnly;
        return this;
    }

    public Boolean getNoImplicitUseStrict() {
        return noImplicitUseStrict;
    }

    public CompilerOptions setNoImplicitUseStrict(Boolean noImplicitUseStrict) {
        this.noImplicitUseStrict = noImplicitUseStrict;
        return this;
    }

    public Boolean getNoStrictGenericChecks() {
        return noStrictGenericChecks;
    }

    public CompilerOptions setNoStrictGenericChecks(Boolean noStrictGenericChecks) {
        this.noStrictGenericChecks = noStrictGenericChecks;
        return this;
    }

    public Boolean getSuppressExcessPropertyErrors() {
        return suppressExcessPropertyErrors;
    }

    public CompilerOptions setSuppressExcessPropertyErrors(Boolean suppressExcessPropertyErrors) {
        this.suppressExcessPropertyErrors = suppressExcessPropertyErrors;
        return this;
    }

    public Boolean getSuppressImplicitAnyIndexErrors() {
        return suppressImplicitAnyIndexErrors;
    }

    public CompilerOptions setSuppressImplicitAnyIndexErrors(Boolean suppressImplicitAnyIndexErrors) {
        this.suppressImplicitAnyIndexErrors = suppressImplicitAnyIndexErrors;
        return this;
    }

    public Boolean getEmitDecoratorMetadata() {
        return emitDecoratorMetadata;
    }

    public CompilerOptions setEmitDecoratorMetadata(Boolean emitDecoratorMetadata) {
        this.emitDecoratorMetadata = emitDecoratorMetadata;
        return this;
    }

    public Boolean getExperimentalDecorators() {
        return experimentalDecorators;
    }

    public CompilerOptions setExperimentalDecorators(Boolean experimentalDecorators) {
        this.experimentalDecorators = experimentalDecorators;
        return this;
    }

    public String getJsx() {
        return jsx;
    }

    public CompilerOptions setJsx(String jsx) {
        this.jsx = jsx;
        return this;
    }

    public String getJsxFactory() {
        return jsxFactory;
    }

    public CompilerOptions setJsxFactory(String jsxFactory) {
        this.jsxFactory = jsxFactory;
        return this;
    }

    public String getJsxFragmentFactory() {
        return jsxFragmentFactory;
    }

    public CompilerOptions setJsxFragmentFactory(String jsxFragmentFactory) {
        this.jsxFragmentFactory = jsxFragmentFactory;
        return this;
    }

    public String getJsxImportSource() {
        return jsxImportSource;
    }

    public CompilerOptions setJsxImportSource(String jsxImportSource) {
        this.jsxImportSource = jsxImportSource;
        return this;
    }

    public List<String> getLib() {
        return lib;
    }

    public CompilerOptions setLib(List<String> lib) {
        this.lib = lib;
        return this;
    }

    public Boolean getNoLib() {
        return noLib;
    }

    public CompilerOptions setNoLib(Boolean noLib) {
        this.noLib = noLib;
        return this;
    }

    public String getReactNamespace() {
        return reactNamespace;
    }

    public CompilerOptions setReactNamespace(String reactNamespace) {
        this.reactNamespace = reactNamespace;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public CompilerOptions setTarget(String target) {
        this.target = target;
        return this;
    }

    public Boolean getUseDefineForClassFields() {
        return useDefineForClassFields;
    }

    public CompilerOptions setUseDefineForClassFields(Boolean useDefineForClassFields) {
        this.useDefineForClassFields = useDefineForClassFields;
        return this;
    }

    public Boolean getDiagnostics() {
        return diagnostics;
    }

    public CompilerOptions setDiagnostics(Boolean diagnostics) {
        this.diagnostics = diagnostics;
        return this;
    }

    public Boolean getExplainFiles() {
        return explainFiles;
    }

    public CompilerOptions setExplainFiles(Boolean explainFiles) {
        this.explainFiles = explainFiles;
        return this;
    }

    public Boolean getExtendedDiagnostics() {
        return extendedDiagnostics;
    }

    public CompilerOptions setExtendedDiagnostics(Boolean extendedDiagnostics) {
        this.extendedDiagnostics = extendedDiagnostics;
        return this;
    }

    public Boolean getListEmittedFiles() {
        return listEmittedFiles;
    }

    public CompilerOptions setListEmittedFiles(Boolean listEmittedFiles) {
        this.listEmittedFiles = listEmittedFiles;
        return this;
    }

    public Boolean getListFiles() {
        return listFiles;
    }

    public CompilerOptions setListFiles(Boolean listFiles) {
        this.listFiles = listFiles;
        return this;
    }

    public Boolean getTraceResolution() {
        return traceResolution;
    }

    public CompilerOptions setTraceResolution(Boolean traceResolution) {
        this.traceResolution = traceResolution;
        return this;
    }

    public Boolean getComposite() {
        return composite;
    }

    public CompilerOptions setComposite(Boolean composite) {
        this.composite = composite;
        return this;
    }

    public Boolean getDisableReferencedProjectLoad() {
        return disableReferencedProjectLoad;
    }

    public CompilerOptions setDisableReferencedProjectLoad(Boolean disableReferencedProjectLoad) {
        this.disableReferencedProjectLoad = disableReferencedProjectLoad;
        return this;
    }

    public Boolean getDisableSolutionSearching() {
        return disableSolutionSearching;
    }

    public CompilerOptions setDisableSolutionSearching(Boolean disableSolutionSearching) {
        this.disableSolutionSearching = disableSolutionSearching;
        return this;
    }

    public Boolean getDisableSourceOfProjectReferenceRedirect() {
        return disableSourceOfProjectReferenceRedirect;
    }

    public CompilerOptions setDisableSourceOfProjectReferenceRedirect(Boolean disableSourceOfProjectReferenceRedirect) {
        this.disableSourceOfProjectReferenceRedirect = disableSourceOfProjectReferenceRedirect;
        return this;
    }

    public Boolean getIncremental() {
        return incremental;
    }

    public CompilerOptions setIncremental(Boolean incremental) {
        this.incremental = incremental;
        return this;
    }

    public String getTsBuildInfoFile() {
        return tsBuildInfoFile;
    }

    public CompilerOptions setTsBuildInfoFile(String tsBuildInfoFile) {
        this.tsBuildInfoFile = tsBuildInfoFile;
        return this;
    }

    public Boolean getNoErrorTruncation() {
        return noErrorTruncation;
    }

    public CompilerOptions setNoErrorTruncation(Boolean noErrorTruncation) {
        this.noErrorTruncation = noErrorTruncation;
        return this;
    }

    public Boolean getPreserveWatchOutput() {
        return preserveWatchOutput;
    }

    public CompilerOptions setPreserveWatchOutput(Boolean preserveWatchOutput) {
        this.preserveWatchOutput = preserveWatchOutput;
        return this;
    }

    public Boolean getPretty() {
        return pretty;
    }

    public CompilerOptions setPretty(Boolean pretty) {
        this.pretty = pretty;
        return this;
    }

    public Boolean getSkipDefaultLibCheck() {
        return skipDefaultLibCheck;
    }

    public CompilerOptions setSkipDefaultLibCheck(Boolean skipDefaultLibCheck) {
        this.skipDefaultLibCheck = skipDefaultLibCheck;
        return this;
    }

    public Boolean getSkipLibCheck() {
        return skipLibCheck;
    }

    public CompilerOptions setSkipLibCheck(Boolean skipLibCheck) {
        this.skipLibCheck = skipLibCheck;
        return this;
    }
}
