package parser

import lexer.Token

sealed class Expression
data class NumberExpression(val value: Double) : Expression()
data class BinaryExpression(val left: Expression, val op: Token, val right: Expression) : Expression()
data class VariableAccess(val name: String) : Expression()

data class BooleanLiteral(val value: Boolean) : Expression()
data class BooleanBinaryExpression(val left: Expression, val op: Token, val right: Expression) : Expression()
data class BooleanUnaryExpression(val op: Token, val expression: Expression) : Expression()

sealed class Statement
data class ExpressionStatement(val expression: Expression) : Statement()
data class VariableDeclaration(val name: String, val expression: Expression) : Statement()
