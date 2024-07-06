package com.example.jawlah.di.myplans

import com.example.jawlah.data.local.realm.plan.MyPlanRepoImpl
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

}