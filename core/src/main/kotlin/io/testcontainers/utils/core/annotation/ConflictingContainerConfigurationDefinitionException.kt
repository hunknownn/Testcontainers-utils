package io.testcontainers.utils.core.annotation

class ConflictingContainerConfigurationDefinitionException(
    override val message: String? = "Conflicting container configuration definition found",
    override val cause: Throwable? = null,
) : IllegalStateException(message, cause)