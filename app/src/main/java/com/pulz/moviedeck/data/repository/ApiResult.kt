package com.pulz.moviedeck.data.repository

/**
 * Resultado genérico das chamadas de API.
 */
sealed class ApiResult<out T> {
    abstract val message: String?

    data class Success<T>(val data: T) : ApiResult<T>() {
        override val message: String? = null
    }
    data class ApiError(override val message: String?) : ApiResult<Nothing>() // Response = "False" ou erro da própria API
    data class HttpError(val code: Int, override val message: String?) : ApiResult<Nothing>() // status != 2xx
    object NetworkError : ApiResult<Nothing>() { // IOException (sem internet, timeout)
        override val message: String = "Network Error"
    }
    data class UnknownError(override val message: String?) : ApiResult<Nothing>() // outros
}
