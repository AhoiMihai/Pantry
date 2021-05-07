package com.ahoi.pantry.common.operation

interface OperationState

enum class CommonOperationState: OperationState {
    SUCCESS,
    UNAUTHORIZED,
    UNKNOWN_ERROR
}