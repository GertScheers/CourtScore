package com.gitje.courtscorewear.util

fun Int.getTennisScore(): String {
    return when (this) {
        0 -> "0"
        1 -> "15"
        2 -> "30"
        3 -> "40"
        else -> "ADV"
    }
}