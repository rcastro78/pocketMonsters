package sv.com.castroluna.pocketm.go.fragment

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.captured_fragment.*
import kotlinx.android.synthetic.main.my_team_fragment.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.castroluna.pocketm.go.R
import sv.com.castroluna.pocketm.go.adapter.CapturedAdapter
import sv.com.castroluna.pocketm.go.adapter.CapturedRecyclerViewAdapter
import sv.com.castroluna.pocketm.go.dao.CapturedPokemon
import sv.com.castroluna.pocketm.go.model.Captured
import sv.com.castroluna.pocketm.go.model.PokemonDataRoot
import sv.com.castroluna.pocketm.go.networking.APISamaritanUtils
import sv.com.castroluna.pocketm.go.networking.APIUtils
import sv.com.castroluna.pocketm.go.networking.IPokeService
import sv.com.castroluna.pocketm.go.networking.ISamaritanService
import sv.com.castroluna.pocketm.go.service.AppDatabase
import sv.com.castroluna.pocketm.go.util.OnItemClickListener
import sv.com.castroluna.pocketm.go.util.addOnItemClickListener
import sv.com.castroluna.pocketm.go.viewmodel.CapturedViewModel
import kotlin.coroutines.CoroutineContext


class CapturedFragment : Fragment() {

    companion object {
        fun newInstance() = CapturedFragment()
    }

    private lateinit var viewModel: CapturedViewModel
    lateinit var iPokeService: IPokeService
    lateinit var iSamaritanService: ISamaritanService
    private lateinit var adapter: CapturedRecyclerViewAdapter
    private val SHARED:String="spref.castroluna.pocketm.go"
    private var sharedPreferences: SharedPreferences? = null
    var captured:ArrayList<Captured> = ArrayList()
    private lateinit var db: AppDatabase



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.captured_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CapturedViewModel::class.java)
        iPokeService = APIUtils.getPokeService()!!
        iSamaritanService = APISamaritanUtils.getSamaritanService()!!
        db = AppDatabase.getInstance(requireActivity().application)
        sharedPreferences = activity?.getSharedPreferences(SHARED, 0)
        val token: String? = sharedPreferences?.getString("TOKEN", "")

        var data: List<CapturedPokemon> = ArrayList()
        CoroutineScope(Dispatchers.IO).launch {
            data = db.capturedPkmDao().getAll()
            if(data.isNotEmpty()){
                readData()
            }else{
                CoroutineScope(Dispatchers.Main).launch {
                   makeApiCall(token!!)
                }

            }
        }

        rvCaptured.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val p = captured[position]
                showdialog(p)


            }
        })

    }


    fun showdialog(p:Captured){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Are you sure to release ${p.name}?")
        builder.setPositiveButton("Yes") { dialog, which ->

        CoroutineScope(Dispatchers.IO).launch {
            db.capturedPkmDao().deleteByName(p.name!!)
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            readData()
        }

//api call to delete
val token: String? = sharedPreferences?.getString("TOKEN", "")
//releaseCaptured("Bearer $token",p.id.toString())
}
builder.setNegativeButton("Cancel", { dialog, which -> dialog.cancel() })

builder.show()
}


fun readData(){
var data: List<CapturedPokemon> = ArrayList()
captured.clear()

data = db.capturedPkmDao().getAll()
for (d in data) {
    val capt = Captured()
    capt.id = d.id?.toInt() ?:0
    capt.name = d.name!!
    capt.pkImage = d.pkImage!!
    captured.add(capt)
 }


CoroutineScope(Dispatchers.Main).launch {
adapter= CapturedRecyclerViewAdapter(captured)
val numberOfColumns = 3
rvCaptured.setLayoutManager(GridLayoutManager(activity, numberOfColumns))
rvCaptured.adapter = adapter
}

}

fun releaseCaptured(tk:String,id:String){
val call = iSamaritanService.deletePokemon(tk,id)
call.enqueue(object : Callback<JsonObject> {
override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
    if (response != null) {
        Log.d("RESULTADO", "${response.code()}")
        Toast.makeText(activity, "${response.code()}".toString(), Toast.LENGTH_LONG).show()
    } else {
        Log.d("RESULTADO", "Error en la petición")
        Toast.makeText(activity, "Error en la petición", Toast.LENGTH_LONG).show()
    }
}

override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
    TODO("Not yet implemented")
}

})
}



/*private fun getPokeData(captured: ArrayList<Captured>){
for(c in captured){
val call = iPokeService.getPokemonData(c.name?.toLowerCase()!!)
call.enqueue(object : Callback<PokemonDataRoot> {
    override fun onResponse(
        call: Call<PokemonDataRoot>?,
        response: Response<PokemonDataRoot>?
    ) {
        if (response != null) {
            val img = response.body().sprites?.front_default!!


            CoroutineScope(Dispatchers.IO).launch {
                db.capturedPkmDao().update(img, c.name!!)
            }

        }
    }

    override fun onFailure(call: Call<PokemonDataRoot>?, t: Throwable?) {
        Log.d("POKE_IMG", t.toString())

    }
})

adapter= CapturedRecyclerViewAdapter(captured)
val numberOfColumns = 3
rvCaptured.setLayoutManager(GridLayoutManager(activity, numberOfColumns))
rvCaptured.adapter = adapter

}
}*/


private fun makeApiCall(tk: String){
captured.clear()

/*CoroutineScope(Dispatchers.IO).launch {
db.capturedPkmDao().delete()
}*/

viewModel.getCapturedListDataObserver().observe(viewLifecycleOwner, {
if (it != null) {
    for (i in it) {
        val c = Captured()
        c.name = i.name
        c.id = i.id
        captured.add(c)
        CoroutineScope(Dispatchers.IO).launch {
            db.capturedPkmDao().insert(i.id.toString(), i.name!!, "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+i.id.toString()+".png")
        }
    }
}


})


viewModel.apiCall("Bearer $tk")

}





}