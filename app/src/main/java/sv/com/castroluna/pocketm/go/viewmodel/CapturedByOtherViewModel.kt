package sv.com.castroluna.pocketm.go.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.castroluna.pocketm.go.model.Captured
import sv.com.castroluna.pocketm.go.model.PokemonAbility
import sv.com.castroluna.pocketm.go.model.PokemonDataRoot
import sv.com.castroluna.pocketm.go.networking.APISamaritanUtils
import sv.com.castroluna.pocketm.go.networking.APIUtils
import sv.com.castroluna.pocketm.go.networking.IPokeService


class CapturedByOtherViewModel: ViewModel()  {
    var pokemonListData: MutableLiveData<PokemonDataRoot>
    var pokemonAbilitiesListData: MutableLiveData<PokemonAbility>
    private var iPokeService: IPokeService
    init {
        iPokeService = APIUtils.getPokeService()!!
        pokemonListData = MutableLiveData()
        pokemonAbilitiesListData = MutableLiveData()
    }

    fun getCapturedListDataObserver():MutableLiveData<PokemonDataRoot>{
        return pokemonListData
    }

    fun getCapturedAbilityListDataObserver():MutableLiveData<PokemonAbility>{
        return pokemonAbilitiesListData
    }

fun apiCallAbility(name:String){
    val call = iPokeService.getPokemonInfo(name)
    call.enqueue(object : Callback<PokemonAbility>{
        override fun onResponse(call: Call<PokemonAbility>?, response: Response<PokemonAbility>?) {
            if (response != null) {
                pokemonAbilitiesListData.postValue(response.body())
            }
        }

        override fun onFailure(call: Call<PokemonAbility>?, t: Throwable?) {
            pokemonAbilitiesListData.postValue(null)
        }

    })
}


    fun apiCall(name:String){
        val call = iPokeService.getPokemonData(name)
        call.enqueue(object:Callback<PokemonDataRoot>{
            override fun onResponse(
                call: Call<PokemonDataRoot>?,
                response: Response<PokemonDataRoot>?
            ) {
                if (response != null) {
                    pokemonListData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<PokemonDataRoot>?, t: Throwable?) {
                pokemonListData.postValue(null)
            }

        })
    }





}