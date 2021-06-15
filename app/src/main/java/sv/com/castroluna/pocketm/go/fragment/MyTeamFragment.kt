package sv.com.castroluna.pocketm.go.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.community_fragment.*
import kotlinx.android.synthetic.main.my_team_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.castroluna.pocketm.go.R
import sv.com.castroluna.pocketm.go.adapter.MyTeamRecyclerViewAdapter
import sv.com.castroluna.pocketm.go.dao.Team
import sv.com.castroluna.pocketm.go.model.MyTeam
import sv.com.castroluna.pocketm.go.model.PokemonDataRoot
import sv.com.castroluna.pocketm.go.networking.APIUtils
import sv.com.castroluna.pocketm.go.networking.IPokeService
import sv.com.castroluna.pocketm.go.service.AppDatabase
import sv.com.castroluna.pocketm.go.util.OnItemClickListener
import sv.com.castroluna.pocketm.go.util.addOnItemClickListener
import sv.com.castroluna.pocketm.go.viewmodel.MyTeamViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyTeamFragment : Fragment() {

    companion object {
        fun newInstance() = MyTeamFragment()
    }
    lateinit var iPokeService: IPokeService
    private lateinit var adapter:MyTeamRecyclerViewAdapter
    private lateinit var viewModel: MyTeamViewModel
    private val SHARED:String="spref.castroluna.pocketm.go"
    private var sharedPreferences: SharedPreferences? = null
    var myTeam:ArrayList<MyTeam> = ArrayList()
    private lateinit var db: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.my_team_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyTeamViewModel::class.java)
        iPokeService = APIUtils.getPokeService()!!
        db = AppDatabase.getInstance(requireActivity().application)
        sharedPreferences = activity?.getSharedPreferences(SHARED, 0)
        val token: String? = sharedPreferences?.getString("TOKEN", "")

        var data: List<Team> = ArrayList()
        CoroutineScope(Dispatchers.IO).launch {
            data = db.teamDao().getAll()
            if(data.isNotEmpty()){
                readData()
            }else{

                CoroutineScope(Dispatchers.Main).launch {
                    makeApiCall(token!!)
                }

            }
        }



                //makeApiCall(token!!)


        // TODO: Use the ViewModel
    }


    /*
    * adapter=MyTeamRecyclerViewAdapter(myTeam)
        val vLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvTeam.layoutManager = vLayoutManager
        rvTeam.adapter = adapter
    * */

    fun readData(){
        var data: List<Team> = ArrayList()
        myTeam.clear()

            data = db.teamDao().getAll()

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            val output = SimpleDateFormat("MM/dd/yyyy")



            for(d in data){
                val strDate = d.captured!!.replace("Z","")
                val dt:Date = sdf.parse(strDate)
                val fDate:String = output.format(dt)
                val team = MyTeam()
                team.name = d.name!!
                team.pokeUrl = d.imgUrl!!
                team.captured_at = fDate
                team.hp = d.hp
                team.type = d.type
                myTeam.add(team)
            }



        CoroutineScope(Dispatchers.Main).launch {
            adapter=MyTeamRecyclerViewAdapter(myTeam)
            val vLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvTeam.layoutManager = vLayoutManager
            rvTeam.adapter = adapter
        }


    }




    private fun getPokeData(myTeam: ArrayList<MyTeam>){
        for(m in myTeam) {
            var tps:String=""
            val call = iPokeService.getPokemonData(m.name?.toLowerCase()!!)
            call.enqueue(object : Callback<PokemonDataRoot> {
                override fun onResponse(
                    call: Call<PokemonDataRoot>?,
                    response: Response<PokemonDataRoot>?
                ) {
                    if (response != null) {
                        val img = response.body().sprites?.front_default!!
                        for(t in response.body().types!!){
                            tps += t.name+", "
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            db.teamDao().update(tps, m.name!!)
                        }

                    }
                }

                override fun onFailure(call: Call<PokemonDataRoot>?, t: Throwable?) {
                    Log.d("POKE_IMG", t.toString())

                }
            })

            adapter=MyTeamRecyclerViewAdapter(myTeam)
            val vLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvTeam.layoutManager = vLayoutManager
            rvTeam.adapter = adapter

        }





    }


    private fun makeApiCall(tk: String){
        myTeam.clear()
        CoroutineScope(Dispatchers.IO).launch {
            db.teamDao().delete()
        }
        val vm = viewModel
        vm.getMyTeamListDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {
                for (i in it) {
                    val t = MyTeam()
                    t.id = i.id
                    t.name = i.name
                    t.captured_at = i.captured_at

                    myTeam.add(t)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.teamDao().insert(i.name!!, "250", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+i.id.toString()+".png", "", i.captured_at!!)
                    }


                }
            } else {

            }

            getPokeData(myTeam)


        })

        vm.apiCall("Bearer $tk")





    }

}