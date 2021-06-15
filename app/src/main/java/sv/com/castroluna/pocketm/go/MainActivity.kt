package sv.com.castroluna.pocketm.go

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import sv.com.castroluna.pocketm.go.fragment.CapturedFragment
import sv.com.castroluna.pocketm.go.fragment.CommunityFragment
import sv.com.castroluna.pocketm.go.fragment.ExploreFragment
import sv.com.castroluna.pocketm.go.fragment.MyTeamFragment
import sv.com.castroluna.pocketm.go.networking.APISamaritanUtils
import sv.com.castroluna.pocketm.go.networking.APIUtils
import sv.com.castroluna.pocketm.go.networking.IPokeService
import sv.com.castroluna.pocketm.go.networking.ISamaritanService
import sv.com.castroluna.pocketm.go.service.AppDatabase
import sv.com.castroluna.pocketm.go.service.DBService
import sv.com.castroluna.pocketm.go.util.TabAdapter

class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val SHARED:String="spref.castroluna.pocketm.go"
    private var mViewPager: ViewPager? = null
    private var adapter: TabAdapter? = null
    private var tabLayout: TabLayout? = null
    private var sharedPreferences: SharedPreferences? = null
    private lateinit var  iSamaritanService: ISamaritanService

    private lateinit var db:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(SHARED, 0)
        db = AppDatabase.getInstance(this)
        iSamaritanService = APISamaritanUtils.getSamaritanService()!!

        tabLayout = findViewById(R.id.tabs)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.container)
        mViewPager!!.adapter = mSectionsPagerAdapter
        tabLayout = findViewById(R.id.tabs)
        adapter = TabAdapter(supportFragmentManager)
        adapter!!.addFragment(ExploreFragment(), resources.getString(R.string.explore))
        adapter!!.addFragment(CommunityFragment(), resources.getString(R.string.community))
        adapter!!.addFragment(MyTeamFragment(), resources.getString(R.string.my_team))
        adapter!!.addFragment(CapturedFragment(), resources.getString(R.string.captured))

        mViewPager!!.adapter = adapter
        tabLayout!!.setupWithViewPager(mViewPager)

        val intent = Intent(this, DBService::class.java)
        startService(intent)

    }





    class PlaceholderFragment : Fragment() {
        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_explore, container, false)
        }

        companion object {
            private const val ARG_SECTION_NUMBER = "section_number"
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }


    class SectionsPagerAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            return 4
        }
    }


}