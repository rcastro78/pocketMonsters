package sv.com.castroluna.pocketm.go.networking

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import sv.com.castroluna.pocketm.go.fragment.WildFragment
import sv.com.castroluna.pocketm.go.model.Captured
import sv.com.castroluna.pocketm.go.model.Community
import sv.com.castroluna.pocketm.go.model.MyTeam
import sv.com.castroluna.pocketm.go.model.UserToken

interface ISamaritanService {
    @POST("token?email=rcastroluna.sv@gmail.com")
    fun getToken():Call<UserToken>

    @GET("activity")
    fun getCommunity(@Header("Authorization") token:String):Call<Community>

    @GET("my-team")
    fun getMyTeam(@Header("Authorization") token:String):Call<List<MyTeam>>

    @GET("captured")
    fun getCaptured(@Header("Authorization") token:String):Call<List<Captured>>

    @POST("capture")
    fun capturePokemon(@Header("Authorization") token:String, @Body body: JsonObject):Call<JsonObject>

    @POST("release?id={id}")
    fun deletePokemon(@Header("Authorization") token:String, @Path("id") id:String):Call<JsonObject>

}