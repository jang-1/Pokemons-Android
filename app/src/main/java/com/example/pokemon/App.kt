package com.example.pokemon

import android.app.Application
import com.example.pokemon.module.pokemonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(pokemonModule)
        }
    }
}