package lexer

sealed class Token {
    data class Number(val value: Double) : Token()

    data object Plus : Token() {
        override fun toString() = "+"
    }

    data object Minus : Token() {
        override fun toString() = "-"
    }

    data object Multiply : Token() {
        override fun toString() = "*"
    }

    data object Divide : Token() {
        override fun toString() = "/"
    }

    data object OpenParenthesis : Token() {
        override fun toString() = "("
    }

    data object CloseParenthesis : Token() {
        override fun toString() = ")"
    }

    data object Equals : Token() {
        override fun toString() = "="
    }

    data class Identifier(val name: String) : Token()
}
