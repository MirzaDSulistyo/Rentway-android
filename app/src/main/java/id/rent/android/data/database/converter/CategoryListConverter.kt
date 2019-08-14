package id.rent.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.rent.android.model.Category
import java.util.*

object CategoryListConverter {

    @TypeConverter
    @JvmStatic
    fun stringToCategoryList(data: String?) : List<Category>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType =  object : TypeToken<List<Category>>() {}.type

        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun categoryListToString(data: List<Category>?): String? {
        return Gson().toJson(data)
    }
}