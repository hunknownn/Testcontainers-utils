package io.testcontainers.utils.core.annotation

import io.testcontainers.utils.core.listener.TestContainersListener
import org.springframework.test.context.TestExecutionListeners
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@TestExecutionListeners(
    listeners = [TestContainersListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
annotation class BootstrapTestContainer(
    val properties: Array<ContainerProperty> = []
)

