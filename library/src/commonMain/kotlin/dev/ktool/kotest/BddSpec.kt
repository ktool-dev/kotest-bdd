package dev.ktool.kotest

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.runBlocking

abstract class BddSpec(body: suspend BddSpec.() -> Unit = {}) : StringSpec(), BddSpecRootScope {
    final override val bddSpecCallState = BddSpecCallState()

    init {
        runBlocking { body() }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
    }
}
