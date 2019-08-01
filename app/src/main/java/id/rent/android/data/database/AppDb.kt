package id.rent.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.rent.android.data.dao.UserDao
import id.rent.android.model.Auth
import id.rent.android.model.User

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class,
        Auth::class
       ],
    version = 2,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

}