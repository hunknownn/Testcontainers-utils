package io.testcontainers.utils.core

enum class Component(val defaultImage: String) {
    POSTGRESQL("postgres:16"),
//    MONGODB,
//    REDIS,
    NONE("");
}