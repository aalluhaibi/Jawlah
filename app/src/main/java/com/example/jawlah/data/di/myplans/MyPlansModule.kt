package com.example.jawlah.data.di.myplans

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.data.local.realm.plan.MyPlanRepoImpl
import com.example.jawlah.domain.budget.usecase.AddCategoryUseCase
import com.example.jawlah.domain.budget.usecase.AddPlaceUseCase
import com.example.jawlah.domain.budget.usecase.AddTotalIncomeUseCase
import com.example.jawlah.domain.budget.usecase.AddTransactionUseCase
import com.example.jawlah.domain.budget.usecase.CreateBudgetUseCase
import com.example.jawlah.domain.budget.usecase.DeleteTransactionUseCase
import com.example.jawlah.domain.budget.usecase.RetrieveBudgetUseCase
import com.example.jawlah.domain.budget.usecase.RetrieveCategoriesUseCase
import com.example.jawlah.domain.budget.usecase.RetrievePlacesUseCase
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
    fun provideDeleteTransactionUseCase(
        dispatcher: Dispatcher,
        repo: MyPlansRepo
    ): DeleteTransactionUseCase = DeleteTransactionUseCase(
        dispatcher = dispatcher,
        repo = repo
    )

    @Provides
    fun provideAddPlaceUseCase(
        dispatcher: Dispatcher,
        repo: MyPlansRepo
    ): AddPlaceUseCase = AddPlaceUseCase(
        dispatcher = dispatcher,
        repo = repo
    )

    @Provides
    fun provideAddTotalIncomeUseCase(
        dispatcher: Dispatcher,
        repo: MyPlansRepo
    ): AddTotalIncomeUseCase = AddTotalIncomeUseCase(
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

    @Provides
    fun provideRetrieveCategoriesUseCase(
        dispatcher: Dispatcher,
        repo: MyPlansRepo
    ): RetrieveCategoriesUseCase = RetrieveCategoriesUseCase(
        dispatcher = dispatcher,
        repo = repo
    )

    @Provides
    fun provideRetrievePlacesUseCase(
        dispatcher: Dispatcher,
        repo: MyPlansRepo
    ): RetrievePlacesUseCase = RetrievePlacesUseCase(
        dispatcher = dispatcher,
        repo = repo
    )

    @Provides
    fun provideAddCategoryUseCase(
        dispatcher: Dispatcher,
        repo: MyPlansRepo
    ): AddCategoryUseCase = AddCategoryUseCase(
        dispatcher = dispatcher,
        repo = repo
    )
}