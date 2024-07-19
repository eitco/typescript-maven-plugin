package de.eitco.cicd.typescript.maven.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eirslett.maven.plugins.frontend.lib.FrontendException;
import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.DependencyResolutionException;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResolverException;

import java.io.File;
import java.io.IOException;

@Mojo(name = "diff-package", threadSafe = true, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class DiffPackageMojo extends AbstractTypescriptMojo {

    @Override
    protected void execute(FrontendPluginFactory factory) throws FrontendException {

        try {

            if (skipExecution()) {

                return;
            }

            File packageJson = new File(project.getBasedir(), PACKAGE_FILE_NAME);

            getLog().info("generating diff of " + packageJson + " in " + defaultValues);

            Package expectedPackage = makeProjectBuilder().makeBuildPackage();

            final ObjectMapper mapper = NpmDependencyMetaDataReader.makeObjectMapper();


            if (!packageJson.getAbsoluteFile().exists()) {

                getLog().info(packageJson.getAbsolutePath() + " does not exist");

                return;
            }

            Package actualPackageJson = mapper.readValue(packageJson, Package.class);

            Package differencePackage = actualPackageJson.diff(expectedPackage);

            if (differencePackage != null) {

                getLog().debug("diff is\n" + mapper.writeValueAsString(differencePackage));

                mapper.writeValue(defaultValues, differencePackage);

            } else {

                getLog().info("no difference found");
            }

        } catch (DependencyResolutionException | ArtifactResolverException | IOException e) {

            throw new RuntimeException(e);
        }
    }
}
