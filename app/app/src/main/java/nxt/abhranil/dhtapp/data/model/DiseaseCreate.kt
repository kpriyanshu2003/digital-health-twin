package nxt.abhranil.dhtapp.data.model

import okhttp3.MultipartBody

data class DiseaseCreate(
    val name: String,
    val file: List<MultipartBody.Part>,
    val appointment: String
)
