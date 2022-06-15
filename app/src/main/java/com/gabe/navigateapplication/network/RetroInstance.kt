package com.gabe.navigateapplication.network

import com.gabe.navigateapplication.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {
    companion object{
//        private const val BaseURL ="https://rickandmortyapi.com/api/"


        fun getRetroInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
//                .cache(Cache(context.cacheDir, CACHE_SIZE))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }
}


