package nxt.abhranil.dhtapp.data.repo

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import nxt.abhranil.dhtapp.data.model.CommonResponse
import nxt.abhranil.dhtapp.data.model.CreateUser
import nxt.abhranil.dhtapp.data.model.DiseaseCreate
import nxt.abhranil.dhtapp.data.remote.DHTApi
import nxt.abhranil.dhtapp.data.utils.UiState
import javax.inject.Inject

class DHTRepository @Inject constructor(private val api: DHTApi) {

    suspend fun createUser(token: String, user: CreateUser) : UiState<CommonResponse> {
        val response = api.createUser(
            token = token,
            user = user
        )
        if (response.isSuccessful)
            return UiState.Success(response.body()!!)
        else
            return UiState.Error(response.message())
    }

    suspend fun createDisease(token: String, disease: DiseaseCreate) : UiState<CommonResponse> {
        val response = api.createDisease(
            token = token,
            diseaseName = disease.name,
            files = disease.file,
            appointment = disease.appointment
        )
        if (response.isSuccessful)
            return UiState.Success(response.body()!!)
        else
            return UiState.Error(response.message())
    }
}