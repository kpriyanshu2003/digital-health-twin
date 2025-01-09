package nxt.abhranil.dhtapp.data.model

import android.net.Uri

data class AppointmentUpload(
    var name: String = "",
    var date: String = "",
    var imageUri: Uri? = null
)
