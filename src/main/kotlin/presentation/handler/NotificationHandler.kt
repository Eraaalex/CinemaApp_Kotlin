package presentation.handler

import utils.extention.toGreen

object NotificationHandler {
    fun notify(message: String?) {
        println(message?.toGreen())
    }
}