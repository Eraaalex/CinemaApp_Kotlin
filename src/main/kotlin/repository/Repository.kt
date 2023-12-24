package repository

import kotlinx.serialization.Serializable

interface Repository<T> {
    fun getAll(): List<T>
    fun add(entity: T)
    fun save(entities: MutableList<T>)
}