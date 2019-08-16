package id.rent.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import id.rent.android.model.Product

object ProductTypeConverter {

    var gson = Gson()

    @TypeConverter
    @JvmStatic
    fun stringToProduct(data: String?) : Product {

        return gson.fromJson(data, Product::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun productToString(data: Product?): String? {
        return gson.toJson(data)
    }

}