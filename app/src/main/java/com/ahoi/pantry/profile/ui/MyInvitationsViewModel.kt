package com.ahoi.pantry.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.profile.data.Invitation
import com.ahoi.pantry.profile.domain.InvitationRepository
import com.ahoi.pantry.profile.domain.ProfileRepositoryImpl

class MyInvitationsViewModel(
    private val profileRepository: ProfileRepositoryImpl,
    private val invitationRepository: InvitationRepository,
    private val userIdSupplier: () -> String,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {

    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> = _operationState

    private val _invitations = MutableLiveData<List<Invitation>>()
    val invitations: LiveData<List<Invitation>> = _invitations

    fun loadInvitations() {
        profileRepository.getOrLoadCurrent().flatMap {
            invitationRepository.getInvitationsForUser(it.email)
        }.subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) {
                        _operationState.postValue(InvitationState.EMPTY_LIST)
                    } else {
                        _invitations.postValue(it)
                    }
                },
                {
                    _operationState.postValue(errorHandler.handleError(it))
                }
            )
    }

    fun acceptInvitation(invitation: Invitation) {
        profileRepository.updateProfilePantryRef(userIdSupplier(), invitation.pantryId)
            .andThen(profileRepository.loadProfile(userIdSupplier()))
            .doOnComplete { invitationRepository.deleteInvitation(invitation.id) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    _operationState.postValue(InvitationState.INVITATION_ACCEPTED)
                },
                {
                    _operationState.postValue(errorHandler.handleError(it))
                }
            )
    }

    fun deleteInvitation(invitation: Invitation) {
        invitationRepository.deleteInvitation(invitation.id)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    _operationState.postValue(InvitationState.INVITATION_DELETED)
                },
                {
                    _operationState.postValue(errorHandler.handleError(it))
                }
            )
    }

}

enum class InvitationState : OperationState {
    INVITATION_DELETED,
    INVITATION_ACCEPTED,
    EMPTY_LIST
}