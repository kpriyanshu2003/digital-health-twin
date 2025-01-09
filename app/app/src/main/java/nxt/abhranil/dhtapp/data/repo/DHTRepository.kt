package nxt.abhranil.dhtapp.data.repo

import nxt.abhranil.dhtapp.data.model.CommonResponse
import nxt.abhranil.dhtapp.data.model.CreateUser
import nxt.abhranil.dhtapp.data.model.DiseaseCreate
import nxt.abhranil.dhtapp.data.model.GetAllAppointmentsResponse
import nxt.abhranil.dhtapp.data.model.GetAppointmentByIdResponse
import nxt.abhranil.dhtapp.data.model.GetDiseaseByIdResponse
import nxt.abhranil.dhtapp.data.model.GetUserDiseaseResponse
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

    suspend fun getUserDiseases(token: String) : UiState<GetUserDiseaseResponse> {
        val response = api.getUserDiseases(
            token = token
        )
        if (response.isSuccessful)
            return UiState.Success(response.body()!!)
        else
            return UiState.Error(response.message())
    }

    suspend fun getDiseaseById(token: String, diseaseID: String) : UiState<GetDiseaseByIdResponse> {
        val response = api.getDiseaseById(
            token = token,
            diseaseID = diseaseID
        )
        if (response.isSuccessful)
            return UiState.Success(response.body()!!)
        else
            return UiState.Error(response.message())
    }

    suspend fun getAppointmentById(token: String, appointmentID: String) : UiState<GetAppointmentByIdResponse> {
        val response = api.getAppointmentById(
            token = token,
            appointmentID = appointmentID
        )
        if (response.isSuccessful)
            return UiState.Success(response.body()!!)
        else
            return UiState.Error(response.message())
    }

    suspend fun getAllAppointments(token: String) : UiState<GetAllAppointmentsResponse> {
        val response = api.getAllAppointments(
            token = token
        )
        if (response.isSuccessful)
            return UiState.Success(response.body()!!)
        else
            return UiState.Error(response.message())
    }
}