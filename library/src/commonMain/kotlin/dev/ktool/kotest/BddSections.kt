package dev.ktool.kotest

data class BddSpecCallState(
    var givenCalled: Boolean = false,
    var whenCalled: Boolean = false,
    var thenCalled: Boolean = false,
    var andCalled: Boolean = false,
    var expectCalled: Boolean = false,
) {
    fun clear() {
        givenCalled = false
        whenCalled = false
        thenCalled = false
        andCalled = false
        expectCalled = false
    }
}

interface BddSections {
    val bddSpecCallState: BddSpecCallState
    val enforceOrder: Boolean
    private val givenCalled: Boolean get() = bddSpecCallState.givenCalled
    private val whenCalled: Boolean get() = bddSpecCallState.whenCalled
    private val thenCalled: Boolean get() = bddSpecCallState.thenCalled
    private val andCalled: Boolean get() = bddSpecCallState.andCalled
    private val expectCalled: Boolean get() = bddSpecCallState.expectCalled

    @Suppress("ktlint:standard:function-naming")
    fun Given(given: String) {
        if (enforceOrder) {
            if (whenCalled || thenCalled || expectCalled) error("Given should only be called before other blocks")
            if (givenCalled) error("You should only have one Given block in a test, if you want a second one use And")
        }
        if (given.isNotBlank()) println("Given $given")
        bddSpecCallState.givenCalled = true
    }

    @Suppress("ktlint:standard:function-naming")
    fun When(`when`: String) {
        if (enforceOrder) {
            if (thenCalled || expectCalled) error("When should only be called before Then")
            if (whenCalled) error("You should only have one When block in a test, if you need another When you should write a another test")
        }
        if (`when`.isNotBlank()) println("When $`when`")
        bddSpecCallState.whenCalled = true
    }

    @Suppress("ktlint:standard:function-naming")
    fun Then(then: String) {
        if (enforceOrder) {
            if (!whenCalled) error("You should have a When block before a Then block, or use an Expect block")
            if (thenCalled) error("You should only have one Then block in a test")
        }
        if (then.isNotBlank()) println("Then $then")
        bddSpecCallState.thenCalled = true
    }

    @Suppress("ktlint:standard:function-naming")
    fun And(and: String) {
        if (enforceOrder) {
            if ((!givenCalled && !whenCalled) || expectCalled || thenCalled) error("And should only be called after a Given or When block")
        }
        if (and.isNotBlank()) println("And $and")
        bddSpecCallState.andCalled = true
    }

    @Suppress("ktlint:standard:function-naming")
    fun Expect(expect: String) {
        if (enforceOrder) {
            if (thenCalled || whenCalled) error("You should either use When/Then or Expect, but not both")
            if (expectCalled) error("You should only have one Expect block in a test")
        }
        if (expect.isNotBlank()) println("Expect $expect")
        bddSpecCallState.expectCalled = true
    }

    val Given: Unit get() = Given("")
    val When: Unit get() = When("")
    val Then: Unit get() = Then("")
    val Expect: Unit get() = Expect("")
    val And: Unit get() = And("")
}