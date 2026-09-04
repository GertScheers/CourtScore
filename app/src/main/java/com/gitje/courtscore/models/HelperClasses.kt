package com.gitje.courtscore.models


enum class PlayerId { P1, P2 }
enum class Sports { Tennis, Padel, Badminton }

class Player(val name: String) {}

data class ScoreEvent(
    val scoringPlayer: Player,
    val scoreAfter: ScoreSnapshot,
    val timestamp: Long = System.currentTimeMillis()
)

data class ScoreSnapshot(
    val set: Int,
    val points: Pair<Int, Int>
)