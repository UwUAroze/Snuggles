package me.aroze.snuggles.database.service

import me.aroze.snuggles.database
import kotlin.reflect.KClass

open class DatabaseCollection<T : Any>(
    val entity: KClass<T>,
    collectionName: String = generateCollectionName(entity.java)
) {

    val collection = database.getCollection(collectionName, entity.java)

}

private fun generateCollectionName(clazz: Class<*>): String {
    val className = clazz.simpleName.trim()
    val words = className.split("(?=[A-Z])".toRegex()).filter { it.isNotEmpty() }
    return words.joinToString(separator = "_") { it.lowercase() }
}
