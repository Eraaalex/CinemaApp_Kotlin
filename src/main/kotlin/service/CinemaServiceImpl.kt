package service

import model.Film
import model.Session
import exception.InvalidBookingException
import exception.InvalidReturnException
import exception.NotFoundException
import java.util.*

interface CinemaAdminService {
    fun addFilm(filmName: String, filmDirector: String, durationInMinutes: Int, rentalDates: Pair<Date, Date>)
    fun addSession(filmName: String, date: Date, seatsNumber: Int)
    fun deleteFilm(filmName: String, date: Date)
    fun deleteSession(filmName: String, date: Date)
    fun updateFilm(filmId: String, newFilmName: String, newFilmDirector: String, durationInMinutes: Int)
    fun updateSession(sessionId: String, newSessionTime: Date)
}

interface CinemaClientService {
    fun buyTickets(filmName: String, date: Date, seats: List<Int>)
    fun returnTickets(filmName: String, date: Date, seats: List<Int>)
    fun markVisit(filmName: String, sessionDate: Date, seats: List<Int>)
}

interface CinemaService {
    fun getAllFilmSessions(filmName: String, day: Date): List<Session>
    fun getSession(filmName: String, date: Date): Session?
    fun getReservedSeats(session: Session): List<Int>
}

class CinemaServiceImpl(
    val ticketService: TicketClientService,
    val filmService: FilmService,
    val sessionService: SessionService
) : CinemaService, CinemaAdminService, CinemaClientService {
    override fun getAllFilmSessions(filmName: String, day: Date): List<Session> {
        filmService.getFilmByNameAndDay(filmName, day)?.let {
            return sessionService.getSessionsByFilmId(it.id)
        }
        throw NotFoundException("Film not found")
    }

    override fun getSession(filmName: String, date: Date): Session? {  // Get Session by certain time
        val film = filmService.getFilmByNameAndDay(filmName, date)
        if (film == null) {
            return null
        }
        return sessionService.getSessionByStartTimeAndFilmId(film.id, date)
    }

    override fun buyTickets(filmName: String, date: Date, seats: List<Int>) {
        val session = getSession(filmName, date)
        session?.let {
            val isValid = ticketService.buyTickets(session, seats)
            if (!isValid) {
                throw InvalidBookingException("seats are not avaliable")
            }
        } ?: run {
            throw InvalidBookingException("Session not found")
        }
    }

    override fun returnTickets(filmName: String, date: Date, seats: List<Int>) {
        val session = getSession(filmName, date)
        if (session == null) {
            throw InvalidReturnException("Session not found")
        }
        if (!ticketService.returnTickets(session, seats)) {
            throw InvalidReturnException("Return seats failed")
        }
    }


    override fun getReservedSeats(session: Session): List<Int> {
        return sessionService.getReservedSeats(session)
    }

    override fun updateFilm(filmId: String, newFilmName: String, newFilmDirector: String, durationInMinutes: Int) {
        val film = filmService.getById(filmId)
        if (film == null) {
            throw NotFoundException("Film not found")
        }
        filmService.update(Film(filmId, newFilmName, durationInMinutes, newFilmDirector, film.rentalDates))
    }

    override fun updateSession(sessionId: String, newSessionTime: Date) {
        val session = sessionService.getById(sessionId)
        if (session == null) {
            throw NotFoundException("Session not found")
        }
        sessionService.update(
            Session(
                sessionId,
                session.filmId,
                newSessionTime,
                session.seatsNumber,
                session.tickets
            )
        )
    }

    override fun markVisit(filmName: String, sessionDate: Date, seats: List<Int>) {
        val session = getSession(filmName, sessionDate)
        if (session == null) {
            throw NotFoundException("Session not found")
        }
        if (!ticketService.markVisit(session, seats)) {
            throw InvalidBookingException("Mark visit failed")
        }

    }

    override fun addFilm(
        filmName: String,
        filmDirector: String,
        durationInMinutes: Int,
        rentalDates: Pair<Date, Date>
    ) {
        filmService.add(Film(UUID.randomUUID().toString(), filmName, durationInMinutes, filmDirector, rentalDates))
    }

    override fun addSession(filmName: String, date: Date, seatsNumber: Int) {
        val film = filmService.getFilmByNameAndDay(filmName, date)
        if (film == null) {
            throw NotFoundException("Film not found")
        }
        sessionService.add(Session(UUID.randomUUID().toString(), film.id, date, seatsNumber))
    }

    override fun deleteFilm(filmName: String, date: Date) {
        val film = filmService.getFilmByNameAndDay(filmName, date)
        if (film == null) {
            throw NotFoundException("Film not found")
        }
        filmService.remove(film)
    }

    override fun deleteSession(filmName: String, date: Date) {
        val session = getSession(filmName, date)
        if (session == null) {
            throw NotFoundException("Session not found")
        }
        sessionService.remove(session)

    }

    fun getAllFilms(): List<Film> {
        return filmService.getAll()
    }

    fun getAllSessions(): List<Session> {
        return sessionService.getAll()
    }
}