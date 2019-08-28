package id.rent.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import id.rent.android.model.Category
import timber.log.Timber

object CategoryTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToCategory(data: String?) : Category {
        Timber.d("convert to category $data")
        if (data == null) {
            Timber.d("convert to category $data is null")
            return Category()
        }

        return Gson().fromJson(data, Category::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun categoryToString(data: Category?): String? {
        return Gson().toJson(data)
    }

}