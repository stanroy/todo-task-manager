package com.stanroy

import android.app.Application
import android.os.Build
import com.stanroy.todotaskmanager.BuildConfig
import com.stanroy.todotaskmanager.di.DatabaseModule
import com.stanroy.todotaskmanager.di.RepositoryModule
import com.stanroy.todotaskmanager.di.UseCaseModule
import com.stanroy.todotaskmanager.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class TaskManagerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TaskManagerApp)

            koin.loadModules(
                listOf(
                    DatabaseModule.provider,
                    RepositoryModule.provider,
                    UseCaseModule.provider,
                    ViewModelModule.provider
                )
            )
        }
    }

}