package io.testcontainers.utils.core

import io.testcontainers.utils.core.core.Recycle
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MultipleContainerTest {

    @Test
    fun `MERGE 옵션이면 매번 같은 컨테이너로 등록되는지 테스트`() {
        val devShell =
            TestGenericContainer().containerShell("alpine:3.18", Recycle.MERGE, TestAlpineCustomizer(), null)
        val devKey = devShell.key()

        val devShell2 =
            TestGenericContainer().containerShell("alpine:3.18", Recycle.MERGE, null, TestInjectable())
        val devKey2 = devShell2.key()

        assertEquals(devKey, devKey2, "동일 설정이면 같은 컨테이너로 등록되어야 함")
    }

    @Test
    fun `NEW 옵션이면 매번 새로운 컨테이너가 등록되는지 테스트`() {
        val devShell = TestGenericContainer().containerShell("alpine:3.18", Recycle.NEW, TestAlpineCustomizer(), null)
        val devKey = devShell.key()

        val devShell2 = TestGenericContainer().containerShell("alpine:3.18", Recycle.NEW, null, TestInjectable())
        val devKey2 = devShell2.key()

        assertTrue(devKey != devKey2, "NEW 옵션이면 매번 새로운 컨테이너가 등록되어야 함")
    }
}
