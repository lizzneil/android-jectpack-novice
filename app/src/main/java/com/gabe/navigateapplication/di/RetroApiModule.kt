//package com.gabe.navigateapplication.di
//
//import android.content.Context
//import com.gabe.navigateapplication.BuildConfig
//import com.gabe.navigateapplication.network.RetroService
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import okhttp3.Cache
//import okhttp3.Interceptor
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import javax.inject.Named
//import javax.inject.Singleton
//
//private const val CACHE_SIZE = 20 * 1024 * 1024 // 20 MB
//    .toLong()
//
//@Module
//@InstallIn(SingletonComponent::class)
//object RetroApiModule {
//    @Provides
////    @Singleton
//    @Named("apiModel")
//    fun provideRetroApi(
//        @ApplicationContext context: Context
//        ,
//        okHttpBuilder: OkHttpClient.Builder
//    ): RetroService {
//        val okHttpBuilder  = OkHttpClient.Builder()
//        val okHttp = okHttpBuilder
//            .cache(Cache(context.cacheDir, CACHE_SIZE))
//            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
//                val request = chain.request()
////                val linkHeader =
////                    request.header(HEADER_LINK)
//                val response = chain.proceed(request)
//                response
//            }).build()
////        val moshi = Moshi.Builder().build()
//        val retrofit = Retrofit.Builder()
////            .addConverterFactory(ScalarsConverterFactory.create())
////            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create( ))
//            .baseUrl(BuildConfig.BASE_URL)
//            .client(okHttp)
//            .build()
//        return retrofit.create(RetroService::class.java)
//    }
//
//
//}