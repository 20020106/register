package com.astrum.register.RegistrRepository

import com.astrum.register.RegisterDate.ValidateEmailBodi
import com.astrum.register.RegistrUtils.APIConsumer
import com.astrum.register.RegistrUtils.RequestStatus
import com.astrum.register.RegistrUtils.SimplifideMessage
import kotlinx.coroutines.flow.flow

class AuthRepository(private val consumer: APIConsumer) {
    fun validateEmailAddress(bodi: ValidateEmailBodi) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.validateEmailAddress(bodi)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifideMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }

    }
}