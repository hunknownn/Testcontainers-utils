package io.testcontainers.utils.spring.bootstrap.annotations

import io.testcontainers.utils.spring.bootstrap.components.Container
import io.testcontainers.utils.spring.bootstrap.listener.TestContainersListener
import org.springframework.test.context.TestExecutionListeners
import java.lang.annotation.Inherited
import kotlin.reflect.KClass


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@TestExecutionListeners(
    listeners = [TestContainersListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
annotation class BootstrapTestContainer(
    val factories: Array<KClass<out Container<*>>> = [],
    val properties: Array<ContainerProperty> = []
)

