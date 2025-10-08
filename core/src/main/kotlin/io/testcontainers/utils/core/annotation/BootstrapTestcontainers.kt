package io.testcontainers.utils.core.annotation

import io.testcontainers.utils.core.listener.TestcontainersListener
import org.springframework.test.context.TestExecutionListeners
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@TestExecutionListeners(
    listeners = [TestcontainersListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
annotation class BootstrapTestcontainers(
    val properties: Array<ContainerProperty> = []
)

