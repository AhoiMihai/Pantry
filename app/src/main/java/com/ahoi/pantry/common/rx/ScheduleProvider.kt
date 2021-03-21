package com.ahoi.pantry.common.rx

import io.reactivex.rxjava3.core.Scheduler

interface ScheduleProvider {

    fun computation(): Scheduler

    fun io() :Scheduler

    fun mainThread(): Scheduler
}