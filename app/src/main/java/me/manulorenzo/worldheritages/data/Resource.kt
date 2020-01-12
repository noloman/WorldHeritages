package me.manulorenzo.worldheritages.data

/**
 * Sealed to help encapsulate the data modeling it with the different states it can have.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String) : Resource<T>(null, message)
}