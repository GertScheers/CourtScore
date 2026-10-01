package com.gitje.courtscorewear.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.serialization.json.Json
import com.gitje.courtscore.sharedclasses.Game

class WearDataSyncer(private val context: Context) {
    @SuppressLint("VisibleForTests")
    fun syncDataToPhone(game: Game): Boolean {
        return try {
            // 1. Create a PutDataMapRequest with a unique path
            val dataMapRequest = PutDataMapRequest.create("/game_result").apply {
                dataMap.putString("gameResult", Json.encodeToString(game))
                // Always add a timestamp so DataClient detects a change and triggers sync
                dataMap.putLong("timestamp", System.currentTimeMillis())
            }
            val request = dataMapRequest.asPutDataRequest().setUrgent()
            Wearable.getDataClient(context).putDataItem(request)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}