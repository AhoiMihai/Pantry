package com.ahoi.pantry.common.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class DefaultSchedulerProvider: SchedulerProvider {
    override fun computation(): Scheduler = Schedulers.computation()

    override fun io(): Scheduler = Schedulers.io()

    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()
}