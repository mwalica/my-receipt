package ch.walica.receipt_app.models

import android.net.Uri

data class Image(
    val id: Long,
    val addDate: Long,
    val name: String,
    val uri: Uri,
    val title: String
)
