package com.example.jawlah.data.di.ai

import com.example.jawlah.base.annotation.GeneralAiModel
import com.example.jawlah.base.annotation.LandmarkLensAiModel
import com.example.jawlah.base.annotation.LuggageClassificationAiModel
import com.example.jawlah.base.annotation.ReadGateNoAiModel
import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.domain.ask_gemi.usecase.AskGemiGeneralQuestionUseCase
import com.example.jawlah.domain.ask_gemi.usecase.ClassifyLuggageUseCase
import com.example.jawlah.domain.ask_gemi.usecase.LandmarkLensUseCase
import com.example.jawlah.domain.ask_gemi.usecase.ReadGateNumberUseCase
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AiModelModule {
    @Provides
    fun provideAskGemiGeneralQuestionUseCase(
        dispatcher: Dispatcher,
        @GeneralAiModel generalAiModel: GenerativeModel
    ): AskGemiGeneralQuestionUseCase {
        return AskGemiGeneralQuestionUseCase(
            dispatcher = dispatcher,
            generalAiModel = generalAiModel
        )
    }

    @Provides
    fun provideClassifyLuggageUseCase(
        dispatcher: Dispatcher,
        @LuggageClassificationAiModel generalAiModel: GenerativeModel
    ): ClassifyLuggageUseCase {
        return ClassifyLuggageUseCase(
            dispatcher = dispatcher,
            generalAiModel = generalAiModel
        )
    }

    @Provides
    fun provideLandmarkLensUseCase(
        dispatcher: Dispatcher,
        @LandmarkLensAiModel generalAiModel: GenerativeModel
    ): LandmarkLensUseCase {
        return LandmarkLensUseCase(
            dispatcher = dispatcher,
            generalAiModel = generalAiModel
        )
    }

    @Provides
    fun provideReadGateNumberUseCase(
        dispatcher: Dispatcher,
        @ReadGateNoAiModel generalAiModel: GenerativeModel
    ): ReadGateNumberUseCase {
        return ReadGateNumberUseCase(
            dispatcher = dispatcher,
            generalAiModel = generalAiModel
        )
    }
}