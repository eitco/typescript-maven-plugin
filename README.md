
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

In this phase first the `install-node-and-package-manager` goal is called. It executes the goal `install-node-and-npm` 
(or `install-node-and-pnpm` if `packageManager` is `PNPM`) of the [frontend maven plugin](https://github.com/eirslett/frontend-maven-plugin), 
using the configuration of the frontend maven plugin given in the pom. This will install node and npm in the configured 
versions. No need to install it by yourself. If the pom does not specify a version of the frontend maven plugin, the 
version this plugin depends on is used.

> 📘 Up to version 5.0.2 the goal `install-node-and-npm` of the frontend maven plugin was bound directly with the execution 
> id `default-install-node-and-npm`. Only the plugin level configuration of the frontend maven plugin is used now - if you 
> configured the execution `default-install-node-and-npm`, move its configuration to the plugin level.

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
deploying the package created, the `package.json` file and the `package-lock.json` file (or 
`pnpm-lock.yaml` when using pnpm) to the configured maven remote repository.

### authentication

By default `npm publish` uses npm's own authentication (e.g. an `.npmrc` file). Alternatively, the
credentials can be taken from a server in the maven `settings.xml` - encrypted passwords are supported:

```xml
<servers>
    <!-- basic authentication, e.g. for nexus or artifactory -->
    <server>
        <id>npm-private</id>
        <username>deployer</username>
        <password>{encrypted-password}</password>
    </server>
    <!-- token authentication, e.g. for npmjs.org -->
    <server>
        <id>npmjs</id>
        <password>npm_token</password>
    </server>
</servers>
```

```xml
<configuration>
    <publishRegistryURL>https://nexus.example.com/repository/npm-private/</publishRegistryURL>
    <publishServerId>npm-private</publishServerId>
</configuration>
```

If the server specifies a username, basic authentication is used, otherwise its password is used as 
auth token. The credentials are handed to npm as environment variables (registry scoped `npm_config_` 
variables) and are never written to an `.npmrc` file. This requires npm 7 or later. The same works for pnpm,
the variables are additionally given with the prefix `pnpm_config_`, which pnpm 11 reads.

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
* `BUNDLED`-dependencies will be added to the delivery artifact - including their transitive dependencies. They are 
  listed as `dependencies` and `bundledDependencies` in the `package.json` file delivered, with the version installed.
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

## pnpm

Instead of npm, [pnpm](https://pnpm.io) can be used to install dependencies, run scripts, pack and publish the
package. pnpm 10 (10.17 or later), pnpm 11 and pnpm 12 are supported. Note that pnpm 11 and 12 require node 22.13 or 
later. pnpm 10.16 accepts `minimumReleaseAge`, but does not enforce it reliably. pnpm 12 downloads its native binary 
from the npm registry the first time it is executed - this download does not use maven's proxy or mirror settings.

To use pnpm, set the parameter `packageManager` to `PNPM` and configure the pnpm version at the
`frontend-maven-plugin`:
```xml
...
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <configuration>
                    <nodeVersion>v22.13.0</nodeVersion>
                    <pnpmVersion>11.28.8</pnpmVersion>
                </configuration>
            </plugin>
        </plugins>
    </pluginManagement>
    <plugins>
        <plugin>
            <groupId>de.eitco.cicd</groupId>
            <artifactId>typescript-maven-plugin</artifactId>
            <extensions>true</extensions>
            <configuration>
                <packageManager>PNPM</packageManager>
            </configuration>
        </plugin>
    </plugins>
</build>
...
```

The goal `install-node-and-package-manager` then installs node and pnpm instead of node and npm.

The parameter `legacyPeerDeps` is not supported by pnpm and ignored. Publishing with pnpm skips pnpm's git checks
(`--no-git-checks`), since the package is published from the `target/dist` directory of a working copy.

### dependency scripts

pnpm does not execute scripts (like `postinstall`) of dependencies unless they are explicitly allowed. pnpm 10 only
warns about ignored scripts, pnpm 11 fails the installation. The dependencies allowed to execute scripts are listed
with the parameter `allowBuilds`:
```xml
<configuration>
    <packageManager>PNPM</packageManager>
    <allowBuilds>
        <allowBuild>esbuild</allowBuild>
    </allowBuilds>
</configuration>
```

### minimum release age

pnpm can refuse to resolve - and thus download - versions of dependencies that were published recently. This
protects against compromised versions that are usually detected and removed within hours. The parameter
`minimumReleaseAge` specifies the minimum age in minutes, `minimumReleaseAgeExclude` lists dependencies exempt from
this rule:
```xml
<configuration>
    <packageManager>PNPM</packageManager>
    <!-- one day -->
    <minimumReleaseAge>1440</minimumReleaseAge>
    <minimumReleaseAgeExclude>
        <minimumReleaseAgeExclude>@eitco/*</minimumReleaseAgeExclude>
    </minimumReleaseAgeExclude>
</configuration>
```

If `minimumReleaseAge` is left empty, pnpm's default is used: pnpm 10 does not check the release age, pnpm 11 requires
one day. Set it to `0` to disable the check with pnpm 11.

### pnpm-workspace.yaml

The goal `generate-project` writes the settings above to the file `pnpm-workspace.yaml`. The build allow list is
written as `allowBuilds` (read by pnpm 11) and `onlyBuiltDependencies` (read by pnpm 10). The keys `allowBuilds`,
`onlyBuiltDependencies`, `minimumReleaseAge` and `minimumReleaseAgeExclude` are managed by the plugin - any other
setting in an existing `pnpm-workspace.yaml` is kept.

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



