coveralls-maven-plugin
======================

[![Build Status](https://travis-ci.org/trautonen/coveralls-maven-plugin.png?branch=master)](https://travis-ci.org/trautonen/coveralls-maven-plugin)
[![Coverage Status](https://coveralls.io/repos/trautonen/coveralls-maven-plugin/badge.png?branch=master)](https://coveralls.io/r/trautonen/coveralls-maven-plugin?branch=master)

Maven plugin for submitting Java code coverage reports to [Coveralls](https://coveralls.io/) web
service.


### Features

* Supports [Cobertura](http://mojo.codehaus.org/cobertura-maven-plugin/),
  [JaCoCo](http://www.eclemma.org/jacoco/trunk/doc/maven.html) and
  [Saga](http://timurstrekalov.github.io/saga/) coverage tools
* Multi-module report aggregation with Cobertura
* Built-in support for [Travis](https://travis-ci.org/), [Circle](https://circleci.com/),
  [Codeship](https://www.codeship.io/), [Jenkins](http://jenkins-ci.org/) and
  [Bamboo](https://www.atlassian.com/software/bamboo/) continuous integration services
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
    <version>2.2.0</version>
    <configuration>
        <repoToken>yourcoverallsprojectrepositorytoken</repoToken>
    </configuration>
</plugin>
```

#### Configuration

If used as a standalone Maven build or with any continuous integration server other than Travis
CI, the Coveralls repository token must be provided. This can be achieved by setting the
configuration section in the plugin or setting a system property for VM using
`-DrepoToken=yourcoverallsprojectrepositorytoken` when running the maven command. **Do not publish
your repository token in public GitHub repositories.**

If you are using Travis, Circle, Codeship, Jenkins or Bamboo continuous integration services, no
other configuration is required. The plugin's built-in service environment support take care of
the rest. Multi-module projects that require aggregated reports have to set up Cobertura Maven
plugin for the root project with `aggregate=true`. For other projects you are free to choose
either [Cobertura](#cobertura) or [JaCoCo](#jacoco) plugin. Finally add the corresponding Maven
command for the selected plugin to your continuous integration service build job.

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
mvn cobertura:cobertura coveralls:cobertura
```

For example if you are using Travis-CI this means you need to add to your `.travis.yml` the lines:

```
after_success:
  - mvn clean cobertura:cobertura coveralls:cobertura
```


#### JaCoCo

Set up the JaCoCo Maven plugin in the build section of the project pom.xml:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.6.4.201312101107</version>
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
mvn clean test jacoco:report coveralls:jacoco
```

Again, if you are using Travis-CI this means you need to add to your `.travis.yml` the lines:

```
after_success:
  - mvn clean test jacoco:report coveralls:jacoco
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

You should also set the `sourceUrls` parameter for the plugin to load the sources from Jaasmine
server. This allows creating coverage reports also for example CoffeeScript sources:

```xml
<sourceUrls>
    <sourceUrl>http://localhost:${jasmine.serverPort}</sourceUrl>
</sourceUrls>
```

Execute Maven to create Saga report and submit Coveralls data:

```
mvn clean test saga:coverage coveralls:saga
```

And if you are using Travis-CI this means you need to add to your `.travis.yml` the lines:
```
after_success:
  - mvn clean test saga:coverage coveralls:saga
```


#### Chain

Create Coveralls data from multiple coverage tools.
*Note: The chaining approach will be the default approach for future versions of coveralls maven
plugin usage. Probably with the difference that the goal is changed from `chain` to `report`.*

Configure the coverage plugins as described earlier and instead of single coverage tool goal
use the `chain` goal to aggregate all coverage sources.

Execute Maven to create Cobertura and Saga report and submit Coveralls data:

```
mvn clean test saga:coverage cobertura:cobertura coveralls:chain
```

And if you are using Travis-CI this means you need to add to your `.travis.yml` the lines:

```
after_success:
  - mvn clean test saga:coverage cobertura:cobertura coveralls:chain
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
| `coverallsFile` | `File` | **Default: ${project.build.directory}/coveralls.json**<br>File path to write and submit Coveralls data. |
| `coverallsUrl` | `String` | **Default: https://coveralls.io/api/v1/jobs**<br>Url for the Coveralls API. |
| `sourceDirectories` | `List<File>` | List of source directories. If not provided, the plugin will scan the project's compiled source roots. |
| `sourceUrls` | `List<URL>` | List of source urls. Can be used to load sources from external service, e.g. Jasmine server. |
| `sourceEncoding` | `String` | **Default: ${project.build.sourceEncoding}**<br>Source file encoding. |
| `serviceName` | `String` | CI service name. If not provided the supported service environments are used. |
| `serviceJobId` | `String` | CI service job id. Currently supported only with Travis. If this property is set, `repoToken` is not required. If not provided the supported service environments are used. | 
| `serviceBuildNumber` | `String` | CI service build number. If not provided the supported service environments are used. |
| `serviceBuildUrl` | `String` | CI service build url. If not provided the supported service environments are used. |
| `serviceEnvironment` | `Properties` | CI service specific environment properties. If not provided the supported service environments are used. |
| `repoToken` | `String` | Coveralls repository token. **Do not publish this paramater unencrypted in public GitHub repositories.** |
| `branch` | `String` | Git branch name. If not provided the supported service environments are used. |
| `pullRequest` | `String` | GitHub pull request identifier. If not provided the supported service environments are used. |
| `timestamp` | `Date` | **Default: ${timestamp}**<br>Build timestamp. Must be in Maven supported 'yyyy-MM-dd HH:mm:ssa' format. |
| `dryRun` | `boolean` | **Default: false**<br>Dry run Coveralls report without actually sending it. |
| `coveralls.skip` | `boolean` | **Default: false**<br>Skip the plugin execution. |
| `coberturaFile` | `File` | **Default: ${project.reporting.outputDirectory}/cobertura/coverage.xml**<br>Only for `chain` goal. Cobertura report file. |
| `jacocoFile` | `File` | **Default: ${project.reporting.outputDirectory}/jacoco/jacoco.xml**<br>Only for `chain` goal. JaCoCo report file. |
| `sagaFile` | `File` | **Default: ${project.build.directory}/saga-coverage/total-coverage.xml**<br>Only for `chain` goal. Saga report file. |


### FAQ

> **Q:** How do I know that my coverage report was submitted successfully to Coveralls?  
> **A:** The plugin will end with BUILD SUCCESS and the log contains the reported job id and
> direct URL to Coveralls.

<!-- -->
> **Q:** I get BUILD SUCCESS but why Coveralls shows only question marks in the reports?  
> **A:** The data is most likely reported correctly, but Coveralls might take hours, or even a
> day, to update the actual coverage numbers.

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
> source directories are available, everything is fine. Otherwise the used source directories can
> be changed with `sourceDirectories` configuration parameter that takes a Maven configuration
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

#### 2.2.0

- #31: Improved error messages for Coveralls API failures
- #30: Improved error message for missing charset
- #28: More lenient XML parsing
- #26, #29: Support for Saga coverage tool and chain multiple coverage reports


#### 2.1.0

- #24: Filter out remote names from git branches
- #19, #20: Skip configuration property to allow skipping of plugin execution


#### 2.0.1

- #18: Update to HttpComponents HttpClient 4.3
- #15, #16, #17: Disable PKCS cryptography provider at runtime to work around OpenJDK SSL issue


#### 2.0.0

- #13: Dry run property for test builds
- #12: Use ServiceSetup as secondary configuration source and Maven/VM properties as primary
- #11: Support multiple source directories
- #9: Support for other CI tools and platforms
- #8: Aggregated reports for multi-module projects


#### 1.2.0

- #10: Validation of the Coveralls job
- #4: Report build timestamp to Coveralls
- #3: Log code lines from generated report to Maven console


#### 1.1.0

- #1: Easier configuration for Travis CI


#### 1.0.0

- Initial release


### Migration guide

Changes marked with bold affect the plugin usage. Other changes are only related to development
and codebase.


#### 1.x to 2.x

- **`sourceDirectory` parameter removed and replaced with a list parameter `sourceDirectories`**
- **service environment parameters do not override configuration parameters**
- `org.eluder.coveralls.maven.plugin.service.ServiceSetup` interface completely changed to reflect
  the service specific configuration properly
- `org.eluder.coveralls.maven.plugin.service.Travis` changed to reflect the new service setup
  interface
- `org.eluder.coveralls.maven.plugin.domain.JobValidator` and all validation related code is
  now located in `org.eluder.coveralls.maven.plugin.validation` package
- `org.eluder.coveralls.maven.plugin.domain.GitRepository` does not take custom branch parameter
  as constructor argument anymore, the same behavior is handled with the new service environment
  setup
- `org.eluder.coveralls.maven.plugin.domain.Job` has only default constructor and initialization
  is done with _with*_ methods, _validate()_ method returns list of validation errors instead of
  throwing exception
- `org.eluder.coveralls.maven.plugin.domain.SourceLoader` constructor takes a list of source
  directories instead of a single source directory


### Credits

- Jakub Bednář (@bednar) for Saga integration and the idea of chaining multiple reports provided
  by different coverage tools.


### Continuous integration

Travis CI builds the plugin with Oracle JDK 7. All successfully built snapshots are deployed to
Sonatype OSS repository. Cobertura is used to gather coverage metrics and the report is submitted
to Coveralls with this plugin.


### License

The project coveralls-maven-plugin is licensed under the MIT license.
