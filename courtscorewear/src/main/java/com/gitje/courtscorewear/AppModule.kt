package com.gitje.courtscorewear

import com.gitje.courtscorewear.logic.BadmintonViewModel
import com.gitje.courtscorewear.logic.BaseViewModel
import com.gitje.courtscorewear.logic.TennisPadelViewModel
import org.koin.dsl.module

val appModule = module {
    single { BadmintonViewModel(get()) }
    single { TennisPadelViewModel(get()) }
}