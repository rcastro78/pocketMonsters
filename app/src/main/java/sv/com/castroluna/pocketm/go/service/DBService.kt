package sv.com.castroluna.pocketm.go.service

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.castroluna.pocketm.go.model.UserToken
import sv.com.castroluna.pocketm.go.networking.APISamaritanUtils
import sv.com.castroluna.pocketm.go.networking.ISamaritanService

class DBService: Service() {
    private lateinit var  iSamaritanService: ISamaritanService
    private val SHARED:String="spref.castroluna.pocketm.go"
    private var sharedPreferences: SharedPreferences? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(SHARED, 0)
        iSamaritanService = APISamaritanUtils.getSamaritanService()!!
        getToken()
    }


    fun getToken(){
        val call = iSamaritanService.getToken()
        call.enqueue(object : Callback<UserToken> {
            override fun onResponse(call: Call<UserToken>?, response: Response<UserToken>?) {
                if (response != null) {
                    val editor = sharedPreferences!!.edit()
                    editor.putString("TOKEN",response.body().token)
                    editor.putString("EXPIRY",response.body().expiresAt)
                    editor.apply()
                    Log.d("SERVICE_PK",response.body().token);
                }
            }

            override fun onFailure(call: Call<UserToken>?, t: Throwable?) {

            }
        })
    }







}