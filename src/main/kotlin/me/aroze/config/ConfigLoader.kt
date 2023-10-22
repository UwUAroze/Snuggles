package me.aroze.config

import com.akuleshov7.ktoml.Toml

object ConfigLoader {

    fun load() {
        Toml.decodeFromString<>("")
    }

}