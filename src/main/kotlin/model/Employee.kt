package model
import service.CinemaServiceImpl


class Employee (
    val id : String,
    val login : String,
    val password : String,
    private val cinemaServiceImpl: CinemaServiceImpl
)