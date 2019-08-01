package id.rent.android.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import id.rent.android.data.api.ApiService
import id.rent.android.data.database.dao.ProductDao
import id.rent.android.data.database.dao.UserDao
import id.rent.android.data.database.AppDb
import id.rent.android.utility.LiveDataCallAdapterFactory
import id.rent.android.utility.Vars
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    /********
     * URLS
     */
    private val LOCAL = Vars.getLocale()

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.43.216:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room
            .databaseBuilder(app, AppDb::class.java, "rent.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideProductDao(db: AppDb): ProductDao {
        return db.productDao()
    }

}