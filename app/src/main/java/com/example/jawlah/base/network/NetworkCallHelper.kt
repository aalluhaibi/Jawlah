package com.example.jawlah.base.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.example.jawlah.data.remote.httpclient.httperror.AppHttpException
import com.example.jawlah.data.remote.httpclient.httperror.AppOfflineException

sealed class ResultWrappers<out T> {
    data class Success<out T>(val value: T) : ResultWrappers<T>()
    data class Error(val throwable: Throwable, val message: String) :
        ResultWrappers<Nothing>()
    object Offline : ResultWrappers<Nothing>()
}

internal suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrappers<T> {
    return withContext(dispatcher) {
        try {
            val call = apiCall.invoke()
            ResultWrappers.Success(call)
        } catch (e: AppHttpException) {
            ResultWrappers.Error(e, e.msg)
        } catch (e: AppOfflineException) {
            ResultWrappers.Offline
        }
        catch (e: Exception) {
            ResultWrappers.Error(e, e.message ?: "Unknown Error")
        }
    }
}