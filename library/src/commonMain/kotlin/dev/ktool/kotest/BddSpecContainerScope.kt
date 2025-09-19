package dev.ktool.kotest

import io.kotest.common.KotestInternal
import io.kotest.core.names.TestName
import io.kotest.core.spec.KotestTestScope
import io.kotest.core.spec.style.scopes.AbstractContainerScope
import io.kotest.core.spec.style.scopes.StringSpecScope
import io.kotest.core.test.TestScope

@OptIn(KotestInternal::class)
@KotestTestScope
class BddSpecContainerScope(testScope: TestScope) : AbstractContainerScope(testScope) {
    suspend fun addTest(name: String, test: suspend StringSpecScope.() -> Unit) {
        registerTest(
            name = TestName(
                focus = false,
                bang = false,
                prefix = null,
                name = name,
                suffix = null,
                defaultAffixes = false
            ),
            disabled = false,
            config = null,
        ) {
            StringSpecScope(coroutineContext, testCase).test()
        }
    }
}
