package sv.com.castroluna.pocketm.go.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.community_fragment.*
import sv.com.castroluna.pocketm.go.DetailActivity
import sv.com.castroluna.pocketm.go.R
import sv.com.castroluna.pocketm.go.adapter.FoesRecyclerViewAdapter
import sv.com.castroluna.pocketm.go.adapter.FriendsRecyclerViewAdapter
import sv.com.castroluna.pocketm.go.model.Foe
import sv.com.castroluna.pocketm.go.model.Friend
import sv.com.castroluna.pocketm.go.networking.APIUtils
import sv.com.castroluna.pocketm.go.networking.IPokeService
import sv.com.castroluna.pocketm.go.service.AppDatabase
import sv.com.castroluna.pocketm.go.service.DBService
import sv.com.castroluna.pocketm.go.util.OnItemClickListener
import sv.com.castroluna.pocketm.go.util.addOnItemClickListener
import sv.com.castroluna.pocketm.go.viewmodel.CommunityViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CommunityFragment : Fragment() {
    private val SHARED:String="spref.castroluna.pocketm.go"
    private var sharedPreferences: SharedPreferences? = null
    var friendsList:ArrayList<Friend> = ArrayList()
    var pokemons:ArrayList<String> = ArrayList()
    var foesList:ArrayList<Foe> = ArrayList()

    lateinit var iPokeService:IPokeService
    private lateinit var friendsAdapter: FriendsRecyclerViewAdapter
    private lateinit var foesAdapter: FoesRecyclerViewAdapter
    private lateinit var db: AppDatabase
    companion object {
        fun newInstance() = CommunityFragment()
    }

    private lateinit var viewModel: CommunityViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.community_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences(SHARED, 0)
        val token: String? = sharedPreferences?.getString("TOKEN", "")
        db = AppDatabase.getInstance(requireActivity().application)
        iPokeService = APIUtils.getPokeService()!!
        viewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)
        makeApiCall(token!!)
        val font1 = Typeface.createFromAsset(activity?.assets, "fonts/Lato-Bold.ttf")
        lblFoes.typeface = font1
        lblFriends.typeface = font1

        rvFriends.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val friend = friendsList[position]
                val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
                editor!!.putInt("TYPE_DETAIL",2)
                editor.putString("POKEMON_NAME","${friend.pokemon?.name}")
                editor.putString("POKEMON_CAPTURED","${friend.pokemon?.captured_at}")
                editor.putString("TRAINER","${friend.name}")
                editor.apply()
                val intent = Intent (activity, DetailActivity::class.java)
                activity?.startActivity(intent)
            }
        })

        rvFoes.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val foe = foesList[position]
                val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
                editor!!.putInt("TYPE_DETAIL",2)
                editor.putString("POKEMON_NAME","${foe.pokemon?.name}")
                editor.putString("POKEMON_CAPTURED","${foe.pokemon?.captured_at}")
                editor.putString("TRAINER","${foe.name}")
                editor.apply()
                val intent = Intent (activity, DetailActivity::class.java)
                activity?.startActivity(intent)


            }
        })



        // TODO: Use the ViewModel
    }



    fun makeApiCall(tk: String){

        /*CoroutineScope(Dispatchers.IO).launch {
            db.friendDao().delete()
        }*/
        friendsList.clear()
        foesList.clear()
        val vm = viewModel
        vm.getCommunityListDataObserver().observe(viewLifecycleOwner, { it ->
            if (it != null) {
                it.friends?.forEach {
                    //Log.d("POKE_COMM","${it.name}")
                    val friend = Friend()
                    friend.name = it.name
                    friend.pokemon = it.pokemon
                    friend.pokemon?.captured_at = it.pokemon?.captured_at
                    //db.commPkmDao().insert("0",it.pokemon?.name!!,"")
                    //makePokemonApiCall(it.pokemon?.name!!)
                    friend.pkImage = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+ it.pokemon?.id.toString()+".png"


                    friendsList.add(friend)
                }
                it.foes?.forEach {
                    val foe = Foe()
                    foe.name = it.name
                    foe.pokemon = it.pokemon
                    foe.pokemon?.captured_at = it.pokemon?.captured_at
                    foe.pkImage="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+ it.pokemon?.id.toString()+".png"
                    foesList.add(foe)
                }
            }


            ///getFriends()

          //getPokemonData(friendsList)


            friendsAdapter = FriendsRecyclerViewAdapter(friendsList)
            foesAdapter = FoesRecyclerViewAdapter(foesList)
            val horizontalLayoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rvFriends.layoutManager = horizontalLayoutManager
            rvFriends.adapter = friendsAdapter

            val foeHorizontalLayoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

            rvFoes.layoutManager = foeHorizontalLayoutManager
            rvFoes.adapter = foesAdapter



        })

        vm.apiCall("Bearer $tk")
    }



/*
    fun getPokemonData(friend:ArrayList<Friend>){
        var image:String=""

        for(f in friend) {

            val call = f.pokemon?.name?.let { iPokeService.getPokemonData(it) }
            if (call != null) {
                call.enqueue(object : Callback<PokemonDataRoot> {
                    override fun onResponse(
                        call: Call<PokemonDataRoot>?,
                        response: Response<PokemonDataRoot>?
                    ) {
                        if (response != null) {
                            response.body().sprites?.front_shiny?.let {
                                image = it
                                f.pkImage = image
                            }

                        }
                    }

                    override fun onFailure(call: Call<PokemonDataRoot>?, t: Throwable?) {

                    }

                })
            }
        }



        friendsAdapter = FriendsRecyclerViewAdapter(friend)

        val horizontalLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvFriends.layoutManager = horizontalLayoutManager
        rvFriends.adapter = friendsAdapter


    }
*/


}