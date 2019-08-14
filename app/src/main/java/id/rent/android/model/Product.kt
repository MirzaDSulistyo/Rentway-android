package id.rent.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.rent.android.data.database.converter.CategoryTypeConverter

@Entity
@TypeConverters(CategoryTypeConverter::class)
data class Product (
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    val id: String = "",
    @SerializedName("store_id")
    @Expose
    val storeId: String? = null,
    @SerializedName("name")
    @Expose
    val name: String? = null,
    @SerializedName("description")
    @Expose
    val description: String? = null,
    @SerializedName("category")
    @Expose
    val category: Category? = null,
    @SerializedName("available")
    @Expose
    val available: String? = null,
    @SerializedName("commission")
    @Expose
    val commission: String? = null,
    @SerializedName("photo")
    @Expose
    val photo: String? = null,
    @SerializedName("brand")
    @Expose
    val brand: String? = null,
    @SerializedName("barcode")
    @Expose
    val barcode: String? = null,
    @SerializedName("sku")
    @Expose
    val sku: String? = null,
    @SerializedName("price")
    @Expose
    val price: Int = 0,
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null
)

class ProductList {

    @SerializedName("data")
    @Expose
    var products: List<Product>? = null

    @SerializedName("status")
    @Expose
    val status: Int = 0

}