package id.rent.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.rent.android.data.vo.Resource
import id.rent.android.model.Favorite
import javax.inject.Inject
import id.rent.android.repository.FavoriteRepository
import okhttp3.RequestBody
import id.rent.android.utility.AbsentLiveData

class FavoriteViewModel
@Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel() {
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

    val data: LiveData<Resource<List<Favorite>>> = Transformations
        .switchMap(_token) { token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                repository.get(token)
            }
        }

//    fun save(token: String, body: RequestBody): LiveData<Resource<Favorite>> {
//        return repository.save(token, body)
//    }
//
//    fun update(token: String, id: String, fields: Map<String, String>): LiveData<Resource<Favorite>> {
//        return repository.update(token, id, fields)
//    }
//
//    fun delete(token: String, id: String): LiveData<Resource<Favorite>> {
//        return repository.delete(token, id)
//    }
}