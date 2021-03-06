package com.gabe.navigateapplication.di

import android.app.Application
import android.content.Context
import com.gabe.navigateapplication.BuildConfig
import com.gabe.navigateapplication.GabeDatabase
import com.gabe.navigateapplication.db.PersonDataSource
import com.gabe.navigateapplication.db.PersonDataSourceImpl
import com.gabe.navigateapplication.network.RetroService
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModel {

    private const val CACHE_SIZE = 20 * 1024 * 1024 // 20 MB
        .toLong()

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

    @Provides
    @Singleton
    fun provideServerApi(
        @ApplicationContext context: Context,
        okHttpBuilder: OkHttpClient.Builder
    ): RetroService {
        val okHttp = okHttpBuilder
            .cache(Cache(context.cacheDir, CACHE_SIZE))
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request = chain.request()
//                val linkHeader =
//                    request.header(HEADER_LINK)
                val response = chain.proceed(request)
                response
            }).build()
//        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder()
//            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttp)
            .build()
        return retrofit.create(RetroService::class.java)
    }

}


