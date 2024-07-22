package com.example.jawlah.data.di.myplans

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.data.local.realm.plan.MyPlanRepoImpl
import com.example.jawlah.domain.budget.usecase.AddTransactionUseCase
import com.example.jawlah.domain.budget.usecase.CreateBudgetUseCase
import com.example.jawlah.domain.budget.usecase.RetrieveBudgetUseCase
import com.example.jawlah.domain.myplans.MyPlansRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.realm.kotlin.Realm

@InstallIn(ViewModelComponent::class)
@Module
class MyPlansModule {

    @Provides
    fun provideMyPlansRepo(realm: Realm): MyPlansRepo = MyPlanRepoImpl(realm)

    @Provides
    fun provideRetrieveBudgetUseCase(
        dispatcher: Dispatcher,
        repo: MyPlansRepo
    ): RetrieveBudgetUseCase = RetrieveBudgetUseCase(
        dispatcher = dispatcher,
        repo = repo
    )

    @Provides
    fun provideAddTransactionUseCase(
        dispatcher: Dispatcher,
        repo: MyPlansRepo
    ): AddTransactionUseCase = AddTransactionUseCase(
        dispatcher = dispatcher,
        repo = repo
    )

    @Provides
    fun provideCreateBudgetUseCase(
        dispatcher: Dispatcher,
        repo: MyPlansRepo
    ): CreateBudgetUseCase = CreateBudgetUseCase(
        dispatcher = dispatcher,
        repo = repo
    )
}