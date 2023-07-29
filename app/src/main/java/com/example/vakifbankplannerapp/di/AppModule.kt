package com.example.vakifbankplannerapp.di


import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import com.example.vakifbankplannerapp.data.service.PlannerService
import com.example.vakifbankplannerapp.domain.util.Constant.BASE_URL_FOR_LOGIN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val inceptor = HttpLoggingInterceptor()
    val inceptorLogging= inceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder().addInterceptor(inceptorLogging).build()

    @Singleton
    @Provides
    fun providePlannerRepository(
        api : PlannerService
    ) = PlannerRepository(api)

    @Provides
    @Singleton
    fun providePlannerApi() : PlannerService{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_FOR_LOGIN)
            .client(client)
            .build()
            .create(PlannerService::class.java)
    }

}