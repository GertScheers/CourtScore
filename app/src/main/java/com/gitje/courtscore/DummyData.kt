package com.gitje.courtscore

import com.gitje.courtscore.models.Game
import com.gitje.courtscore.models.Player
import com.gitje.courtscore.models.PlayerId
import com.gitje.courtscore.models.ScoreEvent
import com.gitje.courtscore.models.ScoreSnapshot
import com.gitje.courtscore.models.Sports
import java.time.LocalDateTime

fun getTennisScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now().minusYears(2), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(6, 2))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 4))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(2, Pair(6, 4))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(3, Pair(6, 4))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(4, Pair(6, 4))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(5, Pair(6, 4))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(6, Pair(6, 4))
                )
            ),
            PlayerId.P1,
            Sports.Tennis
        ),
        Game(
            LocalDateTime.now().minusYears(2), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(5, 7))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 4))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 3))
                )
            ),
            PlayerId.P1,
            Sports.Tennis
        ),
        Game(
            LocalDateTime.now().minusDays(2), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(4, 6))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(3, 6))
                )
            ),
            PlayerId.P2,
            Sports.Tennis
        ),
        Game(
            LocalDateTime.now(), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(1, 6))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(4, 6))
                )
            ),
            PlayerId.P2,
            Sports.Tennis
        ),
        Game(
            LocalDateTime.now(), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(7, 5))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 3))
                )
            ),
            PlayerId.P1,
            Sports.Tennis
        ),
        Game(
            LocalDateTime.now().minusDays(2), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(6, 2))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 4))
                )
            ),
            PlayerId.P1,
            Sports.Tennis
        )
    )
}

fun getPadelScoresDummyData(): List<Game> {
    return listOf(
        Game(
            LocalDateTime.now(), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(6, 2))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 4))
                )
            ),
            PlayerId.P1,
            Sports.Padel
        ),
        Game(
            LocalDateTime.now(), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(5, 7))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 4))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 3))
                )
            ),
            PlayerId.P1,
            Sports.Padel
        ),
        Game(
            LocalDateTime.now().minusDays(5), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(4, 6))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(3, 6))
                )
            ),
            PlayerId.P2,
            Sports.Padel
        ),
        Game(
            LocalDateTime.now().minusDays(5), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(1, 6))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(4, 6))
                )
            ),
            PlayerId.P2,
            Sports.Padel
        ),
        Game(
            LocalDateTime.now().minusDays(12), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(7, 5))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 3))
                )
            ),
            PlayerId.P1,
            Sports.Padel
        ),
        Game(
            LocalDateTime.now().minusDays(12), listOf(
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(0, Pair(6, 2))
                ),
                ScoreEvent(
                    Player("Gitje"),
                    ScoreSnapshot(1, Pair(6, 4))
                )
            ),
            PlayerId.P1,
            Sports.Padel
        )
    )
}

fun getBadmintonScoresDummyData(): List<Game> {
    return listOf(
        return listOf(
            Game(
                LocalDateTime.now().minusDays(1), listOf(
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(0, Pair(21, 16))
                    ),
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(1, Pair(21, 18))
                    )
                ),
                PlayerId.P1,
                Sports.Badminton
            ),
            Game(
                LocalDateTime.now().minusDays(1), listOf(
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(0, Pair(12, 21))
                    ),
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(1, Pair(21, 16))
                    ),
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(1, Pair(21, 19))
                    )
                ),
                PlayerId.P1,
                Sports.Badminton
            ),
            Game(
                LocalDateTime.now().minusDays(1), listOf(
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(0, Pair(18, 21))
                    ),
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(1, Pair(12, 21))
                    )
                ),
                PlayerId.P2,
                Sports.Badminton
            ),
            Game(
                LocalDateTime.now().minusDays(8), listOf(
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(0, Pair(5, 21))
                    ),
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(1, Pair(14, 21))
                    )
                ),
                PlayerId.P2,
                Sports.Badminton
            ),
            Game(
                LocalDateTime.now().minusDays(8), listOf(
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(0, Pair(25, 23))
                    ),
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(1, Pair(21, 15))
                    )
                ),
                PlayerId.P1,
                Sports.Badminton
            ),
            Game(
                LocalDateTime.now().minusDays(8), listOf(
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(0, Pair(21, 11))
                    ),
                    ScoreEvent(
                        Player("Gitje"),
                        ScoreSnapshot(1, Pair(21, 17))
                    )
                ),
                PlayerId.P1,
                Sports.Badminton
            )
        )
    )
}