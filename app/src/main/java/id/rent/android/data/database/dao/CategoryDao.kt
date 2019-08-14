package id.rent.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.rent.android.model.Category

@Dao
abstract class CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun inserts(data: List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(data: Category)

    @Query("SELECT * FROM category")
    abstract fun load(): LiveData<List<Category>>

    @Query("DELETE FROM category WHERE id = :id")
    abstract fun deleteById(id: Int)
}