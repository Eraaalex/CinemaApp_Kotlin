package model
import java.util.Date

class Film(
    val id: String,
    val name: String,
    val durationInMinutes: Int,
    val director: String,
    val rentalDates: Pair<Date, Date>,
) {
    override fun toString(): String {
        return "[Film] id='$id', name='$name', durationInMinutes=$durationInMinutes, director='$director'"
    }
}


