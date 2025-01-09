package nxt.abhranil.dhtapp.data.model

import android.net.Uri
import com.google.gson.annotations.Expose

data class AppointmentUpload(
    var name: String = "",
    var date: String = "",
    var imageUri: Uri? = null
)
