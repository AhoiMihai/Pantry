package com.ahoi.pantry.common.operation

import io.reactivex.rxjava3.core.Single

interface Operation<T> {

    fun execute(): Single<T>
}