package ignis

import ignis.parsing.Tokenizer
import java.io.File


fun main(args: Array<String>) {
    val tk = Tokenizer("src/main/resources/test.is")
    tk.tokenizeFile()
    for (token in tk.tokens) {
        println(token)
    }
}