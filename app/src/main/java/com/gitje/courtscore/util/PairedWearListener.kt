package com.gitje.courtscore.util

import com.gitje.courtscore.sharedclasses.Game
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.serialization.json.Json

class PairedWearListener : WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)

        for (event in dataEvents) {
            // Process only changed or created DataItems
            if (event.type == DataEvent.TYPE_CHANGED) {
                val item = event.dataItem

                if (item.uri.path == "/game_result") {
                    val dataMap = DataMapItem.fromDataItem(item).dataMap
                    val json = dataMap.getString("gameResult") ?: return
                    try {
                        val result = Json.decodeFromString<Game>(json)
                        // TODO: Handle and store the data in a Room db
                    } catch(ex: Exception) {
                        // TODO: Log error in firebase with details
                        println("${ex.message}")
                    }
                }
            }
        }
    }
}