package sv.com.castroluna.pocketm.go.networking

class APIUtils {
    companion object {
        private const val POKE_URL = "https://pokeapi.co/api/v2/"

        fun getPokeService(): IPokeService? {
            return RetrofitClient.getClient(POKE_URL)?.create(IPokeService::class.java)
        }

    }
}