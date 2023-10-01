package com.danielmaile.zenith.evaluator

import com.danielmaile.zenith.lexer.Token
import com.danielmaile.zenith.parser.BinaryExpression
import com.danielmaile.zenith.parser.BooleanBinaryExpression
import com.danielmaile.zenith.parser.BooleanLiteral
import com.danielmaile.zenith.parser.BooleanUnaryExpression
import com.danielmaile.zenith.parser.Expression
import com.danielmaile.zenith.parser.ExpressionStatement
import com.danielmaile.zenith.parser.NumberExpression
import com.danielmaile.zenith.parser.Statement
import com.danielmaile.zenith.parser.VariableAccess
import com.danielmaile.zenith.parser.VariableDeclaration

class Evaluator {

    private val variables = mutableMapOf<String, Any>()

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

    private fun evaluateExpression(expression: Expression): Any = when (expression) {
        is NumberExpression -> expression.value
        is BooleanLiteral -> expression.value

        is BinaryExpression -> {
            val left = evaluateExpression(expression.left) as Double
            val right = evaluateExpression(expression.right) as Double
            when (expression.op) {
                Token.Plus -> left + right
                Token.Minus -> left - right
                Token.Multiply -> left * right
                Token.Divide -> left / right
                else -> throw IllegalArgumentException("Unexpected operation: ${expression.op}")
            }
        }

        is BooleanBinaryExpression -> {
            val left = evaluateExpression(expression.left) as Boolean
            val right = evaluateExpression(expression.right) as Boolean
            when (expression.op) {
                Token.And -> left && right
                Token.Or -> left || right
                else -> throw IllegalArgumentException("Unexpected operation: ${expression.op}")
            }
        }

        is BooleanUnaryExpression -> {
            val value = evaluateExpression(expression.expression) as Boolean
            when (expression.op) {
                Token.Not -> !value
                else -> throw IllegalArgumentException("Unexpected operation: ${expression.op}")
            }
        }

        is VariableAccess -> {
            variables[expression.name] ?: throw IllegalArgumentException("Variable ${expression.name} was not found")
        }
    }
}
