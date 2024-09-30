package com.example.jawlah.domain.ask_gemi.usecase

import com.example.jawlah.base.annotation.ReadGateNoAiModel
import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.presentation.feature.ask_gemi.Message
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content

class ReadGateNumberUseCase(
    dispatcher: Dispatcher,
    @ReadGateNoAiModel private val generalAiModel: GenerativeModel
) : FlowUseCaseWrapper<Message, GenerateContentResponse?>(dispatcher) {
    private val askGemi: Chat = generalAiModel.startChat()

    override suspend fun execute(parameters: Message): GenerateContentResponse? {
        return try {
            val response = askGemi.sendMessage(
                content {
                    text(parameters.text)
                    parameters.img?.let { image(it) }
                }
            )
            response
        } catch (exception: Exception) {
            exception.stackTrace
            null
        }
    }
}