package me.aroze.snuggles.utils

fun <T: Any> Class<T>.safeConstruct(): T? {
    return try {
        this.getDeclaredConstructor().newInstance()
    } catch(_:Exception) { null }
}