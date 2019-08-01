package id.rent.android.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Profile {
    @SerializedName("data")
    @Expose
    var user: User? = null

    var stores: List<Store>? = null
}