package sv.com.castroluna.pocketm.go.fragment

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
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.captured_by_other_fragment.*
import kotlinx.android.synthetic.main.captured_by_other_fragment.imgFront
import kotlinx.android.synthetic.main.captured_by_other_fragment.imgRear
import kotlinx.android.synthetic.main.captured_by_other_fragment.lblPokeName
import kotlinx.android.synthetic.main.fragment_wild.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.castroluna.pocketm.go.R
import sv.com.castroluna.pocketm.go.networking.APISamaritanUtils
import sv.com.castroluna.pocketm.go.networking.ISamaritanService
import sv.com.castroluna.pocketm.go.service.AppDatabase
import sv.com.castroluna.pocketm.go.viewmodel.CapturedByOtherViewModel
import sv.com.castroluna.pocketm.go.viewmodel.WildFragmentViewModel


class WildFragment: Fragment() {
    companion object {
        fun newInstance() = WildFragment()
    }
    private val SHARED:String="spref.castroluna.pocketm.go"
    private var sharedPreferences: SharedPreferences? = null
    lateinit var iSamaritanService:ISamaritanService
    var movesList:ArrayList<String> = ArrayList()
    private lateinit var db: AppDatabase
    private lateinit var viewModel: WildFragmentViewModel
    val pkId:String?=""

    private val callback = OnMapReadyCallback { googleMap ->
        val latitude: String? = sharedPreferences?.getString("CAPT_LATITUDE", "0.0")
        val longitude: String? = sharedPreferences?.getString("CAPT_LONGITUDE", "0.0")
        val pkName:String? = sharedPreferences?.getString("SPOTTED", "0.0")

        val lat:Double = latitude?.toDouble() ?: 0.0
        val lng:Double = longitude?.toDouble() ?: 0.0
        val spot = LatLng(lat, lng)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spot, 16.0F))
        googleMap.addMarker(MarkerOptions()
                .position(spot)
                .title(pkName)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pokeball01)
                )
        )
        googleMap.setOnCameraMoveListener{

        }

        googleMap.setOnCameraIdleListener {

        }


        googleMap.setOnMarkerClickListener { marker ->
            if (!marker.isInfoWindowShown) {
                marker.hideInfoWindow()

            } else {
                marker.showInfoWindow()
            }
            true
        }


    }





    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(WildFragmentViewModel::class.java)
       return inflater.inflate(R.layout.fragment_wild, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences(SHARED, 0)
        db = AppDatabase.getInstance(requireActivity().application)
        iSamaritanService = APISamaritanUtils.getSamaritanService()!!
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        val pkName:String? = sharedPreferences?.getString("SPOTTED", "0.0")
        lblPokeName.text=pkName
        makeApiCall(pkName!!.toLowerCase())
        makeApiCallAbility(pkName!!.toLowerCase())

        CoroutineScope(Dispatchers.IO).launch {
            val pokemonCaptured = db.capturedPkmDao().countPokemonByName(pkName.capitalize())
            CoroutineScope(Dispatchers.Main).launch {
                if (pokemonCaptured<=0) {
                    imgCaptured.visibility = View.VISIBLE
                } else {
                    imgCaptured.visibility = View.INVISIBLE
                }
            }
        }
        btnCapture.setOnClickListener{ it->
            val pkName:String? = sharedPreferences?.getString("SPOTTED", "0.0")
            showdialog(pkName!!)
        }
    }

    fun showdialog(name: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Assign a name")
        val input = EditText(activity)

        input.setHint("New name? (Empty for default)")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)


        builder.setPositiveButton("OK") { dialog, which ->
            var newPokemonName = input.text.toString()
            if (newPokemonName.length <= 0) {
                newPokemonName = name
            }

            val latitude: String? = sharedPreferences?.getString("CAPT_LATITUDE", "0.0")
            val longitude: String? = sharedPreferences?.getString("CAPT_LONGITUDE", "0.0")

            val objPokemon = JsonObject()
            val pkData = JsonObject()
            pkData.addProperty("id",pkId)
            pkData.addProperty("name",newPokemonName)
            pkData.addProperty("lat",latitude!!.toDouble())
            pkData.addProperty("long",longitude!!.toDouble())
            objPokemon.add("pokemon",pkData)
            Log.d("RESULTADO", "${objPokemon}")

            val token: String? = sharedPreferences?.getString("TOKEN", "")
            postCaptured("Bearer $token", objPokemon)

            CoroutineScope(Dispatchers.IO).launch {
                val frontImage:String? = sharedPreferences?.getString("FRONT_IMAGE","")
                val pkId:String? = sharedPreferences?.getString("PK_ID","0")
                db.capturedPkmDao().insert(pkId!!,newPokemonName,frontImage!!)

            }
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(activity,"Pokemon successfully captured!",Toast.LENGTH_LONG).show()
            }


        }
        builder.setNegativeButton("Cancel", { dialog, which -> dialog.cancel() })

        builder.show()
    }


    fun postCaptured(tk: String, pk: JsonObject){
        val call = iSamaritanService.capturePokemon(tk, pk)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response != null) {
                    Log.d("RESULTADO", "${response.code()}")

                } else {

                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
               Log.d("WILD_FRAG",t.toString());
            }

        })
    }


    fun makeApiCallAbility(name: String){
        val vm = viewModel
        vm.getCapturedAbilityListDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {
                var types: String = ""

                for (tp in it.types!!) {
                    types += tp.type?.name!! + ", "
                }

                var moves: String = ""
                for (mv in it.moves!!) {
                    //moves += mv.move?.name!!+", "
                    movesList.add(mv.move?.name!!)
                }

                if (movesList.size >= 4) {
                    //Random
                    movesList.shuffle()
                    //lblType.text = ""
                    lblMove.text = "Moves: " + movesList[0] + ", " + movesList[1] + ", " + movesList[2] + ", " + movesList[3]
                } else {
                    for (m in movesList) {
                        moves += "$m, "
                        lblMove.text = moves
                    }
                }

                lblType.text = "Types: " + types
                //lblMove.text = "Move: "+moves
            }
        })
        vm.apiCallAbility(name)
    }

    fun makeApiCall(name: String){
        val vm = viewModel
        vm.getCapturedListDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {

                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString("FRONT_IMAGE", it.sprites?.front_default)
                editor.putString("PK_ID", it.sprites?.front_default?.split('/')?.get(8)?.replace(".png",""))

                editor.apply()
                Glide.with(imgFront).load(it.sprites?.front_default)
                        .placeholder(R.drawable.pokeball01)
                        .error(R.drawable.pokeball01)
                        .circleCrop()
                        .into(imgFront)


                Glide.with(imgRear).load(it.sprites?.back_default)
                        .placeholder(R.drawable.pokeball01)
                        .error(R.drawable.pokeball01)
                        .circleCrop()
                        .into(imgRear)

                //Get basic info


            }
        })

        vm.apiCall(name)
    }

//POST body to save pokemon

    class PokemonJSON{
        var id:Int=0;
        var name:String=""
        var lat:Double=0.0
        var lng:Double=0.0
    }

    class PostPokemon{
        var pokemon: PokemonJSON? =null
    }



}