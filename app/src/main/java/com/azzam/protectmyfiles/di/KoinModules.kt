package com.azzam.protectmyfiles.di

import androidx.room.Room
import com.azzam.protectmyfiles.data.local.MainDataBase
import com.azzam.protectmyfiles.data.repository.RepositoryImpl
import com.azzam.protectmyfiles.data.sharedPref.SharedPrefs
import com.azzam.protectmyfiles.data.sharedPref.SharedPrefsImpl
import com.azzam.protectmyfiles.domain.repository.Repository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.*
import org.koin.dsl.module
import com.azzam.protectmyfiles.presentation.keypad.KeypadViewModel
import com.azzam.protectmyfiles.presentation.splash.SplashViewModel
import com.azzam.protectmyfiles.presentation.home.HomeViewModel
import com.azzam.protectmyfiles.presentation.encryptedFileList.EncryptedFileListViewModel
import com.azzam.protectmyfiles.presentation.savedPasswords.SavedPasswordsViewModel
import com.azzam.protectmyfiles.util.DATABASE_NAME


val repositoryModule = module {
    single { SharedPrefsImpl(androidContext()) as SharedPrefs }
    single {
        Room.databaseBuilder(androidContext(), MainDataBase::class.java, DATABASE_NAME)
            .build()
            .getMainDao()
    }
    single { RepositoryImpl(get(), get()) as Repository }
}


val viewModelModule = module {
    viewModelOf(::KeypadViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::EncryptedFileListViewModel)
    viewModelOf(::SavedPasswordsViewModel)
}
