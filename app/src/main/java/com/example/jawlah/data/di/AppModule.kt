package com.example.jawlah.data.di

import android.content.Context
import com.example.jawlah.BuildConfig
import com.example.jawlah.base.annotation.GeneralAiModel
import com.example.jawlah.base.annotation.LandmarkLensAiModel
import com.example.jawlah.base.annotation.LuggageClassificationAiModel
import com.example.jawlah.base.annotation.ReadGateNoAiModel
import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.dispatcher.DispatcherImp
import com.example.jawlah.data.local.preference.AppPreferences
import com.example.jawlah.data.local.preference.SecurePreferences
import com.example.jawlah.data.local.realm.RealmPreferences
import com.example.jawlah.data.local.realm.RealmSecurePreferences
import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity
import com.example.jawlah.data.local.realm.plan.entity.CategoryEntity
import com.example.jawlah.data.local.realm.plan.entity.PlanEntity
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.data.remote.httpclient.httperror.AppHttpErrorMapper
import com.example.jawlah.data.remote.httpclient.httperror.ErrorMapper
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.android.Android
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun httpClientEngine(): HttpClientEngineFactory<*> = Android

    @Provides
    fun provideDispatchersProvider(): Dispatcher = DispatcherImp()

    @Singleton
    @Provides
    fun provideHttpErrorMapper(@ApplicationContext context: Context): ErrorMapper =
        AppHttpErrorMapper(context)

    @Provides
    fun provideRealmPreferences(@ApplicationContext context: Context): RealmPreferences =
        RealmSecurePreferences(context = context)

    @Provides
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences =
        SecurePreferences(context = context)

    @Provides
    @GeneralAiModel
    fun provideGeneralAiModel(): GenerativeModel =
        GenerativeModel(
            modelName = "gemini-1.5-flash", //gemini-pro-vision
            apiKey = BuildConfig.apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f
            },
            systemInstruction = content {
                text("You are a capable travel assistant named Gemi.")
            }
        )

    @Provides
    fun provideAiChat(@GeneralAiModel generativeModel: GenerativeModel): Chat =
        generativeModel.startChat(
            history = listOf(
                content("model") {
                    text("Hey there, traveler! \uD83D\uDC4B Ready to turn your wanderlust into reality?  I'm here to help you discover awesome places, answer your burning travel questions, and build the perfect trip. Where should we start? \uD83D\uDDFA\uFE0F")
                }
            )
        )

    @Provides
    @LuggageClassificationAiModel
    fun provideLuggageClassificationAiModel(): GenerativeModel =
        GenerativeModel(
            modelName = "gemini-1.5-flash", //gemini-pro-vision
            apiKey = BuildConfig.apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f
            },
            systemInstruction = content {
                text("You are a capable travel assistant named Gemi. Helping passengers classify their luggage and determine what items can be carried in the bag inside the aircraft cabin and what items must be loaded and which are not allowed to be carried inside the cabin.")
            }
        )

    @Provides
    fun provideAiChatForLuggageClassification(@LuggageClassificationAiModel generativeModel: GenerativeModel): Chat =
        generativeModel.startChat(
            history = listOf(
                content("model") {

                }
            )
        )

    @Provides
    @ReadGateNoAiModel
    fun provideReadGateNoAiModel(): GenerativeModel =
        GenerativeModel(
            modelName = "gemini-1.5-flash", //gemini-pro-vision
            apiKey = BuildConfig.apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f
            },
            systemInstruction = content {
                text("You are a capable travel assistant named Gemi. Helping passengers classify their luggage and determine what items can be carried in the bag inside the aircraft cabin and what items must be loaded and which are not allowed to be carried inside the cabin.")
            }
        )

    @Provides
    fun provideAiChatForGateNo(@ReadGateNoAiModel generativeModel: GenerativeModel): Chat =
        generativeModel.startChat(
            history = listOf(
                content("model") {

                }
            )
        )

    @Provides
    @LandmarkLensAiModel
    fun provideLandmarkLensAiModel(): GenerativeModel =
        GenerativeModel(
            modelName = "gemini-1.5-flash", //gemini-pro-vision
            apiKey = BuildConfig.apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f
            },
            systemInstruction = content {
                text("You are a capable tour guide named Gemi. A tour guide helps tourists get to know the landmarks")
            }
        )

    @Provides
    fun provideAiChatForLandmarkLens(@LandmarkLensAiModel generativeModel: GenerativeModel): Chat =
        generativeModel.startChat(
            history = listOf(
                content("model") {

                }
            )
        )

    @Singleton
    @Provides
    fun provideRealm(realmPreferences: RealmPreferences): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                PlanEntity::class,
                BudgetEntity::class,
                TransactionEntity::class,
                CategoryEntity::class
            )
        ).compactOnLaunch()
            .encryptionKey(realmPreferences.getRealmKey())
            .schemaVersion(1)
            .build()
        return Realm.open(config)
    }
}