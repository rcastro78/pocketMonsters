package sv.com.castroluna.pocketm.go.test

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import sv.com.castroluna.pocketm.go.dao.CapturedPkmDAO
import sv.com.castroluna.pocketm.go.service.AppDatabase
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CapturedByOtherFragmentTest : TestCase() {

    fun testChangeDateFormat() {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
        val output = SimpleDateFormat("MM/dd/yyyy")


      val startDate:String="Fri Mar 24 10:23:42 CST 2000"
      val expectedDate:String="03/24/2000"
      val date: Date = sdf.parse(startDate)
      val formattedDate=output.format(date)
      Assert.assertThat(formattedDate, Matchers.equalTo(expectedDate))


    }

    private lateinit var capturedPkmDAO: CapturedPkmDAO
    private lateinit var db: AppDatabase
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java).build()
        capturedPkmDAO = db.capturedPkmDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writePokemonTest() {
        capturedPkmDAO.insert("1","pokename","url")
        val byName = capturedPkmDAO.getPokemonByName("pokename")
        Assert.assertThat(byName, Matchers.equalTo("pokename"))
    }



}