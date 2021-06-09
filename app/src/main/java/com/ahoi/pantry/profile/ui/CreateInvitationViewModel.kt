package com.ahoi.pantry.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.profile.data.Invitation
import com.ahoi.pantry.profile.domain.InvitationRepository
import com.ahoi.pantry.profile.domain.ProfileRepositoryImpl

class CreateInvitationViewModel(
    private val invitationRepository: InvitationRepository,
    private val profileRepository: ProfileRepositoryImpl,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {

    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> = _operationState

    fun sendInvitationToEmail(email: String) {
        profileRepository.getOrLoadCurrent()
            .flatMapCompletable {
                val invitation = Invitation(
                    "",
                    email,
                    it.name,
                    it.pantryReference
                )
                invitationRepository.createInvitationForEmail(invitation)
            }.subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    _operationState.postValue(CommonOperationState.SUCCESS)
                },
                {
                    _operationState.postValue(errorHandler.handleError(it))
                }
            )
    }
}