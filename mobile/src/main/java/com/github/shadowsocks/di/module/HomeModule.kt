package com.github.shadowsocks.di.module

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import com.github.shadowsocks.mvp.contract.HomeContract
import com.github.shadowsocks.mvp.model.HomeModel


@Module
//构建HomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class HomeModule(private val view: HomeContract.View) {
    @ActivityScope
    @Provides
    fun provideHomeView(): HomeContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideHomeModel(model: HomeModel): HomeContract.Model {
        return model
    }
}
