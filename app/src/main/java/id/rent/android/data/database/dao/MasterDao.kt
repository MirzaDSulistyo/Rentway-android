package id.rent.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.rent.android.model.Master

@Dao
abstract class MasterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun inserts(data: Master)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(data: Master)

    @Query("SELECT * FROM master")
    abstract fun load(): LiveData<Master>
}