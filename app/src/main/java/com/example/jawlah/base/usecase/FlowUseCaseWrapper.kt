package com.example.jawlah.base.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.network.ApiResult
import com.example.jawlah.base.network.FlowCallsWrapper
import kotlinx.coroutines.flow.Flow

abstract class FlowUseCaseWrapper<in P, R>(schedulerProvider: Dispatcher) {
    private val flowCallsWrapper = object : FlowCallsWrapper<P, R>(schedulerProvider) {
        override suspend fun safeCall(parameter: P): R {
            return execute(parameter)
        }

        override suspend fun processData(param: P, data: R) {
            processResultData(param, data)
        }
    }

    suspend operator fun invoke(parameters: P): Flow<ApiResult<R>> =
        flowCallsWrapper.asFlow(parameters)

    protected abstract suspend fun execute(parameters: P): R

     open suspend fun processResultData(parameter: P, data: R) = Unit
}