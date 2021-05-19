package com.ahoi.pantry.profile.domain

import com.ahoi.pantry.profile.data.Invitation
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class InvitationRepository(
    private val firestore: FirebaseFirestore,
) {

    fun getInvitationsForUser(email: String): Single<List<Invitation>> {

        return Single.create {
            firestore
                .collection("invitations")
                .whereEqualTo("invitedEmail", email)
                .get()
                .addOnSuccessListener { que ->
                    Single.just(que.documents.map { it.toObject(Invitation::class.java) })
                }
                .addOnFailureListener {
                    Single.error<List<Invitation>>(it)
                }
        }
    }

    fun createInvitationForEmail(invitation: Invitation): Completable {
        return Completable.create {
            firestore
                .collection("invitations")
                .add(invitation)
                .addOnSuccessListener {
                    Completable.complete()
                }
                .addOnFailureListener { Completable.error(it) }
        }
    }

    fun deleteInvitation(id: String): Completable {
        return Completable.create{
            firestore.collection("invitations")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    Completable.complete()
                }
                .addOnFailureListener {
                    Completable.error(it)
                }
        }
    }

}