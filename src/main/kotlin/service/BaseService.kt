package service

interface BaseService<T> {
    fun add(element: T)
    fun update(element: T)
    fun remove(element: T)
}