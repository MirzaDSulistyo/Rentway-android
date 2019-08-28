package id.rent.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import id.rent.android.model.Product

object ProductTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToProduct(data: String?) : Product {
        if (data == null) {
            return Product()
        }

        return Gson().fromJson(data, Product::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun productToString(data: Product?): String? {
        return Gson().toJson(data)
    }

}