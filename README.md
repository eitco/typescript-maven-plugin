
[![License](https://img.shields.io/github/license/eitco/typescript-maven-plugin.svg?style=for-the-badge)](https://opensource.org/license/mit)



[![Build status](https://img.shields.io/github/actions/workflow/status/eitco/typescript-maven-plugin/deploy.yaml?branch=main&style=for-the-badge&logo=github)](https://github.com/eitco/typescript-maven-plugin/actions/workflows/deploy.yaml)
[![Maven Central Version](https://img.shields.io/maven-central/v/de.eitco.cicd/typescript-maven-plugin?style=for-the-badge&logo=apachemaven)](https://central.sonatype.com/artifact/de.eitco.cicd/typescript-maven-plugin)

# typescript maven plugin

This maven plugin provides a build lifecycle for transpiling and packaging typescript code, which it does building 
on top of the [frontend maven plugin](https://github.com/eirslett/frontend-maven-plugin). It is intended for use in 
builds where there are java and typescript sources that produce components that need to be the same version.

