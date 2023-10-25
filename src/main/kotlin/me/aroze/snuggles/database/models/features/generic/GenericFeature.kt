package me.aroze.snuggles.database.models.features.generic

import org.bson.codecs.pojo.annotations.BsonIgnore

open class GenericFeature(
    @BsonIgnore private vararg val _settings: Pair<String, Boolean>,
    var enabled: Boolean = false
) {

    @BsonIgnore private val settings = _settings.toMap()

    fun getSelectedOptions() = settings.filter { it.value }.map { it.key }

}