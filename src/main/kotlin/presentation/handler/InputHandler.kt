package presentation.handler

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class InputHandler {

    fun getFilePath(fileType: String): String {
        while (true) {
            print("$fileType: ")
            val filePath = readLine()!!
            val file = File(filePath)
            if (file.exists()) {
                return filePath
            } else {
                println("File does not exist. ")
            }
        }
    }

    fun getFilmName(): String {
        println("Enter film name: ")
        return readLine()!!
    }

    fun getSessionTime(): Date {
        println("Enter session date and time (yyyy-MM-dd HH:mm): ")
        return SimpleDateFormat("yyyy-MM-dd HH:mm").parse(readLine()!!)
    }

    fun getSeats(): List<Int> {
        println("Enter seats (1-30) (enter the seats separated by a space) : ")
        return readLine()!!.split(" ").map { it.toInt() }
    }

    fun getFilmId(): String {
        println("Enter film id: ")
        return readLine()!!
    }

    fun getFilmDirector(): String {
        println("Enter film director: ")
        return readLine()!!

    }

    fun getFilmDuration() : Int {
        println("Enter film duration: ")
        return readLine()!!.toInt()
    }

    fun getRentalDates(): Pair<Date, Date> {
        println("Enter rental dates (yyyy-MM-dd): ")
        val rentalDates = readLine()!!.split(" ").map { SimpleDateFormat("yyyy-MM-dd").parse(it) }
        return rentalDates[0] to rentalDates[1]
    }

    fun getSeatsNumber(): Int {
        println("Enter seats number: ")
        return readLine()!!.toInt()
    }
}