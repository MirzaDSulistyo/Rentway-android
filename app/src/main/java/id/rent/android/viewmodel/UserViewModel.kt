package id.rent.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.rent.android.data.vo.Resource
import id.rent.android.model.Auth
import id.rent.android.model.Profile
import id.rent.android.repository.UserRepository
import id.rent.android.utility.AbsentLiveData
import javax.inject.Inject

class UserViewModel
@Inject constructor(
    private val userRepository: UserRepository
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

    fun login(email: String, pass: String): LiveData<Resource<Auth>> {
        return userRepository.login(email, pass)
    }

    val profile: LiveData<Resource<Profile>> = Transformations
        .switchMap(_token) {token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                userRepository.profile(token)
            }
        }

}