package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore

data class LogData(
    var id: String,
    val guild: String,

    var disabled: Boolean = false,

    var logMessageChanges: Boolean = true,
) {

    @JsonIgnore
    fun getSelectedOptions() : List<String> {
        val options = mutableListOf<String>()
        if (logMessageChanges) options.add("Log message changes")
        return options
    }

}
