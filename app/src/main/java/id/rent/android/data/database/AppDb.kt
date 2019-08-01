package id.rent.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.rent.android.data.database.dao.ProductDao
import id.rent.android.data.database.dao.UserDao
import id.rent.android.model.Auth
import id.rent.android.model.Product
import id.rent.android.model.User

/**
 * Main database description.
 */
@Database(
    entities = [
        Auth::class,
        User::class,
        Product::class
       ],
    version = 3,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun productDao(): ProductDao

}