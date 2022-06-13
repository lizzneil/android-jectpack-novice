package com.gabe.navigateapplication.di

import android.app.Application
import com.gabe.navigateapplication.GabeDatabase
import com.gabe.navigateapplication.db.PersonDataSource
import com.gabe.navigateapplication.db.PersonDataSourceImpl
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModel {

    @Provides
    @Singleton
    fun providerSqlDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = GabeDatabase.Schema,
            context = app,
            name = "gabe.db"
        )
    }

    @Provides
    @Singleton
    fun providerPersonDataSource(sqlDriver: SqlDriver): PersonDataSource {
        return PersonDataSourceImpl(GabeDatabase(sqlDriver))
    }


}