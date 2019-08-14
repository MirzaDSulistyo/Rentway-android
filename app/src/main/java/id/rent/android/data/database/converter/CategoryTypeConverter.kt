package id.rent.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import id.rent.android.model.Category

object CategoryTypeConverter {

    var gson = Gson()

    @TypeConverter
    @JvmStatic
    fun stringToUser(data: String?) : Category {

        return gson.fromJson(data, Category::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun userToString(data: Category?): String? {
        return gson.toJson(data)
    }

}