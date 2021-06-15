package sv.com.castroluna.pocketm.go

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sv.com.castroluna.pocketm.go.fragment.CapturedByOtherFragment
import sv.com.castroluna.pocketm.go.fragment.WildFragment

class DetailActivity : AppCompatActivity() {
    private val SHARED:String="spref.castroluna.pocketm.go"
    private var sharedPreferences: SharedPreferences? = null
    val WILD=1
    val COMMUNITY=2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        sharedPreferences = getSharedPreferences(SHARED, 0)
        val typeDetail:Int = sharedPreferences!!.getInt("TYPE_DETAIL",-1)


        val capturedByOtherFragment : CapturedByOtherFragment = CapturedByOtherFragment.newInstance()
        val wildFragment: WildFragment = WildFragment.newInstance()

        if (savedInstanceState == null) {
           if(typeDetail==COMMUNITY){
               supportFragmentManager
                       .beginTransaction()
                       .add(R.id.frame_container, capturedByOtherFragment, "captured")
                       .commit()
           }

            if(typeDetail==WILD){
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.frame_container, wildFragment, "wild")
                        .commit()
            }



        }



    }
}