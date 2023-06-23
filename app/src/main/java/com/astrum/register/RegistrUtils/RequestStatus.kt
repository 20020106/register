package com.astrum.register.RegistrUtils

sealed class RequestStatus<out T> {
    object Waiting : RequestStatus<Nothing>()
    data class Success<out T>(val date: T) : RequestStatus<T>()
    data class Error(val message: HashMap<String, String>) : RequestStatus<Nothing>()
}
