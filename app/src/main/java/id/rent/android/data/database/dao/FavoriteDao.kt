package id.rent.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.rent.android.model.Favorite

@Dao
abstract class FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: List<Favorite>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(data: Favorite)

    @Query("SELECT * FROM favorite")
    abstract fun load(): LiveData<List<Favorite>>

    @Query("DELETE FROM favorite WHERE id = :id")
    abstract fun deleteById(id: String)
}