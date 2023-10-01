package evaluator

import lexer.Token
import parser.BinaryExpression
import parser.Expression
import parser.ExpressionStatement
import parser.NumberExpression
import parser.Statement
import parser.VariableAccess
import parser.VariableDeclaration

class Evaluator {

    private val variables = mutableMapOf<String, Double>()

    fun evaluateStatement(statement: Statement): String? {
        if (statement is ExpressionStatement) {
            return evaluateExpression(statement.expression).toString()
        }

        if (statement is VariableDeclaration) {
            val value = evaluateExpression(statement.expression)
            variables[statement.name] = value
            return null
        }

        return null
    }

    private fun evaluateExpression(expression: Expression): Double = when (expression) {
        is NumberExpression -> expression.value
        is BinaryExpression -> {
            val left = evaluateExpression(expression.left)
            val right = evaluateExpression(expression.right)
            when (expression.op) {
                Token.Plus -> left + right
                Token.Minus -> left - right
                Token.Multiply -> left * right
                Token.Divide -> left / right
                else -> throw IllegalArgumentException("Unexpected operation: ${expression.op}")
            }
        }

        is VariableAccess -> {
            variables[expression.name] ?: throw IllegalArgumentException("Variable ${expression.name} was not found")
        }
    }
}
