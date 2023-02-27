package me.aroze.snuggles.utils

import java.lang.reflect.Method
import java.lang.reflect.Parameter
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.kotlinFunction

fun <T: Any> Class<T>.safeConstruct(): T? {
    return try {
        this.getDeclaredConstructor().newInstance()
    } catch(_:Exception) { null }
}

fun Parameter.kotlinParameter(method: Method): KParameter {
    val index = method.parameters.indexOf(this)
    return method.kotlinFunction!!.parameters[index + 1]
}