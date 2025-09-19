package dev.ktool.kotest

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

    "test another thing" {
        Given
        val a = 5

        When
        val b = a * 5

        Then
        b shouldBe 25
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

    "test rows where {1} * 10 = {2}"(
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
