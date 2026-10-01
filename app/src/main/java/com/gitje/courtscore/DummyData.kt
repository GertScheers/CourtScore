package com.gitje.courtscore

import com.gitje.courtscore.sharedclasses.Game
import com.gitje.courtscore.sharedclasses.GameType
import com.gitje.courtscore.sharedclasses.ScoreEvent
import com.gitje.courtscore.sharedclasses.ScoreSnapshot
import java.time.LocalDateTime

fun getTennisScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(),
            listOf(
                ScoreEvent(
                    0,
                    ScoreSnapshot(0, Pair(5, 0))
                ),
            ),
            1,
            GameType.Tennis
        )
    )
}


fun getPadelScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(),
            listOf(
                ScoreEvent(
                    0,
                    ScoreSnapshot(0, Pair(5, 0))
                ),
            ),
            2,
            GameType.Padel
        )
    )
}

fun getBadmintonScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(),
            listOf(
                ScoreEvent(
                    0,
                    ScoreSnapshot(0, Pair(21, 12))
                ),
                ScoreEvent(
                    0,
                    ScoreSnapshot(1, Pair(21, 16))
                ),
            ),
            0,
            GameType.Badminton
        ),
        Game(
            LocalDateTime.now(),
            listOf(
                ScoreEvent(
                    0,
                    ScoreSnapshot(0, Pair(12, 21))
                ),
                ScoreEvent(
                    0,
                    ScoreSnapshot(1, Pair(16, 21))
                ),
            ),
            1,
            GameType.Badminton
        ),
        Game(
            LocalDateTime.now(),
            listOf(
                ScoreEvent(
                    0,
                    ScoreSnapshot(0, Pair(21, 12))
                ),
                ScoreEvent(
                    0,
                    ScoreSnapshot(1, Pair(10, 21))
                ),
                ScoreEvent(
                    0,
                    ScoreSnapshot(2, Pair(21, 16))
                ),
            ),
            1,
            GameType.Badminton
        ),
    )
}