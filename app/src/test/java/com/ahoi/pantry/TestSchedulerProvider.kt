package com.ahoi.pantry

import com.ahoi.pantry.common.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Scheduler

class TestSchedulerProvider(private val testScheduler: Scheduler) : SchedulerProvider {
    override fun mainThread(): Scheduler {
        return testScheduler
    }

    override fun io(): Scheduler {
        return testScheduler
    }

    override fun computation(): Scheduler {
        return testScheduler
    }
}