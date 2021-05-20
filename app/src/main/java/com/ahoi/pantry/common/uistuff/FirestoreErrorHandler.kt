package com.ahoi.pantry.common.uistuff

import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.google.firebase.firestore.FirebaseFirestoreException

class FirestoreErrorHandler {

    fun handleError(throwable: Throwable): OperationState {
        return if (throwable is FirebaseFirestoreException) {
            when (throwable.code) {
                FirebaseFirestoreException.Code.UNAUTHENTICATED -> CommonOperationState.UNAUTHORIZED
                FirebaseFirestoreException.Code.ALREADY_EXISTS -> CommonOperationState.DUPLICATE
                else -> CommonOperationState.UNKNOWN_ERROR
            }
        } else {
            CommonOperationState.UNKNOWN_ERROR
        }
    }
}