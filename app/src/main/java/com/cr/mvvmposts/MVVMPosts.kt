package com.cr.mvvmposts

import android.app.Application
import com.cr.mvvmposts.di.appModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MVVMPosts : Application() {

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())

        startKoin {
            androidContext(this@MVVMPosts)
            modules(
                appModule
            )
        }

    }
}