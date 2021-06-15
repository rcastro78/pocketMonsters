package sv.com.castroluna.pocketm.go.dao

import androidx.room.*

@Entity(tableName = CapturedPokemon.TABLE_NAME)
class CapturedPokemon(@PrimaryKey(autoGenerate = true) val uid: Int,
                      @ColumnInfo(name = "id") val id: String?,
                      @ColumnInfo(name = "name") val name: String?,
                      @ColumnInfo(name = "pkImage") var pkImage: String?,){

    companion object {
        const val TABLE_NAME = "captured_pokemon"
    }


}


@Dao
interface CapturedPkmDAO{
    @Query("SELECT * FROM "+CapturedPokemon.TABLE_NAME)
    fun getAll(): List<CapturedPokemon>

    @Query("SELECT COUNT(*) FROM "+CapturedPokemon.TABLE_NAME+" WHERE name = :name")
    fun countPokemonByName(name:String): Int



    @Query("INSERT INTO "+CapturedPokemon.TABLE_NAME+"(id,name,pkImage) VALUES(:id,:name,:pkImage)")
    fun insert(id:String,name:String,pkImage:String)

    @Query("DELETE FROM "+CapturedPokemon.TABLE_NAME+" WHERE name= :name")
    fun deleteByName(name:String)

    @Query("UPDATE "+CapturedPokemon.TABLE_NAME+" SET pkImage= :pkImage WHERE name= :name")
    fun update(pkImage:String,name:String)


    //USED FOR TEST
    @Query("SELECT name FROM "+CapturedPokemon.TABLE_NAME+" WHERE name = :name")
    fun getPokemonByName(name:String): String
    //END USED FOR TEST FUNCTIONS

}