package id.rent.android.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.rent.android.data.database.converter.CategoryListConverter
import id.rent.android.data.database.converter.PaymentListConverter
import id.rent.android.data.database.converter.RentWayListConverter
import kotlinx.android.parcel.Parcelize

@Entity
@TypeConverters(RentWayListConverter::class, PaymentListConverter::class, CategoryListConverter::class)
data class Master(
    @PrimaryKey
    val status: Int? = null,
    var rentway: List<RentWay>? = null,
    var payments: List<PaymentType>? = null,
    var categories: List<Category>? = null
)

@Entity
data class RentWay(
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String? = null,
    val descriptions: String? = null,
    val level: String? = null,
    val alias: String? = null,
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null
)

@Entity
data class PaymentType(
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    val id: String = "",
    val name: String = "",
    @SerializedName("created_at")
    @Expose
    val createdAt: String = "",
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String = ""
)

@Entity
@Parcelize
data class Category(
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    val id: String = "",
    val name: String = "",
    @SerializedName("created_at")
    @Expose
    val createdAt: String = "",
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String = ""
) : Parcelable