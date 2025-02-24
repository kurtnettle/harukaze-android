package com.kurtnettle.harukaze

import android.app.Application
import com.kurtnettle.harukaze.di.baseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

/*
Whenever we change the theme, MainActivity() is recreated
and I was at first starting Koin in there so my app was crashing.
I was not understanding why at first.
Although the error message was saying "A Koin Application has already been started"
I was searching at stumbled upon this issue
https://github.com/InsertKoinIO/koin/issues/1840

But you know my intelligence is far away from me.
I didn't understand how to do this Application() thing.
So I went on the YT started looking for the most recent videos and got
https://www.youtube.com/watch?v=MZ6dIoTiFCI (@YoursSohailYT)
Copy pasting from there I got my app up and running :D
*/
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(baseModule)
        }
    }
}