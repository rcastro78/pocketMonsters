package sv.com.castroluna.pocketm.go.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response
import sv.com.castroluna.pocketm.go.model.Captured
import sv.com.castroluna.pocketm.go.model.MyTeam
import sv.com.castroluna.pocketm.go.model.PokemonDataRoot
import sv.com.castroluna.pocketm.go.networking.APISamaritanUtils
import sv.com.castroluna.pocketm.go.networking.APIUtils
import sv.com.castroluna.pocketm.go.networking.IPokeService
import sv.com.castroluna.pocketm.go.networking.ISamaritanService
import javax.security.auth.callback.Callback

class CapturedViewModel : ViewModel() {
    var capturedListData: MutableLiveData<List<Captured>>
    var pokemonListImages: MutableLiveData<PokemonDataRoot>
    private var iSamaritanService: ISamaritanService
    private var iPokeService: IPokeService
    init {
        iSamaritanService = APISamaritanUtils.getSamaritanService()!!
        iPokeService = APIUtils.getPokeService()!!
        capturedListData = MutableLiveData()
        pokemonListImages = MutableLiveData()
    }

    fun getCapturedListDataObserver():MutableLiveData<List<Captured>>{
        return capturedListData
    }

    fun apiCall(tk:String){
        val call = iSamaritanService.getCaptured(tk)
        call.enqueue(object : retrofit2.Callback<List<Captured>> {
            override fun onResponse(call: Call<List<Captured>>?, response: Response<List<Captured>>?) {
                if (response != null) {
                    capturedListData.postValue(response.body())
                }else{
                    Log.d("POKE_CAP","Error poke capt")
                    capturedListData.postValue(null)
                }
            }
            override fun onFailure(call: Call<List<Captured>>?, t: Throwable?) {
                Log.d("POKE_CAP", t.toString())
                capturedListData.postValue(null)
            }
        })



    }

}