package com.gitje.courtscore.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.gitje.courtscore.R
import com.gitje.courtscore.sharedclasses.Game
import com.gitje.courtscore.sharedclasses.GameType

fun <K, V> MutableMap<K, List<V>>.mergeGames(other: Map<K, List<V>>) {
    for ((key, values) in other) {
        put(key, getOrDefault(key, emptyList()) + values)
    }
}

@Composable
fun Game.getIcon(): ImageVector {
    return ImageVector.vectorResource(
        when (sport) {
            GameType.Tennis -> R.drawable.ic_tennis
            GameType.Padel -> R.drawable.ic_padel
            else -> R.drawable.ic_badminton
        }
    )
}