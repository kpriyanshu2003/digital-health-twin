package nxt.abhranil.dhtapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nxt.abhranil.dhtapp.data.remote.DHTApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApi(): DHTApi {
        return Retrofit.Builder()
            .baseUrl("https://right-keen-antelope.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DHTApi::class.java)
    }
}