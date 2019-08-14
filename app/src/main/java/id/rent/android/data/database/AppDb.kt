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
        Master::class
    ],
    version = 9,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun productDao(): ProductDao

    abstract fun rentWayDao(): RentWayDao

    abstract fun paymentTypeDao(): PaymentTypeDao

    abstract fun categoryDao(): CategoryDao

    abstract fun masterDao(): MasterDao

}