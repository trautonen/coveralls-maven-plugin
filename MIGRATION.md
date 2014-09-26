# Migration guide

Changes marked with bold affect the plugin usage. Other changes are only related to development
and codebase.


## 2.x to 3.x

-


## 1.x to 2.x

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
