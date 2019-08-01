package id.rent.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.rent.android.data.vo.Resource
import id.rent.android.model.Product
import id.rent.android.repository.ProductRepository
import okhttp3.RequestBody
import javax.inject.Inject
import id.rent.android.utility.AbsentLiveData

class ProductViewModel
@Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel()
{
    private val _token = MutableLiveData<String>()
    private val _id = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token
    val id: LiveData<String>
        get() = _id

    fun setAuth(token: String?) {
        if (_token.value != token) {
            _token.value = token
        }
    }

    fun setStoreId(id: String?) {
        if (_id.value != id) {
            _id.value = id
        }
    }

    fun refresh() {
        _token.value?.let {
            _token.value = it
        }
    }

    val products: LiveData<Resource<List<Product>>> = Transformations
        .switchMap(_token) {token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                productRepository.getProducts(token)
            }
        }

    fun saveProduct(token: String, body: RequestBody): LiveData<Resource<Product>> {
        return productRepository.save(token, body)
    }

    fun updateProduct(token: String, id: String, fields: Map<String, String>): LiveData<Resource<Product>> {
        return productRepository.update(token, id, fields)
    }

    fun deleteProduct(token: String, id: String): LiveData<Resource<Product>> {
        return productRepository.delete(token, id)
    }
}