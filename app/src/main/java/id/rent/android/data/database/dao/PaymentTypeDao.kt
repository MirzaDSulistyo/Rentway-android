package id.rent.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.rent.android.model.PaymentType

@Dao
abstract class PaymentTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPayments(payments: List<PaymentType>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(payment: PaymentType)

    @Query("SELECT * FROM paymenttype")
    abstract fun load(): LiveData<List<PaymentType>>

    @Query("DELETE FROM paymenttype WHERE id = :id")
    abstract fun deleteById(id: Int)
}