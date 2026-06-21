package com.gitje.courtscorewear

import android.content.Context
import com.gitje.courtscorewear.logic.BadmintonViewModel
import com.gitje.courtscorewear.logic.SettingsViewModel
import com.gitje.courtscorewear.logic.TennisPadelViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(qualifier = named("default")) {
        androidContext().getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    single { BadmintonViewModel(get(), get(qualifier = named("default"))) }
    single { TennisPadelViewModel(get(), get(qualifier = named("default"))) }
    single { SettingsViewModel(get(), get(qualifier = named("default"))) }
}