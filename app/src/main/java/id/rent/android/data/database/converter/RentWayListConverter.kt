package id.rent.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.rent.android.model.RentWay
import java.util.*

object RentWayListConverter {

    @TypeConverter
    @JvmStatic
    fun stringToRentWayList(data: String?) : List<RentWay>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType =  object : TypeToken<List<RentWay>>() {}.type

        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun rentWayListToString(data: List<RentWay>?): String? {
        return Gson().toJson(data)
    }
}