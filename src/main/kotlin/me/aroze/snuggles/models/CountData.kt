package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore

data class CountData(
    var lastCounter: String = "",
    var count: Int = 0,
    var highScore: Int = 0,

    var disabled: Boolean = false,

    var allowConsecutiveUsers: Boolean = false,
    var allowTalking: Boolean = true,
    var kinderMessages: Boolean = false,
    var warnForDeletedCounts: Boolean = true,
    var warnForEditedCounts: Boolean = true,
) {

    @JsonIgnore
    fun resetCurrentCount() {
        count = 0
        lastCounter = ""
    }

    @JsonIgnore
    fun getSelectedOptions() : List<String> {
        val options = mutableListOf<String>()
        if (allowConsecutiveUsers) options.add("Consecutive counting")
        if (allowTalking) options.add("Allow speaking")
        return options
    }

}
