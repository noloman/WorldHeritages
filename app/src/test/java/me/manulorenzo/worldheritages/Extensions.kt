package me.manulorenzo.worldheritages

/**
 * Since Kotlin has reified types, we can take advantage of it and test the type of the instance
 */
inline fun <reified T> tryCast(instance: Any?, block: T.() -> Unit) {
    if (instance is T) block(instance)
}