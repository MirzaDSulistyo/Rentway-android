package id.rent.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.rent.android.model.User

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class
//        Product::class,
//        Favorite::class,
//        Box::class,
//        BoxObj::class,
//        Plan::class,
//        Subs::class,
//        Auth::class
       ],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

//    abstract fun userDao(): UserDao
//
//    abstract fun productDao(): ProductDao
//
//    abstract fun favoriteDao(): FavoriteDao
//
//    abstract fun planDao(): PlanDao
//
//    abstract fun boxDao(): BoxDao
//
//    abstract fun subsDao(): SubsDao
}