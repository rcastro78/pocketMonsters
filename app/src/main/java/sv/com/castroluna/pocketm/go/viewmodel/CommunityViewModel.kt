package sv.com.castroluna.pocketm.go.viewmodel

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.castroluna.pocketm.go.model.Community
import sv.com.castroluna.pocketm.go.model.PokemonDataRoot
import sv.com.castroluna.pocketm.go.networking.APISamaritanUtils
import sv.com.castroluna.pocketm.go.networking.APIUtils
import sv.com.castroluna.pocketm.go.networking.IPokeService
import sv.com.castroluna.pocketm.go.networking.ISamaritanService
import sv.com.castroluna.pocketm.go.service.AppDatabase


class CommunityViewModel : ViewModel() {
    var communityListData:MutableLiveData<Community>
    var pokemonListData:MutableLiveData<PokemonDataRoot>
    var iSamaritanService:ISamaritanService
    var iPokeService:IPokeService

    init {
        communityListData = MutableLiveData()
        pokemonListData = MutableLiveData()
        iSamaritanService = APISamaritanUtils.getSamaritanService()!!
        iPokeService = APIUtils.getPokeService()!!

    }

    fun getCommunityListDataObserver():MutableLiveData<Community>{
        return communityListData
    }

    /*
    fun getPokemonListDataObserver():MutableLiveData<PokemonDataRoot>{
        return pokemonListData
    }

    fun pokemonApiCall(pokemonName:String){
        val call = iPokeService.getPokemonData(pokemonName)
        call.enqueue(object : Callback<PokemonDataRoot>{
            override fun onResponse(call: Call<PokemonDataRoot>?, response: Response<PokemonDataRoot>?) {
                if (response != null) {
                    pokemonListData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<PokemonDataRoot>?, t: Throwable?) {
                Log.d("POKE_TEAM", t.toString())
                pokemonListData.postValue(null)
            }
        })
    }
*/

    fun apiCall(token:String){
        val call = iSamaritanService.getCommunity(token)
        call.enqueue(object : Callback<Community>{
            override fun onResponse(call: Call<Community>?, response: Response<Community>?) {
                if (response != null) {
                    Log.d("POKE_COMM",response.message())
                    communityListData.postValue(response.body())
                }else{
                    Log.d("POKE_COMM","Error")
                    communityListData.postValue(null)
                }



            }

            override fun onFailure(call: Call<Community>?, t: Throwable?) {
                communityListData.postValue(null)

            }

        })
    }




}