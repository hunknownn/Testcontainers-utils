rootProject.name = "testcontainers-utils"

// Type-safe project accessors (e.g., projects.core instead of project(":core"))
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("core")
include("postgresql")
include("redis")

// Examples
include("examples")
include("examples:spring-postgresql-example")
include("examples:spring-redis-example")
