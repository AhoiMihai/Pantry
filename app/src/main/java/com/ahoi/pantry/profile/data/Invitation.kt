package com.ahoi.pantry.profile.data

import com.google.firebase.firestore.DocumentId

data class Invitation(
    @DocumentId
    val id: String,
    val invitedEmail: String,
    val sourceDisplayName: String,
    val pantryId: String,
)