package id.rent.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.rent.android.data.database.dao.*
import id.rent.android.model.*

/**
 * Main database description.
 */
@Database(
    entities = [
        Auth::class,
        User::class,
        Product::class,
        PaymentType::class,
        RentWay::class,
        Category::class,
        Master::class,
        Favorite::class
    ],
    version = 10,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun productDao(): ProductDao

    abstract fun rentWayDao(): RentWayDao

    abstract fun paymentTypeDao(): PaymentTypeDao

    abstract fun categoryDao(): CategoryDao

    abstract fun masterDao(): MasterDao

    abstract fun favoriteDao(): FavoriteDao

}