package com.gitje.courtscore

import java.time.LocalDateTime

fun getTennisScoresDummyData(): List<Game> {
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
            Sports.Tennis
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
            Sports.Tennis
        ),
        Game(
            LocalDateTime.now(), listOf(
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
            Sports.Tennis
        )
    )
}

fun getPaddleScoresDummyData(): List<Game> {
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
            Sports.Paddle
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
            Sports.Paddle
        ),
        Game(
            LocalDateTime.now(), listOf(
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
            Sports.Paddle
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
            Sports.Paddle
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
            Sports.Paddle
        ),
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
            Sports.Paddle
        )
    )
}

fun getBadmintonScoresDummyData(): List<Game> {
    return listOf(
        return listOf(
            Game(
                LocalDateTime.now(), listOf(
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
                LocalDateTime.now(), listOf(
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
                LocalDateTime.now(), listOf(
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
                LocalDateTime.now(), listOf(
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
                LocalDateTime.now(), listOf(
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
                LocalDateTime.now(), listOf(
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