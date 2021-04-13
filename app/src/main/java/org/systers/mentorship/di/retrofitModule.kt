package org.systers.mentorship.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.systers.mentorship.remote.BaseUrl
import org.systers.mentorship.remote.CustomInterceptor
import org.systers.mentorship.remote.TokenAuthenticator
import org.systers.mentorship.remote.services.AuthService
import org.systers.mentorship.remote.services.RelationService
import org.systers.mentorship.remote.services.TaskService
import org.systers.mentorship.remote.services.UserService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class retrofitModule {

    /**
     * Provides the HttpLoggingInterceptor
     * This method is for hilt and should not be called.
     * */
    @Provides
    @Singleton
    fun providesInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    /**
     * Provides the okHttpClient
     * This method is for hilt and should not be called.
     * */
    @Singleton
    @Provides
    fun providesOkHttpClient(interceptor : HttpLoggingInterceptor ,
                             customInterceptor: CustomInterceptor ,
                             authenticator: TokenAuthenticator): OkHttpClient {
    return   OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(customInterceptor)
            .authenticator(authenticator)
            .build()
    }

    /**
     * Provides Gson Instance
     * This method is for hilt and should not be called.
     * */
    @Provides
    @Singleton
    fun providesGSON(): Gson {
    return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    /**
     * Provides Retrofit Instance
     * This method is for hilt and should not be called.
     * */
    @Provides
    @Singleton
    fun provideRetrofit(gson : Gson , okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .baseUrl(BaseUrl.apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    /**
     * Provides AuthService Instance
     * This method is for hilt and should not be called.
     * */
    @Provides
    @Singleton
    fun provideAuthService(retrofit : Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    /**
     * Provides UserService Instance
     * This method is for hilt and should not be called.
     * */
    @Provides
    @Singleton
    fun provideUserService(retrofit : Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    /**
     * Provides TaskService Instance
     * This method is for hilt and should not be called.
     * */
    @Provides
    @Singleton
    fun provideTaskService(retrofit : Retrofit): TaskService {
        return retrofit.create(TaskService::class.java)
    }

    /**
     * Provides RelationService Instance
     * This method is for hilt and should not be called.
     * */
    @Provides
    @Singleton
    fun provideRelationService(retrofit: Retrofit): RelationService {
        return retrofit.create(RelationService::class.java)
    }


}