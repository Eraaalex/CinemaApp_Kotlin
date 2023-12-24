import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import model.Film
import model.Session
import model.Ticket
import exception.ExceptionHandler
import extention.toRed
import presentation.CinemaController
import presentation.handler.InputHandler
import service.*
import java.io.File


fun main(args: Array<String>) {
    val inputHandler = InputHandler()

    val cinemaController = initializeServicesAndControllers(inputHandler, args)

    while (true) {
        println("1. View seats") // Посмотреть места на сеансе (3 пункт в задании)
        println("2. Buy tickets") // Зафиксировать билеты (1 пункт в задании)
        println("3. Return tickets") // Вернуть билет (2 пункт в задании)
        println("4. Edit film") // Редактировать информацию о фильме (4 пункт в задании)
        println("5. Edit sessions") // Редактировать информацию о сеансах (4 пункт в задании)
        println("6. Mark a visit") // Отметка занятых мест в зале (5 пункт в задании)

        println("7. View all films")// Опционально, для удобства тестирования
        println("8. View all sessions")// Опционально, для удобства тестирования

        println("9. Add Film")
        println("10. Add Session")
        println("11. Delete Film")
        println("12. Delete Session")

        println("13. Exit") // Выход


        var answer: String = readLine()!!

        when (answer) {
            "1" -> {
                try {
                    val filmName = inputHandler.getFilmName()
                    val sessionTime = inputHandler.getSessionTime()
                    cinemaController.displaySeats(filmName, sessionTime)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                } catch (e: java.text.ParseException) {
                    ExceptionHandler.handleException(e)
                }

            }

            "2" -> {
                try {
                    val filmName = inputHandler.getFilmName()
                    val sessionTime = inputHandler.getSessionTime()
                    val seats = inputHandler.getSeats()
                    cinemaController.buyTickets(filmName, sessionTime, seats)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                }
            }

            "3" -> {

                try {
                    val filmName = inputHandler.getFilmName()
                    val sessionTime = inputHandler.getSessionTime()
                    val seats = inputHandler.getSeats()
                    cinemaController.returnTickets(filmName, sessionTime, seats)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                }
            }

            "4" -> {
                try {
                    val filmId = inputHandler.getFilmId()
                    println("Input NEW data: ")
                    val newFilmName = inputHandler.getFilmName()
                    val newFilmDirector = inputHandler.getFilmDirector()
                    val newFilmDuration = inputHandler.getFilmDuration()
                    cinemaController.updateFilm(filmId, newFilmName, newFilmDuration, newFilmDirector)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                }
            }

            "5" -> {
                try {
                    val filmName = inputHandler.getFilmId()
                    val sessionTime = inputHandler.getSessionTime()

                    cinemaController.updateSession(filmName, sessionTime)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                }
            }

            "6" -> {
                try {
                    val filmName = inputHandler.getFilmName()
                    val sessionDate = inputHandler.getSessionTime()
                    val seats = inputHandler.getSeats()
                    cinemaController.markVisit(filmName, sessionDate, seats)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                }

            }

            "7" -> {
                cinemaController.displayAllFilms()
            }

            "8" -> {
                cinemaController.displayAllSessions()
            }

            "9" -> {
                try {
                    val filmName = inputHandler.getFilmName()
                    val filmDirector = inputHandler.getFilmDirector()
                    val durationInMinutes = inputHandler.getFilmDuration()
                    val rentalDates = inputHandler.getRentalDates()
                    cinemaController.addFilm(filmName, filmDirector, durationInMinutes, rentalDates)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                }
            }

            "10" -> {
                try {
                    val filmName = inputHandler.getFilmName()
                    val sessionTime = inputHandler.getSessionTime()
                    val seatsNumber = inputHandler.getSeatsNumber()
                    cinemaController.addSession(filmName, sessionTime, seatsNumber)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                }
            }

            "11" -> {
                try {
                    val filmName = inputHandler.getFilmName()
                    val sessionTime = inputHandler.getSessionTime()
                    cinemaController.deleteFilm(filmName, sessionTime)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                }
            }


            "12" -> {
                try {
                    val filmName = inputHandler.getFilmName()
                    val sessionTime = inputHandler.getSessionTime()
                    cinemaController.deleteSession(filmName, sessionTime)
                } catch (e: RuntimeException) {
                    ExceptionHandler.handleException(e)
                }
            }

            "13" -> {
                break
            }

            else -> {
                ExceptionHandler.handleException(RuntimeException("Wrong input"))
            }

        }
    }
}

fun initializeServicesAndControllers(
    inputHandler: InputHandler,
    args: Array<String>,
    isManualInput: Boolean = false
): CinemaController {
    val filePathForFilm: String
    val filePathForSession: String
    val filePathForTicket: String
    val filmService: FilmService
    val sessionService: SessionService
    val ticketService: TicketService

    if (args.size == 3 && !isManualInput) {
        filePathForFilm = args[0]
        filePathForSession = args[1]
        filePathForTicket = args[2]
    } else {
        filePathForFilm = inputHandler.getFilePath("Film")
        filePathForSession = inputHandler.getFilePath("Sessions:")
        filePathForTicket = inputHandler.getFilePath("Tickets:")
    }

    try {
        filmService = FilmServiceImpl(
            JsonRepository(
                File(filePathForFilm),
                object : TypeToken<List<Film>>() {}
            )
        )
        sessionService = SessionServiceImpl(
            JsonRepository(
                File(filePathForSession),
                object : TypeToken<List<Session>>() {})
        )

        ticketService = TicketServiceImpl(
            sessionService,
            JsonRepository(
                File(filePathForTicket),
                object : TypeToken<List<Ticket>>() {})
        )
    } catch (e: JsonSyntaxException) {
        println("Error in deserializing the file. Please enter a different file name.".toRed())
        return initializeServicesAndControllers(inputHandler, args, true)
    } catch (e: RuntimeException) {
        ExceptionHandler.handleException(e)
        return initializeServicesAndControllers(inputHandler, args, true)
    }

    return CinemaController(
        CinemaServiceImpl(
            ticketService,
            filmService,
            sessionService
        )
    )
}

