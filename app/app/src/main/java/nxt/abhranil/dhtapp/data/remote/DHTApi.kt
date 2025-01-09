package nxt.abhranil.dhtapp.data.remote

import nxt.abhranil.dhtapp.data.model.CommonResponse
import nxt.abhranil.dhtapp.data.model.CreateUser
import nxt.abhranil.dhtapp.data.model.DiseaseCreate
import nxt.abhranil.dhtapp.data.model.GetAppointmentByIdResponse
import nxt.abhranil.dhtapp.data.model.GetDiseaseByIdResponse
import nxt.abhranil.dhtapp.data.model.GetUserDiseaseResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface DHTApi {

    @POST("user")
    suspend fun createUser(
        @Header("Authorization") token: String,
        @Body user: CreateUser
    ) : Response<CommonResponse>

    @Multipart
    @POST("conditions")
    suspend fun createDisease(
        @Header("Authorization") token: String,
        @Part("name") diseaseName: String,
        @Part files: List<MultipartBody.Part>,
        @Part("appointment") appointment: String
    ) : Response<CommonResponse>

    @GET("conditions")
    suspend fun getUserDiseases(
        @Header("Authorization") token: String
    ) : Response<GetUserDiseaseResponse>

    @GET("conditions/c/{diseaseID}")
    suspend fun getDiseaseById(
        @Header("Authorization") token: String,
        @Path("diseaseID") diseaseID: String
    ) : Response<GetDiseaseByIdResponse>

    @GET("appointments/{appointmentID}")
    suspend fun getAppointmentById(
        @Header("Authorization") token: String,
        @Path("appointmentID") appointmentID: String
    ) : Response<GetAppointmentByIdResponse>
}