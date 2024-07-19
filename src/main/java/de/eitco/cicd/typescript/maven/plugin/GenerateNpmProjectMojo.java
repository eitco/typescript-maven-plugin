/*
 * Copyright (c) 2022 EITCO GmbH
 * All rights reserved.
 *
 * Created on 03.03.2022
 *
 */
package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.DependencyResolutionException;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResolverException;
import org.apache.maven.shared.utils.io.FileUtils;

import java.io.File;
import java.io.IOException;


@Mojo(name = "generate-project", threadSafe = true, defaultPhase = LifecyclePhase.INITIALIZE)
public class GenerateNpmProjectMojo extends AbstractTypescriptMojo {

    @Override
    protected void execute(FrontendPluginFactory factory) {
        // will never be called since Mojo#execute() is overridden
        // this mojo only derives from the frontend abstract mojo for all npm mojos to share a same parent class
    }

    @Override
    public void execute() throws MojoFailureException {

        try {

            NpmProjectBuilder projectBuilder = makeProjectBuilder();

            final ObjectMapper mapper = NpmDependencyMetaDataReader.makeObjectMapper();

            if (defaultValues != null) {

                if (!defaultValues.isFile()) {

                    if (defaultValues.isDirectory()) {

                        getLog().warn("ignoring defaultValues " + defaultValues + " being a directory");
                    }

                    if (!defaultValues.exists()) {

                        getLog().warn("ignoring defaultValues " + defaultValues + " since the file does not exist");
                    }

                } else {

                    Package defaultPackageJson = mapper.readValue(defaultValues, Package.class);

                    projectBuilder.withDefaultValues(defaultPackageJson);
                }
            }

            final Package buildPackage = projectBuilder.makeBuildPackage();

            mapper.writeValue(new File(workingDirectory, PACKAGE_FILE_NAME), buildPackage);

            final Package deployPackage = projectBuilder.makeDeployPackage();

            FileUtils.forceMkdir(distributionDirectory);
            mapper.writeValue(new File(distributionDirectory, PACKAGE_FILE_NAME), deployPackage);

            TsConfig tsConfig = TsConfig.mainConfig(compileOptions, sources);
            tsConfig.setExclude(sourceExcludes);
            tsConfig.setAngularCompilerOptions(angularCompilerOptions);
            tsConfig.getCompilerOptions().setOutDir(distributionDirectory.getPath());

            mapper.writeValue(new File(workingDirectory, TsConfig.COMPILE_CONFIG_FILE), tsConfig);

            TsConfig testTsConfig = TsConfig.testConfig(testCompileOptions, testSources);
            testTsConfig.setAngularCompilerOptions(angularCompilerOptions);
            testTsConfig.setExclude(testSourceExcludes);
            mapper.writeValue(new File(workingDirectory, TsConfig.TEST_COMPILE_CONFIG_FILE), testTsConfig);

        } catch (DependencyResolutionException | ArtifactResolverException | IOException e) {

            throw new MojoFailureException(e.getMessage(), e);
        }

    }

}
