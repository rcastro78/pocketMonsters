package sv.com.castroluna.pocketm.go.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.captured_by_other_fragment.*
import sv.com.castroluna.pocketm.go.R
import sv.com.castroluna.pocketm.go.viewmodel.CapturedByOtherViewModel

import java.text.SimpleDateFormat
import java.util.*

class CapturedByOtherFragment : Fragment() {

    private val SHARED:String="spref.castroluna.pocketm.go"
    private var sharedPreferences: SharedPreferences? = null
    private lateinit var viewModel: CapturedByOtherViewModel
    var movesList:ArrayList<String> = ArrayList()
    companion object {
        fun newInstance() = CapturedByOtherFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.captured_by_other_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences(SHARED, 0)
        viewModel = ViewModelProvider(this).get(CapturedByOtherViewModel::class.java)
        val pokeName:String = sharedPreferences?.getString("POKEMON_NAME","")!!
        val trName:String = sharedPreferences?.getString("TRAINER","")!!
        lblPokeName.text=pokeName
        lblCapturedBy.text="Captured by "+trName

        val dateCaptured:String = sharedPreferences?.getString("POKEMON_CAPTURED","")!!

        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US)
        val output = SimpleDateFormat("MM/dd/yyyy")
        val date:Date = sdf.parse(dateCaptured)
        val dateFormatted = changeDateFormat(dateCaptured)

        lblCaptured.text="Captured: "+dateFormatted


        if(trName == "Jessie") {imgCapturedBy.setImageResource(R.drawable.jessie)}
        if(trName == "James") {imgCapturedBy.setImageResource(R.drawable.james1)}
        if(trName == "Meowth") {imgCapturedBy.setImageResource(R.drawable.meowth)}
        if(trName == "Misty") {imgCapturedBy.setImageResource(R.drawable.misty01)}
        if(trName == "Brock") {imgCapturedBy.setImageResource(R.drawable.brock)}
        if(trName == "Tracey Sketchit") {imgCapturedBy.setImageResource(R.drawable.tracey)}
        if(trName == "May") {imgCapturedBy.setImageResource(R.drawable.may_sk)}
        if(trName == "Gary Oak") {imgCapturedBy.setImageResource(R.drawable.gary)}
        makeApiCall(pokeName.toLowerCase())
        makeApiCallAbility(pokeName.toLowerCase())

    }

    fun changeDateFormat(dateCaptured:String): String {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US)
        val output = SimpleDateFormat("MM/dd/yyyy")
        val date:Date = sdf.parse(dateCaptured)
        return output.format(date)
    }


    fun makeApiCallAbility(name:String){
        val vm =viewModel
        vm.getCapturedAbilityListDataObserver().observe(viewLifecycleOwner,{
            if(it!=null){
                var types:String=""

                for(tp in it.types!!){
                    types += tp.type?.name!!+", "
                }

                var moves:String=""
                for(mv in it.moves!!){
                    movesList.add(mv.move?.name!!)
                }

                lblType.text="Types: "+types


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


            }
        })
        vm.apiCallAbility(name)
    }

    fun makeApiCall(name:String){
        val vm = viewModel
        vm.getCapturedListDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {
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

}