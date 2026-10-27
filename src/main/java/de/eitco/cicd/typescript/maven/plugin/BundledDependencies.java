/*
 * Copyright (c) 2026 EITCO GmbH
 * All rights reserved.
 *
 * Created on 27.10.2026
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Copies bundled dependencies - including their transitive dependencies - into the {@code node_modules} directory
 * of the directory a package is packed from. Dependencies are resolved the way node resolves them - so this works
 * for the flat layout of npm as well as for the linked layout of pnpm. Packages needed in different versions are
 * nested into the {@code node_modules} directory of the package depending on them.
 */
public final class BundledDependencies {

    public static final String NODE_MODULES = "node_modules";

    private static final ObjectMapper MAPPER = NpmDependencyMetaDataReader.makeObjectMapper();

    private final Path rootModules;
    private final Map<Path, Path> sourceByTarget = new HashMap<>();

    private BundledDependencies(Path rootModules) {
        this.rootModules = rootModules;
    }

    /**
     * Copies the given dependencies - installed in the {@code node_modules} directory of {@code packageDirectory} -
     * and their transitive dependencies into the {@code node_modules} directory of {@code targetDirectory}, which is
     * replaced.
     *
     * @return the installed version of every given dependency
     */
    public static Map<String, String> copy(File packageDirectory, Collection<String> names, File targetDirectory) throws IOException {

        Path rootModules = targetDirectory.toPath().resolve(NODE_MODULES);

        deleteRecursively(rootModules);

        BundledDependencies bundledDependencies = new BundledDependencies(rootModules);
        Map<String, String> result = new LinkedHashMap<>();

        for (String name : names) {

            Path source = packageDirectory.toPath().resolve(NODE_MODULES).resolve(name);

            if (!Files.isDirectory(source)) {

                throw new IOException("bundled dependency '" + name + "' is not installed in " + source.getParent());
            }

            result.put(name, bundledDependencies.copy(name, source.toRealPath(), rootModules));
        }

        return result;
    }

    private String copy(String name, Path source, Path parentModules) throws IOException {

        Path target = rootModules.resolve(name);

        if (sourceByTarget.containsKey(target) && !sourceByTarget.get(target).equals(source)) {

            target = parentModules.resolve(name);
        }

        JsonNode packageJson = MAPPER.readTree(source.resolve(AbstractTypescriptMojo.PACKAGE_FILE_NAME).toFile());

        if (source.equals(sourceByTarget.get(target))) {

            return packageJson.path("version").asText();
        }

        sourceByTarget.put(target, source);
        copyPackage(source, target);

        copyDependencies(packageJson.path("dependencies"), false, source, target);
        copyDependencies(packageJson.path("optionalDependencies"), true, source, target);

        return packageJson.path("version").asText();
    }

    private void copyDependencies(JsonNode dependencies, boolean optional, Path source, Path target) throws IOException {

        Iterator<String> names = dependencies.fieldNames();

        while (names.hasNext()) {

            String name = names.next();
            Path dependency = resolve(name, source);

            if (dependency == null) {

                if (optional) {

                    continue;
                }

                throw new IOException("dependency '" + name + "' of bundled package " + source + " is not installed");
            }

            copy(name, dependency.toRealPath(), target.resolve(NODE_MODULES));
        }
    }

    /**
     * Resolves a package the way node does: by looking into the {@code node_modules} directories of the package
     * directory and all of its parents.
     */
    private static Path resolve(String name, Path from) {

        for (Path directory = from; directory != null; directory = directory.getParent()) {

            if (directory.getFileName() != null && directory.getFileName().toString().equals(NODE_MODULES)) {

                continue;
            }

            Path candidate = directory.resolve(NODE_MODULES).resolve(name);

            if (Files.isDirectory(candidate)) {

                return candidate;
            }
        }

        return null;
    }

    private static void copyPackage(Path source, Path target) throws IOException {

        Path nestedModules = source.resolve(NODE_MODULES);

        Files.walkFileTree(source, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) throws IOException {

                if (directory.equals(nestedModules)) {

                    return FileVisitResult.SKIP_SUBTREE;
                }

                Files.createDirectories(target.resolve(source.relativize(directory).toString()));

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {

                Files.copy(file, target.resolve(source.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void deleteRecursively(Path directory) throws IOException {

        if (!Files.exists(directory, LinkOption.NOFOLLOW_LINKS)) {

            return;
        }

        Files.walkFileTree(directory, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {

                Files.delete(file);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exception) throws IOException {

                Files.delete(dir);

                return FileVisitResult.CONTINUE;
            }
        });
    }
}
