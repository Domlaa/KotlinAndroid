package com.gfd.common.common

import android.app.Application
import android.content.Context
import com.gfd.common.injection.component.DaggerAppComponent
import com.gfd.common.injection.module.AppMoudle
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger



/**
 * @Author : 郭富东
 * @Date ：2018/8/2 - 9:49
 * @Email：878749089@qq.com
 * @descriptio：
 */
open class BaseApplication : Application(){
    lateinit var appComponent: DaggerAppComponent

    override fun onCreate() {
        super.onCreate()
        initInjection()
        Logger.addLogAdapter(AndroidLogAdapter())
        context = this
    }

    private fun initInjection() {
         appComponent = DaggerAppComponent.builder()
                 .appMoudle(AppMoudle(this))
                 .build() as DaggerAppComponent
    }

    companion object {
        lateinit var context :Context
    }
}