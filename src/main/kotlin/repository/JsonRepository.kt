import repository.Repository
import java.io.File

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import utils.exception.FileException
import java.io.FileNotFoundException
import java.io.IOException


class JsonRepository<T>(private val file: File, private val typeToken: TypeToken<List<T>>) : Repository<T> {
    private val gson = Gson()

    override fun getAll(): List<T> {
        val listType = typeToken.type
        try {
            val allText : String = file.readText()
            return gson.fromJson(allText, listType)
        } catch (e: FileNotFoundException) {
            throw FileException(file, "Failed to read file", e)
        } catch (e: IOException) {
            throw FileException(file, "Error occurred while reading file", e)
        }
    }

    override fun save(entities: MutableList<T>) {
        try {
            file.writeText(gson.toJson(entities))
        } catch (e: FileNotFoundException) {
            throw FileException(file, "File not found", e)
        } catch (e: IOException) {
            throw FileException(file, "Error occurred while creating file", e)
        }
    }

    override fun add(entity: T) {
        val entities = getAll().toMutableList()
        entities.add(entity)
        save(entities)
    }

}
