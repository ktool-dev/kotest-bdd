package dev.ktool.kotest

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class OutOfOrderSpec : BddSpec(false, {
    "spec works when steps are in any order" {
        Then
        val a = "1"
        a shouldBe "1"

        Given
        val another = 4

        Expect
        another shouldBe 4

        And
        a shouldBe "1"

        When
        a shouldNotBe another
    }
})