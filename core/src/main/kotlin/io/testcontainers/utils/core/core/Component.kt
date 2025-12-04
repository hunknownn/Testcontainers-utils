package io.testcontainers.utils.core.core

enum class Component(val defaultImage: String) {
    POSTGRESQL("postgres:16"),
//    MONGODB,
REDIS("redis:7.4"),
    NONE("");
}