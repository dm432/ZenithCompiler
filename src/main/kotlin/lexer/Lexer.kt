package lexer

class Lexer(private val input: String) {
    fun lex(): List<Token> {
        val tokens = mutableListOf<Token>()
        var currentIndex = 0

        fun currentChar() = if (currentIndex < input.length) input[currentIndex] else null

        while (currentIndex < input.length) {
            val currentChar = currentChar()!!

            when {
                currentChar.isDigit() || currentChar == '.' -> {
                    val startIndex = currentIndex
                    var dotEncountered = currentChar == '.'

                    while (currentIndex < input.length &&
                        (input[currentIndex].isDigit() || input[currentIndex] == '.')
                    ) {
                        if (input[currentIndex] == '.') {
                            if (dotEncountered) throw IllegalArgumentException("Unexpected character: '.' at position: $currentIndex")
                            dotEncountered = true
                        }
                        currentIndex++
                    }

                    val numberStr = input.substring(startIndex, currentIndex)
                    val number = numberStr.toDoubleOrNull()
                        ?: throw IllegalArgumentException("Invalid number format: $numberStr at position: $startIndex")
                    tokens.add(Token.Number(number))
                }

                currentChar == '+' -> {
                    tokens.add(Token.Plus)
                    currentIndex++
                }

                currentChar == '-' -> {
                    tokens.add(Token.Minus)
                    currentIndex++
                }

                currentChar == '*' -> {
                    tokens.add(Token.Multiply)
                    currentIndex++
                }

                currentChar == '/' -> {
                    tokens.add(Token.Divide)
                    currentIndex++
                }

                currentChar == '(' -> {
                    tokens.add(Token.OpenParenthesis)
                    currentIndex++
                }

                currentChar == ')' -> {
                    tokens.add(Token.CloseParenthesis)
                    currentIndex++
                }

                currentChar == '=' -> {
                    tokens.add(Token.Equals)
                    currentIndex++
                }

                currentChar.isLetter() -> {
                    val startIndex = currentIndex
                    while (currentIndex < input.length && input[currentIndex].isLetterOrDigit()) {
                        currentIndex++
                    }
                    val identifier = input.substring(startIndex, currentIndex)
                    tokens.add(Token.Identifier(identifier))
                }

                currentChar.isWhitespace() -> currentIndex++
                else -> throw IllegalArgumentException("Unexpected character: $currentChar at position: $currentIndex")
            }
        }

        return tokens
    }
}
