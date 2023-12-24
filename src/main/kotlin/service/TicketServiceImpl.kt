package service

import model.Session
import model.Ticket
import exception.NotFoundException
import repository.Repository


interface TicketClientService {
    fun buyTickets(session: Session, seats: List<Int>): Boolean
    fun returnTickets(session: Session, seats: List<Int>): Boolean
    fun markVisit(session: Session, seats: List<Int>): Boolean
}

interface TicketService : BaseService<Ticket> {
    fun getTickets(sessionId: String): List<Ticket>
    fun getById(id: String): Ticket?
    fun getTicketBySessionAndSeat(session: Session, seat: Int): Ticket?
}

class TicketServiceImpl(private val sessionService: SessionService, private val ticketRepository: Repository<Ticket>) :
    TicketService, TicketClientService {

    private val tickets = ticketRepository.getAll().toMutableList()

    override fun buyTickets(session: Session, seats: List<Int>): Boolean {
        val reservedSeats = sessionService.getReservedSeats(session)
        if (seats.any { it in reservedSeats }) {
            return false
        }
        seats.forEach { seat ->
            var ticket = Ticket((tickets.size + 1).toString(), session.id, seat)
            session.tickets.add(ticket)
            tickets.add(ticket)

        }
        sessionService.update(session)
        ticketRepository.save(tickets)

        return true
    }

    override fun returnTickets(session: Session, seats: List<Int>): Boolean {
        if (session.startTime.before(java.util.Date())) {
            return false
        }
        seats.forEach { seat ->
            val ticket = session.tickets.find { it.seat == seat }
            if (ticket != null) {
                session.tickets.remove(ticket)
                tickets.remove(ticket)
            }
        }
        sessionService.update(session)
        ticketRepository.save(tickets)
        return true
    }

    private fun findTicketIndex(ticket: Ticket): Int {
        val existingElementIndex = tickets.indexOfFirst { it.id == ticket.id }
        if (existingElementIndex == -1) {
            throw NotFoundException("Element with ID ${ticket.id} not found.")
        }
        return existingElementIndex
    }

    override fun update(ticket: Ticket) {
        val existingElementIndex = findTicketIndex(ticket)
        tickets[existingElementIndex] = ticket
        ticketRepository.save(tickets)
    }

    override fun remove(ticket: Ticket) {
        val existingElementIndex = findTicketIndex(ticket)
        tickets.removeAt(existingElementIndex)
        ticketRepository.save(tickets)
    }

    override fun add(ticket: Ticket) {
        tickets.add(ticket)
        ticketRepository.save(tickets)
    }



    override fun getTickets(sessionId: String): List<Ticket> {
        val session = sessionService.getById(sessionId) ?: return emptyList()
        return session.tickets.toList()
    }

    override fun getById(id: String): Ticket? {
        return tickets.find { it.id == id }
    }



    override fun markVisit(session: Session, seats: List<Int>): Boolean {
        val reservedSeats = sessionService.getReservedSeats(session)
        if (seats.any { it !in reservedSeats }) {
            return false
        }
        seats.forEach { seat ->
            val ticket = session.tickets.find { it.seat == seat }
            if (ticket != null) {
                ticket.isVisited = true
            }
        }
        sessionService.update(session)
        ticketRepository.save(tickets)
        return true
    }

    override fun getTicketBySessionAndSeat(session: Session, seat: Int): Ticket? {
        return tickets.find { it.sessionId == session.id && it.seat == seat }
    }


}