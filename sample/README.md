Coverage samples
================

### Cobertura

```
mvn -Pcobertura clean cobertura:cobertura
```


### JaCoCo

```
mvn -Pjacoco clean verify jacoco:report
```


### Saga

```
mvn -Psaga clean test saga:coverage
```
