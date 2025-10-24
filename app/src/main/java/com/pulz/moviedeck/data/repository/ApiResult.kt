package com.pulz.moviedeck.data.repository

/**
 * Resultado genérico das chamadas de API.
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class ApiError(val message: String?) : ApiResult<Nothing>() // Response = "False" ou erro da própria API
    data class HttpError(val code: Int, val message: String?) : ApiResult<Nothing>() // status != 2xx
    object NetworkError : ApiResult<Nothing>() // IOException (sem internet, timeout)
    data class UnknownError(val error: String?) : ApiResult<Nothing>() // outros
}
