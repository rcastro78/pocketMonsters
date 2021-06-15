package sv.com.castroluna.pocketm.go.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.castroluna.pocketm.go.dao.Team
import sv.com.castroluna.pocketm.go.model.MyTeam
import sv.com.castroluna.pocketm.go.model.PokemonDataRoot
import sv.com.castroluna.pocketm.go.networking.APISamaritanUtils
import sv.com.castroluna.pocketm.go.networking.APIUtils
import sv.com.castroluna.pocketm.go.networking.IPokeService
import sv.com.castroluna.pocketm.go.networking.ISamaritanService
import sv.com.castroluna.pocketm.go.service.AppDatabase

class MyTeamViewModel : ViewModel() {
    var myTeamListData:MutableLiveData<List<MyTeam>>
    var pokemonListImages:MutableLiveData<PokemonDataRoot>
    private var iSamaritanService:ISamaritanService
    private var iPokeService:IPokeService
    lateinit var db: AppDatabase
    init {
        iSamaritanService = APISamaritanUtils.getSamaritanService()!!
        iPokeService = APIUtils.getPokeService()!!
        myTeamListData = MutableLiveData()
        pokemonListImages = MutableLiveData()

    }



    fun getMyTeamListDataObserver():MutableLiveData<List<MyTeam>>{
        return myTeamListData
    }




    fun apiCall(tk:String){
        val call = iSamaritanService.getMyTeam(tk)
        call.enqueue(object : Callback<List<MyTeam>>{
            override fun onResponse(call: Call<List<MyTeam>>?, response: Response<List<MyTeam>>?) {
                if (response != null) {
                    myTeamListData.postValue(response.body())
                }else{
                    Log.d("POKE_TEAM","Error poke team")
                    myTeamListData.postValue(null)
                }
            }
            override fun onFailure(call: Call<List<MyTeam>>?, t: Throwable?) {
                Log.d("POKE_TEAM", t.toString())
                myTeamListData.postValue(null)
            }
        })
    }
}