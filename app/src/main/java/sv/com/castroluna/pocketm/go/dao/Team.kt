package sv.com.castroluna.pocketm.go.dao

import androidx.room.*

@Entity(tableName = Team.TABLE_NAME)
class Team(@PrimaryKey() val uid: Int,
           @ColumnInfo(name = "name") val name: String?,
           @ColumnInfo(name = "hp") val hp: String?,
           @ColumnInfo(name = "imgUrl") val imgUrl: String?,
           @ColumnInfo(name = "type") val type: String?,
           @ColumnInfo(name = "captured") val captured: String?,
) {
    companion object {
        const val TABLE_NAME = "myTeam"
    }
}

@Dao
interface TeamDAO{
    @Query("SELECT * FROM "+Team.TABLE_NAME)
    fun getAll(): List<Team>

    @Query("INSERT INTO "+Team.TABLE_NAME+"(name,hp,imgUrl,type,captured) VALUES(:name,:hp,:imgUrl,:type,:captured)")
    fun insert(name:String,hp:String,imgUrl:String,type:String,captured:String)

    @Query("DELETE FROM "+Team.TABLE_NAME)
    fun delete()

    @Query("UPDATE "+Team.TABLE_NAME+" SET type= :type WHERE name= :name")
    fun update(type:String, name: String)
}
