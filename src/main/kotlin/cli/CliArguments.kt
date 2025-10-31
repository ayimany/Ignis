package ignis.cli

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

class CliArguments(parser: ArgParser) {
    val command by parser.positional("COMMAND", help = "The command to execute").default("help")
}