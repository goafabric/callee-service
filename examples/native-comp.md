# List of possible errors for native images from the past 

# CheckIntegrity Bootstrap
- Flyway
- Hibernate
- SpringDoc*
    - https://github.com/goafabric/person-service/blob/refactoring/src/main/java/org/goafabric/personservice/Application.java
- Spring Security => Removed from Project

# During Runtime (unnoticed)
- Kotlin (Enums)
- Sometimes Hibernate (e.g https://github.com/spring-projects/spring-framework/issues/33848)

# Covered by Archunit
- Force Declarative Rest Client / Aspect Hints / Hibernate Validator Classes
- 3rd Party Libraries outside the defined stack (Apache, Guava ...)

# Completed
- Spring Batch (e.g. StepExecutionListener)

# Unclear
- Mapstruct with Composition ? (uses)