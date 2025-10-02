package io.testcontainers.utils.spring.bootstrap.components

enum class Component(val defaultImage: String) {
    POSTGRESQL("postgres:16"),
//    MONGODB,
//    REDIS,
    NONE("");
}