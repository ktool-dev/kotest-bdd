package dev.ktool.kotest

import dev.ktool.kotest.BddSpecRootScope.ToValueList
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.scopes.StringSpecRootScope
import io.kotest.core.spec.style.scopes.StringSpecScope
import io.kotest.core.spec.style.scopes.addContainer
import io.kotest.core.spec.style.scopes.addTest
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestScope
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

interface BddSpecRootScope : StringSpecRootScope, BddSections {
    /**
     * Adds a String Spec test using the default test case config.
     */
    override operator fun String.invoke(test: suspend StringSpecScope.() -> Unit) {
        addTest(
            TestName(focus = false, bang = false, prefix = null, name = this, suffix = null, defaultAffixes = true),
            false,
            null,
        ) {
            callTest(this, testCase, test)
        }
    }

    private suspend fun callTest(testScope: TestScope, testCase: TestCase, test: suspend StringSpecScope.() -> Unit) {
        bddSpecCallState.clear()

        StringSpecScope(testScope.coroutineContext, testCase).test()
        if (!bddSpecCallState.thenCalled && !bddSpecCallState.expectCalled) error("You should at least have a When/Then or Expect block in your test")
    }

    operator fun <T : ToValueList> String.invoke(vararg a: T, test: suspend StringSpecScope.(T) -> Unit) {
        val testTemplate = this
        addContainer(
            testName = TestName(
                focus = false,
                bang = false,
                prefix = null,
                name = testTemplate,
                suffix = null,
                defaultAffixes = true
            ),
            disabled = false,
            config = null
        ) {
            a.forEach {
                BddSpecContainerScope(this).addTest(buildTestName(it, testTemplate)) {
                    callTest(this, testCase) { test(it) }
                }
            }
        }
    }

    fun <A> row(a: A) = One(a)
    fun <A, B> row(a: A, b: B) = Two(a, b)
    fun <A, B, C> row(a: A, b: B, c: C) = Three(a, b, c)
    fun <A, B, C, D> row(a: A, b: B, c: C, d: D) = Four(a, b, c, d)
    fun <A, B, C, D, E> row(a: A, b: B, c: C, d: D, e: E) = Five(a, b, c, d, e)
    fun <A, B, C, D, E, F> row(a: A, b: B, c: C, d: D, e: E, f: F) = Six(a, b, c, d, e, f)
    fun <A, B, C, D, E, F, G> row(a: A, b: B, c: C, d: D, e: E, f: F, g: G) = Seven(a, b, c, d, e, f, g)
    fun <A, B, C, D, E, F, G, H> row(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H) = Eight(a, b, c, d, e, f, g, h)
    fun <A, B, C, D, E, F, G, H, I> row(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I) =
        Nine(a, b, c, d, e, f, g, h, i)

    fun <A, B, C, D, E, F, G, H, I, J> row(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J) =
        Ten(a, b, c, d, e, f, g, h, i, j)

    interface ToValueList {
        fun toValueList(): List<Any?>
    }

    data class One<A>(val a: A) : ToValueList {
        override fun toValueList(): List<Any?> = listOf(a)
    }

    data class Two<A, B>(val a: A, val b: B) : ToValueList {
        override fun toValueList(): List<Any?> = listOf(a, b)
    }

    data class Three<A, B, C>(val a: A, val b: B, val c: C) : ToValueList {
        override fun toValueList(): List<Any?> = listOf(a, b, c)
    }

    data class Four<A, B, C, D>(val a: A, val b: B, val c: C, val d: D) : ToValueList {
        override fun toValueList(): List<Any?> = listOf(a, b, c, d)
    }

    data class Five<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E) : ToValueList {
        override fun toValueList(): List<Any?> = listOf(a, b, c, d, e)
    }

    data class Six<A, B, C, D, E, F>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F) : ToValueList {
        override fun toValueList(): List<Any?> = listOf(a, b, c, d, e, f)
    }

    data class Seven<A, B, C, D, E, F, G>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G) :
        ToValueList {
        override fun toValueList(): List<Any?> = listOf(a, b, c, d, e, f, g)
    }

    data class Eight<A, B, C, D, E, F, G, H>(
        val a: A,
        val b: B,
        val c: C,
        val d: D,
        val e: E,
        val f: F,
        val g: G,
        val h: H,
    ) : ToValueList {
        override fun toValueList(): List<Any?> = listOf(a, b, c, d, e, f, g, h)
    }

    data class Nine<A, B, C, D, E, F, G, H, I>(
        val a: A,
        val b: B,
        val c: C,
        val d: D,
        val e: E,
        val f: F,
        val g: G,
        val h: H,
        val i: I,
    ) : ToValueList {
        override fun toValueList(): List<Any?> = listOf(a, b, c, d, e, f, g, h, i)
    }

    data class Ten<A, B, C, D, E, F, G, H, I, J>(
        val a: A,
        val b: B,
        val c: C,
        val d: D,
        val e: E,
        val f: F,
        val g: G,
        val h: H,
        val i: I,
        val j: J,
    ) : ToValueList {
        override fun toValueList(): List<Any?> = listOf(a, b, c, d, e, f, g, h, i, j)
    }
}

private fun <T : ToValueList> buildTestName(data: T, testTemplate: String): String {
    val values = data.toValueList().map {
        when (it) {
            null -> "null"
            "" -> "\"\""
            is KClass<*> -> it.simpleName ?: it.toString()
            is KFunction<*> -> it.name
            else -> it.toString()
        }
    }
    var injectedValues = false
    var name = Regex("""\{(?!\d+})(.*?)}""").replace(testTemplate, "{}")
    values.forEachIndexed { i, v ->
        if (name.contains("{${i + 1}")) {
            injectedValues = true
            name = name.replace("{${i + 1}}", v)
        } else if (name.contains("{}")) {
            injectedValues = true
            name = name.replaceFirst("{}", v)
        }
    }
    if (!injectedValues) {
        name = "row: $values"
    }
    return name
}
