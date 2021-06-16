package sv.com.castroluna.pocketm.go.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sv.com.castroluna.pocketm.go.dao.*


@Database(entities = arrayOf(CommunityFriends::class,Team::class,CapturedPokemon::class,CommunityPokemon::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun friendDao():FriendsDAO
    abstract fun teamDao():TeamDAO
    abstract fun capturedPkmDao():CapturedPkmDAO
    abstract fun commPkmDao():CommPokemonDAO
    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                        "pokedb_03.db")
                    .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}