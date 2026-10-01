package com.gitje.courtscore

import com.gitje.courtscore.models.Game
import com.gitje.courtscore.models.GameType
import java.time.LocalDateTime

fun getTennisScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(),
            listOf(6, 6),
            listOf(3, 4),
            1,
            GameType.Tennis
        )
    )
}


fun getPadelScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(),
            listOf(2,6,2),
            listOf(6,2,6),
            2,
            GameType.Padel
        )
    )
}


fun getBadmintonScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(),
            listOf(18,21,21),
            listOf(21,12,18),
            1,
            GameType.Badminton
        )
    )
}