package service

import model.Film
import exception.NotFoundException
import repository.Repository
import java.util.Date

interface FilmService : BaseService<Film> {
    fun getFilmsByDate(date: Date): List<Film>
    fun getById(id: String): Film?
    fun getAll(): List<Film>
    fun getFilmByNameAndDay(name: String, date: Date): Film?
}

class FilmServiceImpl(private val filmRepository: Repository<Film>) : FilmService {

    private val films: MutableList<Film> = filmRepository.getAll().toMutableList()
    override fun getFilmsByDate(date: Date): List<Film> {
        return films.filter { it.rentalDates.first >= date && it.rentalDates.second <= date }
    }

    override fun getById(id: String): Film? {
        return films.find { it.id == id }
    }

    override fun getAll(): List<Film> {
        return films
    }

    override fun getFilmByNameAndDay(name: String, date: Date): Film? {
        return films.find { it.name == name && it.rentalDates.first <= date && date <= it.rentalDates.second }
    }

    override fun add(film: Film) {
        films.add(film)
        filmRepository.save(films)
    }

    private fun findFilmIndex(film: Film): Int {
        val existingElementIndex = films.indexOfFirst { it.id == film.id }
        if (existingElementIndex == -1) {
            throw NotFoundException("Element with ID ${film.id} not found.")
        }
        return existingElementIndex
    }

    override fun update(film: Film) {
        val existingElementIndex = findFilmIndex(film)
        films[existingElementIndex] = film
        filmRepository.save(films)
    }

    override fun remove(film: Film) {
        val existingElementIndex = findFilmIndex(film)
        films.removeAt(existingElementIndex)
        filmRepository.save(films)
    }


}