package nxt.abhranil.dhtapp.data.remote

import nxt.abhranil.dhtapp.data.model.CommonResponse
import nxt.abhranil.dhtapp.data.model.CreateUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface DHTApi {

    @POST("user")
    suspend fun createUser(
        @Header("Authorization") token: String,
        @Body user: CreateUser
    ) : Response<CommonResponse>
}