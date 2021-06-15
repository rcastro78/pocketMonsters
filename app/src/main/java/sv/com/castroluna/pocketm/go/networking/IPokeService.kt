package sv.com.castroluna.pocketm.go.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sv.com.castroluna.pocketm.go.model.Captured
import sv.com.castroluna.pocketm.go.model.PokemonAbility
import sv.com.castroluna.pocketm.go.model.PokemonDataRoot
import sv.com.castroluna.pocketm.go.model.PokemonRoot

interface IPokeService {
    @GET("pokemon")
    fun getPokemon(@Query("limit") limit:String): Call<PokemonRoot>

    @GET("pokemon-form/{name}")
    fun getPokemonData(@Path("name") name:String): Call<PokemonDataRoot>

    @GET("pokemon/{name}")
    fun getPokemonInfo(@Path("name") name:String): Call<PokemonAbility>

}