package model

import java.util.Date


class Session(
    val id: String,
    val filmId: String,
    val startTime: Date,
    val seatsNumber: Int = 30,
    val tickets: MutableSet<Ticket> = mutableSetOf<Ticket>()
) {
    override fun toString(): String {
        return "[Session] id='$id', filmName='$filmId', startTime=$startTime, seatsNumber=$seatsNumber, tickets=$tickets"
    }
}