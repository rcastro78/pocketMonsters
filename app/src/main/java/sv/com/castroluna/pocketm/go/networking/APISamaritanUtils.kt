package sv.com.castroluna.pocketm.go.networking

class APISamaritanUtils {
    companion object{
        private const val SAMARITAN_URL = "https://us-central1-samaritan-android-assignment.cloudfunctions.net/"
        fun getSamaritanService(): ISamaritanService? {
            return SamaritanRetrofitClient.getClient(SAMARITAN_URL)?.create(ISamaritanService::class.java)
        }
    }
}