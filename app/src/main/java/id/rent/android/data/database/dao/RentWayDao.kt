package id.rent.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.rent.android.model.RentWay

@Dao
abstract class RentWayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun inserts(data: List<RentWay>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(data: RentWay)

    @Query("SELECT * FROM rentway")
    abstract fun load(): LiveData<List<RentWay>>

    @Query("DELETE FROM rentway WHERE id = :id")
    abstract fun deleteById(id: Int)
}