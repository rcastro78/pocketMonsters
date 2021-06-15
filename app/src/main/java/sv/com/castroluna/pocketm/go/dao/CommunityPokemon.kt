package sv.com.castroluna.pocketm.go.dao

import androidx.room.*

@Entity(tableName = CommunityPokemon.TABLE_NAME)
class CommunityPokemon(@PrimaryKey(autoGenerate = true) val uid: Int,
                       @ColumnInfo(name = "id") val id: String?,
                       @ColumnInfo(name = "name") val name: String?,
                       @ColumnInfo(name = "pkImage") var pkImage: String?) {

    companion object {
        const val TABLE_NAME = "community_pokemon"
    }




}


@Dao
interface CommPokemonDAO{
    @Query("SELECT * FROM "+CommunityPokemon.TABLE_NAME+" WHERE name =:name")
    fun getCommPokemon(name:String): List<CommunityPokemon>

    @Query("INSERT INTO "+CommunityPokemon.TABLE_NAME+"(id,name,pkImage) VALUES(:id,:name,:pkImage)")
    fun insert(id:String,name:String,pkImage:String)

    @Query("DELETE FROM "+CommunityPokemon.TABLE_NAME)
    fun delete()

    @Query("UPDATE "+CommunityPokemon.TABLE_NAME+" SET pkImage= :pkImage WHERE name= :name")
    fun update(pkImage:String,name:String)

}