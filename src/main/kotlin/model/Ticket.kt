package model

class Ticket(
    val id: String,
    val sessionId: String,
    val seat: Int,
    val price: Double = 100.0,
    var isVisited : Boolean = false
) {
    override fun toString(): String {
        return "Ticket(id='$id', sessionId='$sessionId', seat=$seat, price=$price)"
    }
}