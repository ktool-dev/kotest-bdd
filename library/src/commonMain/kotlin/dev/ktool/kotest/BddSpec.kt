package dev.ktool.kotest

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.runBlocking

abstract class BddSpec(override val enforceOrder: Boolean, body: suspend BddSpec.() -> Unit = {}) : StringSpec(),
    BddSpecRootScope {
    constructor(body: suspend BddSpec.() -> Unit = {}) : this(true, body)

    final override val bddSpecCallState = BddSpecCallState()

    init {
        runBlocking { body() }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
    }
}
