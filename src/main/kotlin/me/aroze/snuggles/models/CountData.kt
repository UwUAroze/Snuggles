package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import me.aroze.snuggles.database.database
import org.litote.kmongo.*

data class CountData(
    var count: Int = 0,
    var highScore: Int = 0,

    var lastCounter: String = "",
    var lastBotMessage: String = "",

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

    companion object {
        fun getTopUsers(guild: String? = null): List<Result> {
            println(guild)
            val collection = database.getCollection<UserData>()
            return if (guild == null) {
                collection.aggregate<Result>(
                    group(
                        UserData::id,
                        UserData::totalCounts sum "\$totalCounts"
                    ),
                    sort(descending(UserData::totalCounts)),
                    limit(12)
                )
            } else {
                collection.aggregate<Result>(
                    match(UserData::guild eq guild),
                    group(
                        UserData::id,
                        UserData::totalCounts sum "\$totalCounts"
                    ),
                    sort(descending(UserData::totalCounts)),
                    limit(12)
                )
            }.toList()
        }

        fun getTopGuildsByCounts(): List<Result> {
            val collection = database.getCollection<UserData>()
            return collection.aggregate<Result>(
                group(
                    UserData::guild,
                    UserData::totalCounts sum "\$totalCounts"
                ),
                sort(descending(UserData::totalCounts)),
                limit(5)
            ).toList()
        }

        fun getTopGuildsByHighScores(): List<ResultHighScore> {
            val collection = database.getCollection<ChannelData>()
            return collection.aggregate<ResultHighScore>(
                match(ChannelData::counting ne null),
                group(
                    ChannelData::guild,
                    CountData::highScore sum "\$counting.highScore"
                ),
                sort(descending(CountData::highScore)),
                limit(5)
            ).toList()
        }
    }

    data class Result(
        val _id: String = "",
        val totalCounts: Int = 0
    )

    data class ResultHighScore(
        val _id: String = "",
        val highScore: Int = 0
    )

}
