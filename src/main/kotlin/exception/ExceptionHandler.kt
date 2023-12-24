package exception

import extention.toRed

object ExceptionHandler {
    fun handleException(e: Exception) {
        println(e.message?.toRed())
    }
}