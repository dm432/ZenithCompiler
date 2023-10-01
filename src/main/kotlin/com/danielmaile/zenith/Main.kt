package com.danielmaile.zenith

import com.danielmaile.zenith.evaluator.Evaluator
import com.danielmaile.zenith.lexer.Lexer
import com.danielmaile.zenith.parser.BinaryExpression
import com.danielmaile.zenith.parser.BooleanBinaryExpression
import com.danielmaile.zenith.parser.BooleanLiteral
import com.danielmaile.zenith.parser.BooleanUnaryExpression
import com.danielmaile.zenith.parser.Expression
import com.danielmaile.zenith.parser.ExpressionStatement
import com.danielmaile.zenith.parser.NumberExpression
import com.danielmaile.zenith.parser.Parser
import com.danielmaile.zenith.parser.Statement
import com.danielmaile.zenith.parser.VariableAccess
import com.danielmaile.zenith.parser.VariableDeclaration

fun main() {
    var debug = false
    val evaluator = Evaluator()

    while (true) {
        print("> ")
        val input = readlnOrNull()?.replace(";", "\n") ?: break

        if (input == "debug") {
            debug = !debug
            println("debug=$debug")
            continue
        }

        val lexer = Lexer(input)
        val tokens = lexer.lex()
        if (debug) {
            print("> Tokens: ")
            println(tokens)
        }

        val parser = Parser(tokens)
        val asts = parser.parse()
        if (debug) {
            println("> Abstract Syntax Tree: ")
            for (ast in asts) {
                println(printTree(ast))
            }
        }

        for (ast in asts) {
            val result = evaluator.evaluateStatement(ast)
            if (result != null) {
                println(result)
            }
        }
    }
}

fun printTree(statement: Statement, prefix: String = "", isTail: Boolean = true): String {
    val builder = StringBuilder()

    builder.append(prefix)
    builder.append(if (isTail) "└── " else "├── ")

    when (statement) {
        is ExpressionStatement -> {
            builder.append("\n")
            val childPrefix = prefix + (if (isTail) "    " else "│   ")
            builder.append(printExpressionTree(statement.expression, childPrefix, true))
        }
        is VariableDeclaration -> {
            builder.append("var ${statement.name} = \n")
            val childPrefix = prefix + (if (isTail) "    " else "│   ")
            builder.append(printExpressionTree(statement.expression, childPrefix, true))
        }
    }

    return builder.toString()
}

fun printExpressionTree(expression: Expression, prefix: String = "", isTail: Boolean = true): String {
    val builder = StringBuilder()

    builder.append(prefix)
    builder.append(if (isTail) "└── " else "├── ")

    when (expression) {
        is NumberExpression -> {
            builder.append(expression.value)
            builder.append("\n")
        }
        is BooleanLiteral -> {
            builder.append(expression.value)
            builder.append("\n")
        }
        is BinaryExpression -> {
            builder.append(expression.op)
            builder.append("\n")

            val childPrefix = prefix + (if (isTail) "    " else "│   ")
            builder.append(printExpressionTree(expression.left, childPrefix, false))
            builder.append(printExpressionTree(expression.right, childPrefix, true))
        }
        is BooleanBinaryExpression -> {
            builder.append(expression.op)
            builder.append("\n")

            val childPrefix = prefix + (if (isTail) "    " else "│   ")
            builder.append(printExpressionTree(expression.left, childPrefix, false))
            builder.append(printExpressionTree(expression.right, childPrefix, true))
        }
        is BooleanUnaryExpression -> {
            builder.append(expression.op)
            builder.append("\n")

            val childPrefix = prefix + (if (isTail) "    " else "│   ")
            builder.append(printExpressionTree(expression.expression, childPrefix, true))
        }
        is VariableAccess -> {
            builder.append(expression.name)
            builder.append("\n")
        }
    }

    return builder.toString()
}
