package de.eitco.cicd.typescript.maven.plugin;

import org.apache.maven.plugin.descriptor.DuplicateParameterException;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.Parameter;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class DelegatedMojoConfigurationTest {

    @Test
    void userValuesOverrideDefaults() throws Exception {

        Xpp3Dom result = DelegatedMojoConfiguration.build(
                xml("<configuration><nodeVersion>v22.13.0</nodeVersion></configuration>"),
                pnpmMojo()
        );

        assertEquals("v22.13.0", result.getChild("nodeVersion").getValue());
        assertEquals("${pnpmVersion}", result.getChild("pnpmVersion").getValue());
    }

    @Test
    void parametersUnknownToTheMojoAreDropped() throws Exception {

        Xpp3Dom result = DelegatedMojoConfiguration.build(
                xml("<configuration><npmVersion>10.2.4</npmVersion><pnpmVersion>11.28.8</pnpmVersion></configuration>"),
                pnpmMojo()
        );

        assertNull(result.getChild("npmVersion"));
        assertEquals("11.28.8", result.getChild("pnpmVersion").getValue());
    }

    @Test
    void defaultsAreKeptWithoutUserConfiguration() throws Exception {

        Xpp3Dom result = DelegatedMojoConfiguration.build(null, pnpmMojo());

        assertEquals("${nodeVersion}", result.getChild("nodeVersion").getValue());
        assertEquals("${installDirectory}", result.getChild("installDirectory").getValue());
        assertEquals("${basedir}", result.getChild("installDirectory").getAttribute("default-value"));
    }

    @Test
    void complexValuesAreKept() throws Exception {

        Xpp3Dom result = DelegatedMojoConfiguration.build(
                xml("<configuration><environmentVariables><A>b</A></environmentVariables></configuration>"),
                pnpmMojo()
        );

        assertEquals("b", result.getChild("environmentVariables").getChild("A").getValue());
    }

    private static MojoDescriptor pnpmMojo() throws DuplicateParameterException, IOException, XmlPullParserException {

        MojoDescriptor descriptor = new MojoDescriptor();

        descriptor.setGoal("install-node-and-pnpm");

        for (String name : new String[]{"nodeVersion", "pnpmVersion", "installDirectory", "environmentVariables"}) {

            Parameter parameter = new Parameter();
            parameter.setName(name);
            descriptor.addParameter(parameter);
        }

        descriptor.setMojoConfiguration(new XmlPlexusConfiguration(xml("<configuration>" +
                "<nodeVersion implementation=\"java.lang.String\">${nodeVersion}</nodeVersion>" +
                "<pnpmVersion implementation=\"java.lang.String\">${pnpmVersion}</pnpmVersion>" +
                "<installDirectory implementation=\"java.io.File\" default-value=\"${basedir}\">${installDirectory}</installDirectory>" +
                "</configuration>")));

        return descriptor;
    }

    private static Xpp3Dom xml(String xml) throws IOException, XmlPullParserException {
        return Xpp3DomBuilder.build(new StringReader(xml));
    }
}
