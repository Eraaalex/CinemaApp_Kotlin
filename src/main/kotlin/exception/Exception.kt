package exception

import java.io.File

class FileException(file : File, message: String, cause: Throwable? = null) :
    RuntimeException("Exception with file ${file.name}: $message", cause)  {
    override val message: String = "FileException: $message"
}

class NotFoundException(message: String, cause: Throwable? = null) :
    RuntimeException("Exception with repository: $message", cause) {
    override val message: String = "NotFoundException: $message"
}

class InvalidBookingException(message: String, cause: Throwable? = null) :
    RuntimeException("Exception with buying seats: $message", cause) {
    override val message: String = "InvalidBookingException: $message"
}

class InvalidReturnException(message: String, cause: Throwable? = null) :
    RuntimeException("Exception with returning seats: $message", cause) {
    override val message: String = "InvalidReturnException: $message"
}