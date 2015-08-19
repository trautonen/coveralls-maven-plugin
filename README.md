coveralls-maven-plugin
======================

[![Join the chat at https://gitter.im/trautonen/coveralls-maven-plugin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/trautonen/coveralls-maven-plugin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Build Status](http://img.shields.io/travis/trautonen/coveralls-maven-plugin/master.svg)](https://travis-ci.org/trautonen/coveralls-maven-plugin)
[![Coverage Status](http://img.shields.io/coveralls/trautonen/coveralls-maven-plugin/master.svg)](https://coveralls.io/r/trautonen/coveralls-maven-plugin?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.eluder.coveralls/coveralls-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eluder.coveralls/coveralls-maven-plugin/)

Maven plugin for submitting Java code coverage reports to [Coveralls](https://coveralls.io/) web
service.


### Features

* Supports [Cobertura](http://mojo.codehaus.org/cobertura-maven-plugin/),
  [JaCoCo](http://www.eclemma.org/jacoco/trunk/doc/maven.html) and
  [Saga](http://timurstrekalov.github.io/saga/) coverage tools
* Multi-module report aggregation
* Built-in support for [Travis CI](https://travis-ci.org/), [Circle](https://circleci.com/),
  [Codeship](https://www.codeship.io/), [Jenkins](http://jenkins-ci.org/),
  [Bamboo](https://www.atlassian.com/software/bamboo/) and [Shippable](https://www.shippable.com/) continuous integration services
* Fully streaming implementation for fast report generation and small memory footprint
* Provides clean interfaces to allow easy extending to different coverage tools
* Convention over configuration for almost zero configuration usage
* Applies [semantic versioning](http://semver.org/)


### Usage

Set up the Coveralls maven plugin in the build section of the project pom.xml:

```xml
<plugin>
    <groupId>org.eluder.coveralls</groupId>
    <artifactId>coveralls-maven-plugin</artifactId>
    <version>3.1.0</version>
    <configuration>
        <repoToken>yourcoverallsprojectrepositorytoken</repoToken>
    </configuration>
</plugin>
```

#### Configuration

If used as a standalone Maven build or with any continuous integration server other than Travis
CI, the Coveralls repository token must be provided. This can be achieved by setting the
configuration section in the plugin or setting the Maven property `repoToken` to your coveralls
project repository token, using `-DrepoToken=yourcoverallsprojectrepositorytoken` when running the
maven command. **Do not publish your repository token in public GitHub repositories.** If you do,
anyone can submit coverage data without permission.

If you are using Travis CI, CircleCI, Codeship, Jenkins or Bamboo continuous integration services,
no other configuration is required. The plugin's built-in service environment support take care of
the rest. The plugin tries to find report files for any of the supported coverage tools and
finally aggregates the coverage report. Java 8 is currently supported only by JaCoCo.

See [Complete plugin configuration](#complete-plugin-configuration) for all of the available
configuration parameters.


#### Cobertura

Set up the Cobertura Maven plugin with XML report format in the build section of the project
pom.xml:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>cobertura-maven-plugin</artifactId>
    <version>2.6</version>
    <configuration>
        <format>xml</format>
        <maxmem>256m</maxmem>
        <!-- aggregated reports for multi-module projects -->
        <aggregate>true</aggregate>
    </configuration>
</plugin>
```

Execute Maven to create Cobertura report and submit Coveralls data:

```
mvn cobertura:cobertura coveralls:report
```

For example if you are using Travis CI this means you need to add to your `.travis.yml` the lines:

```
after_success:
  - mvn clean cobertura:cobertura coveralls:report
```


#### JaCoCo

Set up the JaCoCo Maven plugin in the build section of the project pom.xml:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.7.2.201409121644</version>
    <executions>
        <execution>
            <id>prepare-agent</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Execute Maven to create JaCoCo report and submit Coveralls data:

```
mvn clean test jacoco:report coveralls:report
```

Again, if you are using Travis CI this means you need to add to your `.travis.yml` the lines:

```
after_success:
  - mvn clean test jacoco:report coveralls:report
```


#### Saga

Set up the Saga Maven plugin in the build section of the project pom.xml:

```xml
<plugin>
    <groupId>com.github.timurstrekalov</groupId>
    <artifactId>saga-maven-plugin</artifactId>
    <version>1.5.2</version>
    <executions>
        <execution>
            <goals>
                <goal>coverage</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <baseDir>http://localhost:${jasmine.serverPort}</baseDir>
        <outputDir>${project.build.directory}/saga-coverage</outputDir>
        <noInstrumentPatterns>
            <pattern>.*/spec/.*</pattern>
        </noInstrumentPatterns>
    </configuration>
</plugin>
```

Note that Saga does not have default report output directory, but the plugin assumes
`${project.build.directory}/saga-coverage`.

Execute Maven to create Saga report and submit Coveralls data:

```
mvn clean test saga:coverage coveralls:report
```

And if you are using Travis CI this means you need to add to your `.travis.yml` the lines:

```
after_success:
  - mvn clean test saga:coverage coveralls:report
```


#### Aggregate multiple reports

Report aggregation is applied by default and the only thing the user must take care of is to run
all the desired coverage tools. You can use JaCoCo in a multi-module project so that all modules
run JaCoCo separately and let the plugin aggregate the report, or you can run Saga and Cobertura
in same project and get coverage report for JavaScript and Java files.

Execute Maven to create Saga and Cobertura report and submit Coveralls data:

```
mvn clean test saga:coverage cobertura:cobertura coveralls:report
```

And if you are using Travis CI this means you need to add to your `.travis.yml` the lines:

```
after_success:
  - mvn clean test saga:coverage cobertura:cobertura coveralls:report
```


### Complete plugin configuration

Configuration can be changed by the configuration section of plugin's definition in POM or with
Java virtual machine system properties using the syntax `-Dparameter=value`. See
[Maven plugin guide](http://maven.apache.org/guides/plugin/guide-java-plugin-development.html#Configuring_Parameters_in_a_Project)
how different types are mapped in the configuration XML. Some of the optional parameters are set
by the built-in service environment setups. Note that if a parameter is explicitly defined, the
service environment will not override it.

| Parameter | Type | Description |
| --------- | ---- | ----------- |
| `jacocoReports` | `List<File>` | List of additional JaCoCo report files. ${project.reporting.outputDirectory}/jacoco/jacoco.xml is used as default for every module. |
| `coberturaReports` | `List<File>` | List of additional Cobertura report files. ${project.reporting.outputDirectory}/cobertura/coverage.xml is used as default for every module. |
| `sagaReports` | `List<File>` | List of additional Saga report files. ${project.build.directory}/saga-coverage/total-coverage.xml is used as default for every module. |
| `relativeReportDirs` | `List<String>` | List of additional relative report directories. Directories relative to ${project.reporting.outputDirectory} and ${project.build.directory} are scanned for reports. |
| `coverallsFile` | `File` | **Default: ${project.build.directory}/coveralls.json**<br>File path to write and submit Coveralls data. |
| `coverallsUrl` | `String` | **Default: https://coveralls.io/api/v1/jobs**<br>Url for the Coveralls API. |
| `sourceDirectories` | `List<File>` | List of additional source directories. The plugin will scan the project's compiled source roots for defaults. |
| `sourceEncoding` | `String` | **Default: ${project.build.sourceEncoding}**<br>Source file encoding. |
| `serviceName` | `String` | CI service name. If not provided the supported service environments are used. |
| `serviceJobId` | `String` | CI service job id. Currently supported only with Travis CI. If this property is set, `repoToken` is not required. If not provided the supported service environments are used. | 
| `serviceBuildNumber` | `String` | CI service build number. If not provided the supported service environments are used. |
| `serviceBuildUrl` | `String` | CI service build url. If not provided the supported service environments are used. |
| `serviceEnvironment` | `Properties` | CI service specific environment properties. If not provided the supported service environments are used. |
| `repoToken` | `String` | Coveralls repository token. **Do not publish this parameter unencrypted in public GitHub repositories.** |
| `branch` | `String` | Git branch name. If not provided the supported service environments are used. |
| `pullRequest` | `String` | GitHub pull request identifier. If not provided the supported service environments are used. |
| `timestamp` | `Date` | **Default: ${timestamp}**<br>Build timestamp. Must be in Maven supported 'yyyy-MM-dd HH:mm:ssa' format. |
| `dryRun` | `boolean` | **Default: false**<br>Dry run Coveralls report without actually sending it. |
| `failOnServiceError` | `boolean` | **Default: true**<br> Fail build if Coveralls service is not available or submission fails for internal errors. |
| `scanForSources` | `boolean` | **Default: false**<br>Scan subdirectories for source files. |
| `coveralls.basedir` | `File` | **Default: ${project.basedir}**<br>Base directory of the project. |
| `coveralls.skip` | `boolean` | **Default: false**<br>Skip the plugin execution. |


### FAQ

> **Q:** How do I know that my coverage report was submitted successfully to Coveralls?  
> **A:** The plugin will end with BUILD SUCCESS and the log contains the reported job id and
> direct URL to Coveralls.

<!-- -->
> **Q:** I get BUILD SUCCESS but why Coveralls shows only question marks in the reports?  
> **A:** The data is most likely reported correctly, but Coveralls might take hours, or even a
> day, to update the actual coverage numbers.

<!-- -->
> **Q:** Can I use Java 8 with the plugin?  
> **A:** Yes. The Coveralls plugin works fine with Java 8, but the problem is the coverage tools.
> Currently only tool supporting Java 8 is JaCoCo. You can use JaCoCo in a single module or
> a multi-module project and let the Coveralls plugin handle the report aggregation. This is not
> true aggregation though and does not address cross module coverage calculation (see
> https://github.com/jacoco/jacoco/pull/97)

<!-- -->
> **Q:** Build fails with 'javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated'
> exception, what to do?  
> **A:** If the build is run with OpenJDK, you probably hit an issue with the Cryptography Package
> Providers not supporting all Elliptic Curves. The [issue](https://bugs.launchpad.net/ubuntu/+source/openjdk-6/+bug/1006776)
> is described in the Ubuntu issue tracker. A workaround is to disable the PKCS provider from the
> `java.security` options file.
> ```
> sudo sed -i 's/security.provider.9/#security.provider.9/g' $JAVA_HOME/jre/lib/security/java.security
> ```
> In Travis CI the above command can be added to before_install phase. See complete example from
> this project's `.travis.yml`.

<!-- -->
> **Q:** How can I use Scala or some other project which sources reside in other folder than
> `src/main/java`?  
> **A:** The plugin uses all compiled source roots available for the project at runtime. If the
> source directories are available, everything is fine. Otherwise additional source directories
> can be applied with `sourceDirectories` configuration parameter that takes a Maven configuration
> style list of source directories.

<!-- -->
> **Q:** How can I set the plugin to use multiple source directories?  
> **A:** For multi-module projects, the plugin automatically scans the project hierarchy and adds
> all required source directories. You can also customize the used source directories with
> `sourceDirectories` configuration parameter that takes a Maven configuration style list of
> source directories.

<!-- -->
> **Q:** Why source files are not found for generated sources?  
> **A:** Generated source directories under target are not added to the sources list
> automatically. It is often not good practice to test generated code, because the code is not
> managed by the project under test, unless you are testing a source generator. Cobertura and
> JaCoCo both have `<excludes>` configuration directive that provides ignoring of class files. If
> the generated sources still must be tested, all source directories can be explicitly defined
> with `sourceDirectories` configuration parameter.

<!-- -->
> **Q:** JaCoCo or Cobertura, which one should i choose?  
> **A:** For multi-module projects, only Cobertura supports report aggregation out of the box. The
> coverage metrics and performance of the two plugins are not much different for a small or medium
> sized project, but there are 2 notable differences with the tools:
> - JaCoCo does not track how many times a single line of code is hit by all the tests together,
> so Coveralls is always reported with 1 as the number of hits if the line is covered. Cobertura
> tracks the number of hits and the number is reported to Coveralls.
> - Cobertura tracks all inner classes separately, so a single source file will contain multiple
> records with same file name in Coveralls if there are any innner classes defined. The
> coveralls-maven-plugin adds classifier from the inner class to distinguish the files, but if
> there are lot of inner classes defined this creates some noise to the Coveralls reports. JaCoCo
> tracks inner classes within same source file so each source file is only reported once to
> Coveralls.


### Changelog

See [changelog](CHANGELOG.md) for more details.


### Migration

See [migration](MIGRATION.md) documentation for more information.


### Credits

- Jakub Bednář (@bednar) for Saga integration and the idea of chaining multiple reports provided
  by different coverage tools.
- Marvin Froeder (@velo) for Shippable support, configurable basedir and directory scanning source
  loader.

### Continuous integration

Travis CI builds the plugin with Oracle JDK 7. All successfully built snapshots are deployed to
Sonatype OSS repository. Cobertura is used to gather coverage metrics and the report is submitted
to Coveralls with this plugin.


### License

The project coveralls-maven-plugin is licensed under the MIT license.
