package dev.kotool.kotest

import io.kotest.matchers.shouldBe

class TestSpec : BddSpec({
    "testing something" {
        Given
        val a = 10

        When
        val b = a * 10

        Then
        b shouldBe 100
    }

    "testing rows"(
        row(10, 100),
        row(20, 200),
        row(30, 300),
    ) { (a, b) ->
        When
        val result = a * 10

        Then
        result shouldBe b
    }
})
