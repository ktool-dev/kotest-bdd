# Kotest BDD

A Kotlin Multiplatform library that provides Behavior Driven Development (BDD) extensions
for [Kotest](https://kotest.io/). Write expressive tests using Given/When/Then or Expect syntax across JVM, iOS, macOS,
Linux, and Windows platforms. Though it is true Kotest has
a [BehaviorSpec](https://kotest.io/docs/framework/testing-styles.html#behavior-spec), they are kind of nasty to read,
and this style feels more natural.

## Features

- 🎯 **BDD Syntax**: Write tests using natural Given/When/Then or Expect blocks
- 🌐 **Multiplatform**: Supports JVM, iOS, macOS, Linux, and Windows
- 📊 **Data-driven Testing**: Built-in support for parameterized tests with `row()` function
- 🔧 **Kotest Integration**: Seamlessly integrates with Kotest's powerful testing framework
- ✅ **Type Safety**: Fully type-safe with Kotlin's strong type system

## Requirements

- **Kotlin**: 1.9.0 or higher
- **Kotest**: 5.0.0 or higher

## Installation

This package is published to Maven Central, so you can add it to your project as follows:

```kotlin
dependencies {
    testImplementation("dev.ktool:kotest-bdd:VERSION")
}
```

Replace `VERSION` with the latest version from the [releases page](https://github.com/kotlin-run/kotest-bdd/releases).

## Kotest Compatibility

Because of changes to Kotest, you have to use the correct major version corresponding to your version of Kotest.

| Kotest Version | Kotest BDD Version |
|----------------|--------------------|
| 5.0.x - 5.9.x  | 1.x.x              |

## Usage

### Basic BDD Test

Create a test class extending `BddSpec`:

```kotlin
import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class CalculatorSpec : BddSpec({
    "adding two numbers" {
        Given
        val a = 5
        val b = 3

        When
        val result = a + b

        Then
        result shouldBe 8
    }
})
```

### Using Expect Syntax

For simpler tests, you can use the `Expect` syntax instead of When/Then:

```kotlin
class SimpleSpec : BddSpec({
    "validating user input" {
        Given
        val email = "user@example.com"

        Expect
        email.contains("@") shouldBe true
    }
})
```

### Data-Driven Testing

Use the `row()` function for parameterized tests:

```kotlin
class MathSpec : BddSpec({
    "multiplication table"(
        row(2, 3, 6),
        row(4, 5, 20),
        row(7, 8, 56)
    ) { (a, b, expected) ->
        Given("two numbers $a and $b")

        When("they are multiplied")
        val result = a * b

        Then("the result should be $expected")
        result shouldBe expected
    }
})
```

### Using And Blocks

Chain multiple conditions with `And`:

```kotlin
class UserSpec : BddSpec({
    "user registration" {
        Given
        val user = User("john", "john@example.com")

        And
        user.email.contains("@") shouldBe true

        When
        val registered = userService.register(user)

        Then
        registered shouldBe true
    }
})
```

## BDD Syntax Rules

The library enforces proper BDD structure:

1. **Given** must come before When/Then or Expect
2. **When** must come before Then (cannot be used with Expect)
3. **Then** must come after When (cannot be used with Expect)
4. **Expect** can be used instead of When/Then combination
5. **And** can only be used after Given or When blocks
6. Each test must have at least one Then or Expect block

## Supported Platforms

- **JVM** (Java 17+)
- **iOS** (x64, ARM64, Simulator ARM64)
- **macOS** (x64, ARM64)
- **Linux** (x64, ARM64)
- **Windows** (x64)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Related Projects

- [Kotest](https://kotest.io/) - The underlying testing framework
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) - Cross-platform development