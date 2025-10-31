package ignis.parsing

import java.io.File

data class Token(val tokenType: TokenType, val content: String, val row: Int, val column: Int)

class Tokenizer(path: String) {
    private val file = File(path)
    val tokens = mutableListOf<Token>()
    private val content: String
    private var row = 1
    private var column = 0
    private var index = 0
    private var current: Char

    init {
        if (!(file.exists() && file.isFile)) {
            throw FileSystemException(file, reason = "File does not exist or is not a file")
        }

        content = file.readText()
        current = content[index]
    }

    fun moveIndexForward() {
        current = content[++index]
        column++

        if (current == '\n') {
            row++
            column = 0
        }
    }

    fun isAtEnd() = (index == content.length - 1)

    fun peek(): Char {
        if (isAtEnd()) throw RuntimeException("Cannot peek at end of file")
        return content[index + 1]
    }

    fun skipWhitespace() {
        while (current.isWhitespace() && !isAtEnd()) {
            moveIndexForward()
        }
    }

    fun extractIdentifierToken() {
        val tokenContent = StringBuilder()
        while (current.isLetterOrDigit() || current == '_') {
            tokenContent.append(current)
            moveIndexForward()
        }

        tokens.add(Token(TokenType.IDENTIFIER, tokenContent.toString(), row, column))
    }

    fun extractNumericToken() {
        val tokenContent = StringBuilder()
        var periodCount = 0

        while (current.isDigit() || current == '.') {
            if (current == '.') {
                periodCount++
                if (periodCount > 1) {
                    throw NumberFormatException("Numeric token cannot have more than one period")
                }
            }

            tokenContent.append(current)
            moveIndexForward()
        }


        tokens.add(Token(TokenType.NUMBER, tokenContent.toString(), row, column))
    }

    fun extractSymbolToken() {
        val tokenContent = StringBuilder()
        var continues = true

        if (symbolTypeMap[current.toString()] == null) {
            throw RuntimeException("Unrecognized symbol: $current")
        }

        while (continues) {
            tokenContent.append(current)

            if (isAtEnd()) break
            continues = symbolTypeMap.keys.any { it.startsWith(tokenContent.toString() + peek()) }

            moveIndexForward()
        }

        val finalString = tokenContent.toString()
        val tokenType = symbolTypeMap[finalString] ?: throw RuntimeException("Unrecognized symbol: $finalString")

        tokens.add(Token(tokenType, tokenContent.toString(), row, column))
    }

    fun reactAndAdvance() {
        if (current.isWhitespace()) skipWhitespace()
        else if (current.isLetter() || current == '_') extractIdentifierToken()
        else if (current.isDigit()) extractNumericToken()
        else extractSymbolToken()
    }

    fun tokenizeFile() {
        while (!isAtEnd()) {
            reactAndAdvance()
        }
    }
}
