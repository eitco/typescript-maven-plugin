
[![License](https://img.shields.io/github/license/eitco/typescript-maven-plugin.svg?style=for-the-badge)](https://opensource.org/license/mit)
[![Build status](https://img.shields.io/github/actions/workflow/status/eitco/typescript-maven-plugin/deploy.yaml?branch=main&style=for-the-badge&logo=github)](https://github.com/eitco/typescript-maven-plugin/actions/workflows/deploy.yaml)
[![Maven Central Version](https://img.shields.io/maven-central/v/de.eitco.cicd/typescript-maven-plugin?style=for-the-badge&logo=apachemaven)](https://central.sonatype.com/artifact/de.eitco.cicd/typescript-maven-plugin)

# typescript maven plugin

This maven plugin provides a build lifecycle for transpiling and packaging typescript code, which it does building 
on top of the [frontend maven plugin](https://github.com/eirslett/frontend-maven-plugin). It is intended for use in 
builds where there are java and typescript sources that produce components that need to be the same version.

Opposed to the frontend-maven-plugin this plugin is intended for builds that adhere to maven conventions more strongly 
than to npm conventions. It will generate the most basic files needed for npm, like `package.json` or `ts-config.json` 
and assumes that transpiling typescript is part of the build. 

A complete reference of all goals and parameters can be found [here](https://eitco.github.io/typescript-maven-plugin/plugin-info.html)

# usage 

Add the typescript-maven-plugin to your build, enabling extensions:

```xml
...
<build>
    <plugins>
        <plugin>
            <groupId>de.eitco.cicd</groupId>
            <artifactId>typescript-maven-plugin</artifactId>
            <version>4.0.1</version>
            <extensions>true</extensions>
        </plugin>
    </plugins>
</build>
```

Now you can set the packaging of your project to `npm` activating this plugins' build lifecycle:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>your.group.id</groupId>
    <artifactId>your-artifact-id</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>npm</packaging>
...
</project>
```

# the typescript build lifecycle 

This build lifecycle contains default lifecycle phases with the following executions:

## initialize

In this phase the `generate-project` goal is called. Now the plugin generates the files `package.json`, `ts-config.json`
and `tsconfig.spec.json` from its configuration. It also generates a second `package.json` file inside the distribution 
directory. The second file will be the one distributed with the package. It contains only the basic project information
needed in a package, omitting build specific information. 

## generate-sources

In this phase first the `install-node-and-npm` goal from the [frontend maven plugin](https://github.com/eirslett/frontend-maven-plugin) is called.
This will install node and npm in the configured versions. No need to install it by yourself.
After that this plugins `npm-install` goal is called which basically executes `npm install` 

> 📘 Maven users, note that `npm install` is not like `mvn install`. It simply installs the projects dependencies, not 
> the project itself and thus needs to be done at the beginning of the build, while `mvn install` executes nearly the 
> complete build lifecycle. 

## compile

In this phase the `compile` goal is called, which in turn calls `npm run compile` executing a script generated into the 
file `package.json` earlier - which simply consists of a call to `tsc` the typescript compiler. This will compile the 
typescript sources, which are assumed in `src/main/ts` and `target/generated-sources/main/ts`.


## package

In this phase the `package` goal is called. Additionally to calling `npm pack`, this attaches 
the package created, the `package.json` file and the `package-lock.json` file to the project. 
This will result in these files being installed and deployed to the local and remote maven 
repositories.

## install

In this phase mavens default `install` goal is called. Since the package itself, the 
`package.json` file and the `package-lock.json` file were attached to the project earlier, 
those files will be stored in the local maven repository.

## deploy

In this phase the `deploy` goal is called. This first simply calls `npm publish`, publishing the 
package built to the configured npm registry. After that mavens default `deploy` goal is called,
deploying the package created, the `package.json` file and the `package-lock.json` file to the 
configured maven remote repository.

# specifying dependencies

npm package dependencies can be specified in two ways: As npm dependencies or as maven dependencies. 
Further examples how the specific types of dependencies behave are located in the [integration tests](./src/it/dependencies) 

## npm dependencies

Adding npm dependencies is the more "natural" way and should be used for most dependencies, particularly 
third party dependencies. An npm dependency can be specified in the pom as follows:

```xml
...
<build>
    <plugins>
        <plugin>
            <groupId>de.eitco.cicd</groupId>
            <artifactId>typescript-maven-plugin</artifactId>
            <version>4.0.1</version>
            <extensions>true</extensions>
            <configuration>
                <dependencies>
                    <dependency>
                        <name>[dependency-name]</name>
                        <version>[dependency-version]</version>
                        <type>[dependency-type]</type>
                    </dependency>
                </dependencies>
            </configuration>
        </plugin>
    </plugins>
</build>
...
```
`dependency-name` is the name of the dependency. e.g.: `@types/node`
`dependency-version` is the version of the dependency. e.g.: `18.11.9`
`dependency-type` is one of `BUNDLED`, `PEER`, `DEV`. The `typescript-maven-plugin` will add the dependency to 
the delivery artifact as configured by this value:

* `DEV`-dependencies will not be added to the delivery artifact as they are only needed at compile time
* `BUNDLED`-dependencies will be added to the delivery artifact
* `PEER`-dependencies will not be added to the delivery artifact, however the `package.json` file delivered will contain a reference to such a dependency.

So, in order to add `@types/node` in verion `18.11.9` as a `DEV` dependency to your project, add the following into your pom:

```xml
...
<build>
    <plugins>
        <plugin>
            <groupId>de.eitco.cicd</groupId>
            <artifactId>typescript-maven-plugin</artifactId>
            <version>4.0.1</version>
            <extensions>true</extensions>
            <configuration>
                <dependencies>
                    <dependency>
                        <name>@types/node</name>
                        <version>18.11.9</version>
                        <type>DEV</type>
                    </dependency>
                </dependencies>
            </configuration>
        </plugin>
    </plugins>
</build>
...
```

## maven dependencies

Adding a dependency as maven dependency only works for dependencies built with the `typescript-maven-plugin`. More 
precisely: It only works for `.tgz` files deployed to a maven repository that has a corresponding `.package` file 
deployed, that contains its `package.json` file.
This is mainly intended for maven builds with modules where some modules are packaged using this plugin while some of 
them depend on some of them.

To add a dependency as maven dependency, simply specify it like a dependency in a maven build that has the type `.tgz`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
...
    <dependencies>
        <dependency>
            <groupId>[group-id]</groupId>
            <artifactId>[artifact-id]</artifactId>
            <version>[version]</version>
            <type>tgz</type>
        </dependency>
        ...
    </dependencies>
...    
</project>
```

When generating the `package.json` file, the `typescript-maven-plguin` will scan for dependencies of the type `tgz` -
this includes transitive dependencies - and will add a dependency entry of every dependency found - with the version 
pointing to the resolved file of the dependency - which will reside either in the local maven repository or in one of the 
current build reactors `target` directories. This way, npm will use the local - perhaps even locally built - file when
building the project. The `typescript-maven-plugin` however will use the actual version when adding the dependency to 
the distributed `package.json`, so that any dependent project - be it managed by this plugin or not - will use the 
deployed version of that file. Dependencies added this way will always be considered 'peer' dependencies.


# configuration

## npm and node version

To configure the npm and/or node version, you need to configure the `frontend-maven-plugin`:
```xml
...    
    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>com.github.eirslett</groupId>
                    <artifactId>frontend-maven-plugin</artifactId>
                    <configuration>
                        <nodeVersion>v21.6.2</nodeVersion>
                        <npmVersion>10.2.4</npmVersion>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
...
```


## typescript source file locations

By default, typescript source files are expected in in `src/main/ts` and `target/generated-sources/main/ts`.
This can be overridden using the `sources` parameter:
```xml
...
<build>
    <plugins>
        <plugin>
            <groupId>de.eitco.cicd</groupId>
            <artifactId>typescript-maven-plugin</artifactId>
            <version>4.0.1</version>
            <extensions>true</extensions>
            <configuration>
                <sources>
                    <source>/path/to/sources</source>
                    <source>/other/path/to/sources</source>
                </sources>
            </configuration>
        </plugin>
    </plugins>
</build>
...
```

## npm registry

If you are using a different npm registry that the default registry you can configure this as follows:
```xml
...
<build>
    <plugins>
        <plugin>
            <groupId>de.eitco.cicd</groupId>
            <artifactId>typescript-maven-plugin</artifactId>
            <version>4.0.1</version>
            <extensions>true</extensions>
            <configuration>
                <npmRegistryURL>https://my.registry.io</npmRegistryURL>
            </configuration>
        </plugin>
    </plugins>
</build>
...
```

In this example the plugin will add the commandline parameter `--registry=https://my.registry.io` to every npm call. 



