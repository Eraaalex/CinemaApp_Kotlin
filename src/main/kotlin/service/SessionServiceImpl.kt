package service

import model.Session
import utils.exception.NotFoundException
import repository.Repository
import java.util.Date

interface SessionService : BaseService<Session> {
    fun getSessionByStartTimeAndFilmId(filmId: String, startTime: Date): Session?
    fun getReservedSeats(session: Session): List<Int>
    fun getSessionsByFilmId(filmId: String): List<Session>
    fun getById(id: String): Session?
    fun getAll(): List<Session>
}

class SessionServiceImpl(private val sessionRepository: Repository<Session>) : SessionService {
    private val sessions = sessionRepository.getAll().toMutableList()

    override fun getById(id: String): Session? {
        return sessions.find { it.id == id }
    }

    override fun getAll(): List<Session> {
        return sessions
    }

    override fun add(element: Session) {
        sessions.add(element)
        sessionRepository.save(sessions)
    }

    override fun remove(session: Session) {
        val existingElementIndex = sessions.indexOfFirst { it.id == session.id }
        if (existingElementIndex != -1) {
            sessions.removeAt(existingElementIndex)
            sessionRepository.save(sessions)
        } else {
            throw NotFoundException("Session with ID ${session.id} not found.")
        }
    }

    override fun getSessionByStartTimeAndFilmId(filmId: String, startTime: Date): Session? {
        for (session in sessions) {
            if (session.filmId == filmId && session.startTime == startTime) {
                return session
            }
        }

        return sessions.find { it.startTime == startTime && it.filmId == filmId }
    }

    override fun update(updateSession: Session) {
        val existingElementIndex = sessions.indexOfFirst { it.id == updateSession.id }
        if (existingElementIndex != -1) {
            sessions[existingElementIndex] = updateSession
            sessionRepository.save(sessions)
        } else {
            throw NotFoundException("Element with ID ${updateSession.id} not found.")
        }
    }

    override fun getReservedSeats(session: Session): List<Int> =
        session.tickets.map { it.seat }.toMutableList()


    override fun getSessionsByFilmId(filmId: String): List<Session> {
        return sessions.filter { it.filmId == filmId }
    }

}