package com.gitje.courtscore

import com.gitje.courtscore.sharedclasses.Game
import com.gitje.courtscore.sharedclasses.GameType
import java.time.LocalDateTime

fun getTennisScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(),
            emptyList(),
            1,
            GameType.Tennis
        )
    )
}


fun getPadelScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(),
            emptyList(),
            2,
            GameType.Padel
        )
    )
}


fun getBadmintonScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(),
            emptyList(),
            1,
            GameType.Badminton
        )
    )
}