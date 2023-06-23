package com.astrum.register.RegistrUtils

import com.astrum.register.RegisterDate.UniqueEmailValidationResponse
import com.astrum.register.RegisterDate.ValidateEmailBodi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {

    @POST("users/validate-unique-email")
    suspend fun validateEmailAddress(@Body body: ValidateEmailBodi): Response<UniqueEmailValidationResponse>
}