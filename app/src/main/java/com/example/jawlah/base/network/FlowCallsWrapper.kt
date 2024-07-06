package com.example.jawlah.base.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.jawlah.base.dispatcher.Dispatcher

abstract class FlowCallsWrapper<in P, T>(private val schedulerProvider: Dispatcher) {
    fun asFlow(parameter: P): Flow<ApiResult<T>> = flow {
        emit(ApiResult.Loading)
        val remoteResponse =
            safeApiCall(dispatcher = schedulerProvider.io()) {
                safeCall(parameter) // fetch the remote source provided
            }
        when (remoteResponse) {
            is ResultWrappers.Success -> {
                emit(ApiResult.Success(value = remoteResponse.value))
                remoteResponse.value?.let {
                    processData(
                        parameter,
                        remoteResponse.value
                    )
                }
            }

            is ResultWrappers.Error -> {
                emit(ApiResult.Error(remoteResponse.throwable, remoteResponse.message))
            }

            ResultWrappers.Offline -> {
                emit(ApiResult.Offline)
            }
        }
    }

    abstract suspend fun safeCall(parameter: P): T
    open suspend fun processData(param: P, data: T) = Unit
}