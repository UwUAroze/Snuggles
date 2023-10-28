package me.aroze.snuggles.database.models.features.generic.impl

import me.aroze.snuggles.database.models.features.generic.GenericFeature
import me.santio.coffee.common.annotations.ParserIgnore
import me.santio.coffee.jda.annotations.Description

class LoggingData: GenericFeature {
    @ParserIgnore
    var enabled: Boolean = false

    @setparam:Description("meow?")
    var logMessageChanges: Boolean = true
}