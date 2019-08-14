package id.rent.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.rent.android.data.vo.Resource
import id.rent.android.model.Master
import id.rent.android.model.RentWay
import id.rent.android.repository.MasterRepository
import id.rent.android.utility.AbsentLiveData
import javax.inject.Inject

class DataMasterViewModel
@Inject constructor(
    private val masterRepository: MasterRepository
) : ViewModel()
{
    private val _token = MutableLiveData<String>()

    val token: LiveData<String>
        get() = _token

    fun setAuth(token: String?) {
        if (_token.value != token) {
            _token.value = token
        }
    }

    fun refresh() {
        _token.value?.let {
            _token.value = it
        }
    }

    val master: LiveData<Resource<Master>> = Transformations
        .switchMap(_token) {token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                masterRepository.getDataMaster(token)
            }
        }
}