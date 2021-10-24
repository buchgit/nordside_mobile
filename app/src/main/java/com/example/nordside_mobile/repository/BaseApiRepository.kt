package com.example.nordside_mobile.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Оборачивает результат вызова API в Resource.
 * Вызывает apiCall в отдельном потоке, за выбор потока отвечает Dispatcher.IO.
 * Выполняет запрс в фоновом потоке с помощью withContext, при этом код ждет, пока блок withContext
 * не завершит свою работу.
*/

abstract class BaseApiRepository {

    suspend fun<T> safeApiCall(
        apiCall: suspend() -> T
    ) : Resource<T> {
        return withContext(Dispatchers.IO) {

            try {
                Resource.Success<T>(apiCall.invoke())
            } catch (throwable: Throwable) {

                when(throwable) {
                    is HttpException -> {
                        Resource.Error("Http error code ${throwable.code()}")
                    }
                    else -> {
                        Resource.Error("Network error")
                    }
                }

            }

        }
    }
}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String): Resource<T>(message = message)
}