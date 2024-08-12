package com.example.jawlah.base.annotation

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeneralAiModel

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LuggageClassificationAiModel

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReadGateNoAiModel

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LandmarkLensAiModel