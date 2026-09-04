package com.gitje.courtscore.utils


fun <K, V> MutableMap<K, List<V>>.mergeGames(other: Map<K, List<V>>) {
    for ((key, values) in other) {
        put(key, getOrDefault(key, emptyList()) + values)
    }
}