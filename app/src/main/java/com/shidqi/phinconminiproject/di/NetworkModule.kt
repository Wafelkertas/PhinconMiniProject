package com.shidqi.phinconminiproject.di

import android.content.Context
import com.shidqi.phinconminiproject.network.NetworkConnectionInterceptor
import com.shidqi.phinconminiproject.service.MyServerRetrofitServerInstance
import com.shidqi.phinconminiproject.service.PokeApiRetrofitInstance
import com.shidqi.phinconminiproject.utils.BASE_URL
import com.shidqi.phinconminiproject.utils.MY_SERVER_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Providing retrofit instance for repositories to use
     *  **/
    @Singleton
    @Provides
    fun providePokeapiRetrofitService(@ApplicationContext context : Context ): PokeApiRetrofitInstance {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().connectTimeout(150, TimeUnit.SECONDS).readTimeout(150, TimeUnit.SECONDS)
            .addInterceptor(interceptor).addInterceptor(NetworkConnectionInterceptor(context))
            .addInterceptor(Interceptor {
                val builder = it
                    .request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")



                return@Interceptor it.proceed(
                    builder.build()
                )
            })
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build().create(PokeApiRetrofitInstance::class.java)
    }

    @Singleton
    @Provides
    fun provideMyServerRetrofitService(@ApplicationContext context : Context ): MyServerRetrofitServerInstance {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().connectTimeout(150, TimeUnit.SECONDS).readTimeout(150, TimeUnit.SECONDS)
            .addInterceptor(interceptor).addInterceptor(NetworkConnectionInterceptor(context))
            .addInterceptor(Interceptor {
                val builder = it
                    .request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")



                return@Interceptor it.proceed(
                    builder.build()
                )
            })
            .build()


        return Retrofit.Builder()
            .baseUrl(MY_SERVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build().create(MyServerRetrofitServerInstance::class.java)
    }


}

