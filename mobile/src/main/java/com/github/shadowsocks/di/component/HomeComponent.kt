package com.github.shadowsocks.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.github.shadowsocks.di.module.HomeModule

import com.jess.arms.di.scope.ActivityScope
import com.github.shadowsocks.mvp.ui.activity.HomeActivity

@ActivityScope
@Component(modules = arrayOf(HomeModule::class), dependencies = arrayOf(AppComponent::class))
interface HomeComponent {
    fun inject(activity: HomeActivity)
}
