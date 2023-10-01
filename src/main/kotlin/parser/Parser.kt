package parser

import lexer.Token

class Parser(private val tokens: List<Token>) {
    private var currentIndex = 0

    private fun peek() = tokens.getOrNull(currentIndex)

    private fun advance() {
        currentIndex++
    }

    private fun parseNumber(): Expression {
        val token = peek()
        if (token is Token.Number) {
            advance()
            return NumberExpression(token.value)
        } else {
            throw IllegalArgumentException("Expected a number, but found $token")
        }
    }

    private fun parseParentheses(): Expression {
        advance() // Skip '('
        val expression = parseExpression()
        if (peek() == Token.CloseParenthesis) {
            advance()
            return expression
        } else {
            throw IllegalArgumentException("Expected a closing parenthesis, but found ${peek()}")
        }
    }

    private fun parseFactor(): Expression {
        return when (val token = peek()) {
            is Token.Number -> parseNumber()
            Token.OpenParenthesis -> parseParentheses()
            is Token.Identifier -> {
                advance()
                VariableAccess(token.name)
            }
            else -> throw IllegalArgumentException("Unexpected token: $token")
        }
    }

    private fun parseTerm(): Expression {
        var left = parseFactor()
        while (peek() == Token.Multiply || peek() == Token.Divide) {
            val op = peek()!!
            advance()
            val right = parseFactor()
            left = BinaryExpression(left, op, right)
        }
        return left
    }

    private fun parseExpression(): Expression {
        var left = parseTerm()
        while (peek() == Token.Plus || peek() == Token.Minus) {
            val op = peek()!!
            advance()
            val right = parseTerm()
            left = BinaryExpression(left, op, right)
        }
        return left
    }

    private fun parseVariableDeclarationOrAssignment(): Statement {
        val name = parseIdentifier()
        if (peek() != Token.Equals) throw IllegalArgumentException("Expected '=', but found ${peek()}")
        advance()
        val expression = parseExpression()
        return VariableDeclaration(name, expression)
    }

    private fun parseIdentifier(): String {
        val token = peek()
        if (token is Token.Identifier) {
            advance()
            return token.name
        } else {
            throw IllegalArgumentException("Expected an identifier, but found $token")
        }
    }

    private fun parseStatement(): Statement {
        return if (peek() is Token.Identifier && tokens.getOrNull(currentIndex + 1) == Token.Equals) {
            parseVariableDeclarationOrAssignment()
        } else {
            ExpressionStatement(parseExpression())
        }
    }

    fun parse(): List<Statement> {
        val statements = mutableListOf<Statement>()
        while (peek() != null) {
            statements.add(parseStatement())
        }
        return statements
    }
}
