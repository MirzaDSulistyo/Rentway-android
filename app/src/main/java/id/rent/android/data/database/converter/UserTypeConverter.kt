package id.rent.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import id.rent.android.model.User
import timber.log.Timber

object UserTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToUser(data: String?) : User {
        if (data == null) {
            return User()
        }

        return Gson().fromJson(data, User::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun userToString(data: User?): String? {
        return Gson().toJson(data)
    }

}