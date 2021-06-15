package sv.com.castroluna.pocketm.go.viewmodel

import android.os.LimitExceededException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.castroluna.pocketm.go.model.PokemonPosition
import sv.com.castroluna.pocketm.go.model.PokemonRoot
import sv.com.castroluna.pocketm.go.networking.APIUtils
import sv.com.castroluna.pocketm.go.networking.IPokeService


class ExploreViewModel : ViewModel() {
    lateinit var pokemonListData : MutableLiveData<PokemonRoot>
    private var iPokeService: IPokeService

    init {
        pokemonListData = MutableLiveData()
        iPokeService = APIUtils.getPokeService()!!
    }

    fun getPokemonListDataObserver():MutableLiveData<PokemonRoot>{
        return pokemonListData
    }

    fun apiCall(limit:String){
        val call = iPokeService.getPokemon(limit)
        call.enqueue(object : Callback<PokemonRoot> {
            override fun onResponse(
                call: Call<PokemonRoot>?,
                response: Response<PokemonRoot>?
            ) {
                if (response != null) {
                    pokemonListData.postValue(response.body())
                }else{
                    pokemonListData.postValue(null)
                }

            }

            override fun onFailure(call: Call<PokemonRoot>?, t: Throwable?) {
                pokemonListData.postValue(null)
                Log.d("ERR_POK",t.toString())
            }

        })
    }

}