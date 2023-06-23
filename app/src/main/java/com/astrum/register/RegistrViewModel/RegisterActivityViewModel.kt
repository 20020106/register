package com.astrum.register.RegistrViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrum.register.RegisterDate.User
import com.astrum.register.RegisterDate.ValidateEmailBodi
import com.astrum.register.RegistrRepository.AuthRepository
import com.astrum.register.RegistrUtils.RequestStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterActivityViewModel(val authRepository: AuthRepository, val application: Application) :
    ViewModel() {

    private val isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private val errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private val isUniqueEmail: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private val user: MutableLiveData<User> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getIsUniqueEmail(): LiveData<Boolean> = isUniqueEmail
    fun getUser(): LiveData<User> = user

    fun validateEmailAddress(bodi: ValidateEmailBodi) {
        viewModelScope.launch {
            authRepository.validateEmailAddress(bodi).collect(){
                when(it){
                    is RequestStatus.Waiting ->{
                        isLoading.value = true
                    }
                    is RequestStatus.Success ->{
                        isLoading.value = false
                        isUniqueEmail.value = it.date.isUnique
                    }
                    is RequestStatus.Error ->{
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }

    }
}