package me.aroze.snuggles.database.models.features.generic.impl

import me.aroze.snuggles.database.models.features.generic.GenericFeature

data class LoggingData(
    var logMessageChanges: Boolean = true
): GenericFeature(
    "Log message changes" to logMessageChanges
)