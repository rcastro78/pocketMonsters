package sv.com.castroluna.pocketm.go.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import sv.com.castroluna.pocketm.go.DetailActivity
import sv.com.castroluna.pocketm.go.R
import sv.com.castroluna.pocketm.go.model.PokemonPosition
import sv.com.castroluna.pocketm.go.viewmodel.CapturedByOtherViewModel
import sv.com.castroluna.pocketm.go.viewmodel.ExploreViewModel
import java.util.*
import kotlin.collections.ArrayList

class ExploreFragment : Fragment() {
    private val SHARED:String="spref.castroluna.pocketm.go"
    private var sharedPreferences: SharedPreferences? = null
    private var pokemonLocations:ArrayList<PokemonPosition> = ArrayList()
    private lateinit var viewModel:ExploreViewModel
    private val callback = OnMapReadyCallback { googleMap ->
        val seattle = LatLng(47.608013, -122.335167)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seattle, 16.0F))
        makeApiCall(googleMap)

            googleMap.setOnCameraMoveListener{
                pokemonLocations.clear()
                googleMap.clear()

            }

            googleMap.setOnCameraIdleListener {
                makeApiCall(googleMap)
            }


            googleMap.setOnMarkerClickListener { marker ->
                if (!marker.isInfoWindowShown) {
                    marker.hideInfoWindow()
                    //Toast.makeText(activity,"Le diste click a "+marker.title,Toast.LENGTH_LONG).show()
                    val latitude:Double = marker.position.latitude
                    val longitude:Double = marker.position.longitude

                    val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
                    editor!!.putString ("CAPT_LATITUDE",latitude.toString())
                    editor!!.putString ("CAPT_LONGITUDE",longitude.toString())
                    editor!!.putString ("SPOTTED",marker.title)
                    editor!!.putInt("TYPE_DETAIL",1)
                    editor.apply()
                    val intent = Intent (activity, DetailActivity::class.java)
                    activity?.startActivity(intent)


                } else {
                    marker.showInfoWindow()
                }
                true
            }


    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //makeApiCall()
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences(SHARED, 0)
        viewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }






    private fun getRandomLocation(point: LatLng, radius: Int): LatLng? {
        val randomPoints: MutableList<LatLng> = ArrayList()
        val randomDistances: MutableList<Float> = ArrayList()
        val myLocation = Location("")
        myLocation.setLatitude(point.latitude)
        myLocation.setLongitude(point.longitude)

        for (i in 0..9) {
            val x0 = point.latitude
            val y0 = point.longitude
            val random = Random()

            val radiusInDegrees = (radius / 111000f).toDouble()
            val u: Double = random.nextDouble()
            val v: Double = random.nextDouble()
            val w = radiusInDegrees * Math.sqrt(u)
            val t = 2 * Math.PI * v
            val x = w * Math.cos(t)
            val y = w * Math.sin(t)

            // Adjust the x-coordinate for the shrinking of the east-west distances
            val new_x = x / Math.cos(y0)
            val foundLatitude = new_x + x0
            val foundLongitude = y + y0
            val randomLatLng = LatLng(foundLatitude, foundLongitude)
            randomPoints.add(randomLatLng)
            val l1 = Location("")
            l1.latitude = randomLatLng.latitude
            l1.longitude = randomLatLng.longitude
            randomDistances.add(l1.distanceTo(myLocation))
        }
        val indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances))
        return randomPoints[indexOfNearestPointToCentre]
    }


    private fun makeApiCall(googleMap: GoogleMap) {
        val vm = viewModel
        vm.getPokemonListDataObserver().observe(viewLifecycleOwner, { it ->
            if (it != null) {
                pokemonLocations.clear()
                it.results?.forEach {
                    val pokeList = PokemonPosition()
                    val seattle = LatLng(47.608013, -122.335167)
                    val pos = getRandomLocation(seattle, 10_000)
                    pokeList.name = it.name
                    pokeList.url = it.url

                    pokeList.latitude = pos!!.latitude
                    pokeList.longitude = pos!!.longitude
                    pokemonLocations.add(pokeList)


                }

                for (pokemon: PokemonPosition in pokemonLocations) {
                    val location = LatLng(pokemon.latitude, pokemon.longitude)
                    googleMap.addMarker(MarkerOptions()
                            .position(location)
                            .title(pokemon.name)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pokeball01)
                            )
                    )
                }


            } else {
                Toast.makeText(activity, "Error showing items in map", Toast.LENGTH_LONG).show()
            }
        })
        vm.apiCall("50")
    }




}