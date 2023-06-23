package com.astrum.register.RegistrViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.astrum.register.RegistrRepository.AuthRepository
import java.io.InvalidClassException

class RegisterActivityViewModelFactori(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterActivityViewModel::class.java)) {
            return RegisterActivityViewModel(authRepository, application) as T
        }

        throw InvalidClassException("Unable to construct RegisterActivityViewModel")
    }

}