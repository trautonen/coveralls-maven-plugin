# Changelog

## 4.2.0

- #95, #96: Improved error message for misbehaving Coveralls API
- #92: Support for HTTP proxies in settings.xml
- #91: Support for epoch formatted timestamps


## 4.1.0

- #88: Support for AppVeyor CI


## 4.0.0

- #85: Merge coverages from multiple reports to single source file
- #84: Change source content to source digest per new Coveralls API
- #83: Require Java 7 to support new features and syntax
- #77: Support for custom build timestamp format


## 3.2.1

- #87: Downgraded jgit version to support Java 6


## 3.2.0

- #82: Improved error message for duplicate classes in different modules
- #76: Property to allow Coveralls service fail without build failure
- #74: Handle transitive logging dependencies better


## 3.1.0

- #67: Configurable project basedir
- #65, #66, #68: Support for Shippable CI
- #63: Directory scanning source loader


## 3.0.1

- #53: Improved error message for missing source encoding
- #52: Ignore duplicate source files on Cobertura aggregate mode


## 3.0.0

- #48: Removed support for URL based source loading due to Coveralls changes
- #42, #45, #46: Support Coveralls new GitHub based source view 
- #40: Proper multi-module support and report aggregation
- #37, #41: Disclaimer for Java 8 usage


## 2.2.0

- #31: Improved error messages for Coveralls API failures
- #30: Improved error message for missing charset
- #28: More lenient XML parsing
- #26, #29: Support for Saga coverage tool and chain multiple coverage reports


## 2.1.0

- #24: Filter out remote names from git branches
- #19, #20: Skip configuration property to allow skipping of plugin execution


## 2.0.1

- #18: Update to HttpComponents HttpClient 4.3
- #15, #16, #17: Disable PKCS cryptography provider at runtime to work around OpenJDK SSL issue


## 2.0.0

- #13: Dry run property for test builds
- #12: Use ServiceSetup as secondary configuration source and Maven/VM properties as primary
- #11: Support multiple source directories
- #9: Support for other CI tools and platforms
- #8: Aggregated reports for multi-module projects


## 1.2.0

- #10: Validation of the Coveralls job
- #4: Report build timestamp to Coveralls
- #3: Log code lines from generated report to Maven console


## 1.1.0

- #1: Easier configuration for Travis CI


## 1.0.0

- Initial release
