package id.rent.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.rent.android.model.PaymentType
import java.util.*

object PaymentListConverter {

    @TypeConverter
    @JvmStatic
    fun stringToPaymentList(data: String?) : List<PaymentType>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType =  object : TypeToken<List<PaymentType>>() {}.type

        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun paymentListToString(data: List<PaymentType>?): String? {
        return Gson().toJson(data)
    }
}