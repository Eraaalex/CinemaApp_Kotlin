package utils.extention

val ANSI_RED = "\u001B[31m"
val ANSI_RESET = "\u001B[0m"
val ANSI_LIGHT_BLUE = "\u001B[94m"
val ANSI_LIGHT_GREEN = "\u001B[92m"


fun String.toRed() = "$ANSI_RED$this$ANSI_RESET"
fun String.toGreen() = "$ANSI_LIGHT_GREEN$this$ANSI_RESET"