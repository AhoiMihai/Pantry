package com.ahoi.pantry.common.rx

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {

    fun computation(): Scheduler

    fun io() :Scheduler

    fun mainThread(): Scheduler
}