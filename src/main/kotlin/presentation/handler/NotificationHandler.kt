package presentation.handler

import extention.toGreen

object NotificationHandler {
    fun notify(message: String?) {
        println(message?.toGreen())
    }
}