package presentation

import exception.NotFoundException
import presentation.handler.NotificationHandler
import service.CinemaServiceImpl
import java.util.*

class CinemaController(private val cinemaServiceImpl: CinemaServiceImpl) {

    fun displaySeats(filmName: String, sessionTime: Date) {
        val session = cinemaServiceImpl.getSession(filmName, sessionTime)
        if (session == null) {
            throw NotFoundException("No sessions found")

        }
        val seatsList = cinemaServiceImpl.getReservedSeats(
            session
        )
        for (index in 1..session.seatsNumber) { // Показ мест
            if (seatsList.contains(index)) {
                print("[X] ")
            } else {
                print("[0] ")
            }
            if (index % 10 == 0) {
                println()
            }
        }

    }

    fun displayAllFilms() {
        cinemaServiceImpl.getAllFilms().forEach {
            println(it)
        }
    }

    fun displayAllSessions() {
        cinemaServiceImpl.getAllSessions().forEach {
            println(it)
        }
    }

    fun buyTickets(
        filmName: String,
        sessionTime: Date,
        seats: List<Int>
    ) {

        cinemaServiceImpl.buyTickets(
            filmName,
            sessionTime,
            seats
        )
        NotificationHandler.notify("Tickets bought successfully!")
    }

    fun returnTickets(
        filmName: String,
        sessionTime: Date,
        seats: List<Int>
    ) {
        cinemaServiceImpl.returnTickets(
            filmName,
            sessionTime, seats
        )
        NotificationHandler.notify("Tickets returned successfully!")

    }

    fun updateFilm(
        filmId: String,
        newFilmName: String,
        newFilmDuration: Int,
        newFilmDirector: String
    ) {
        cinemaServiceImpl.updateFilm(
            filmId,
            newFilmName,
            newFilmDirector,
            newFilmDuration
        )
        NotificationHandler.notify("Film updated successfully!")

    }

    fun updateSession(filmId: String, sessionTime: Date) {

        cinemaServiceImpl.updateSession(filmId, sessionTime)
        NotificationHandler.notify("Session updated successfully!")


    }

    fun markVisit(filmName: String, sessionDate: Date, seats: List<Int>) {
        cinemaServiceImpl.markVisit(filmName, sessionDate, seats)
        NotificationHandler.notify("Visit marked successfully!")
    }

    fun addFilm(filmName: String, filmDirector: String, durationInMinutes: Int, rentalDates : Pair<Date, Date>) {
        cinemaServiceImpl.addFilm(
            filmName,
            filmDirector,
            durationInMinutes,
            rentalDates
        )
        NotificationHandler.notify("Film added successfully!")
    }

    fun addSession(filmName: String, sessionTime: Date, seatsNumber: Int) {
        cinemaServiceImpl.addSession(
            filmName,
            sessionTime,
            seatsNumber
        )
        NotificationHandler.notify("Session added successfully!")
    }

    fun deleteFilm(filmName: String, sessionTime: Date) {
        cinemaServiceImpl.deleteFilm(
            filmName,
            sessionTime
        )
        NotificationHandler.notify("Film deleted successfully!")
    }

    fun deleteSession(filmName: String, sessionTime: Date) {
        cinemaServiceImpl.deleteSession(
            filmName,
            sessionTime
        )
        NotificationHandler.notify("Session deleted successfully!")
    }
}