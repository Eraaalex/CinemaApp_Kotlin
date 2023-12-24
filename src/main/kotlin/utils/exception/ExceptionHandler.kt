package utils.exception

import utils.extention.toRed

object ExceptionHandler {
    fun handleException(e: Exception) {
        println(e.message?.toRed())
    }
}