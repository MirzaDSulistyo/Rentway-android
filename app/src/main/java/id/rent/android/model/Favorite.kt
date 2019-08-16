package id.rent.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.rent.android.data.database.converter.ProductTypeConverter

@Entity
@TypeConverters(ProductTypeConverter::class)
data class Favorite(
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    val id: String = "",

    @SerializedName("user")
    @Expose
    val user: String? = null,

    @SerializedName("product")
    @Expose
    val product: Product? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null
)

class FavoriteList {
    @SerializedName("status")
    @Expose
    val status: Int? = null

    @SerializedName("data")
    @Expose
    val data: List<Favorite>? = null
}