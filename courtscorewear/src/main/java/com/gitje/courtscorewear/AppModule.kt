package com.gitje.courtscorewear

import com.gitje.courtscorewear.logic.BadmintonViewModel
import org.koin.dsl.module

val appModule = module {
    single { BadmintonViewModel(get()) }
}