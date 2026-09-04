package com.gitje.courtscore

import android.content.Context
import com.gitje.courtscore.logic.HistoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


val appModule = module {
    single(qualifier = named("default")) {
        androidContext().getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    single { HistoryViewModel(get(), get(qualifier = named("default"))) }
}