package com.ahoi.pantry.profile.domain

import com.ahoi.pantry.profile.data.Invitation
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class InvitationRepository(
    private val firestore: FirebaseFirestore,
) {

    fun getInvitationsForUser(email: String): Single<List<Invitation>> {
        return Single.create { emitter ->
            firestore
                .collection("invitations")
                .whereEqualTo("invitedEmail", email)
                .get()
                .addOnSuccessListener { que ->
                    emitter.onSuccess(que.documents.map { it.toInvitation() })
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun createInvitationForEmail(invitation: Invitation): Completable {
        return Completable.create { emitter ->
            firestore
                .collection("invitations")
                .add(invitation)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener { emitter.onError(it) }
        }
    }

    fun deleteInvitation(id: String): Completable {
        return Completable.create { emitter ->
            firestore.collection("invitations")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}

fun DocumentSnapshot.toInvitation(): Invitation {
    return Invitation(
        this.id,
        this["invitedEmail"] as String,
        this["sourceDisplayName"] as String,
        this["pantryId"] as String
    )
}