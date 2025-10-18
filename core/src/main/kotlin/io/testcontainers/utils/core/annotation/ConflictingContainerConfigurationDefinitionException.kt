package io.testcontainers.utils.core.annotation

class ConflictingContainerConfigurationDefinitionException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : IllegalStateException(message, cause)