package sv.com.castroluna.pocketm.go.dao

import androidx.room.*


@Entity(tableName = CommunityFriends.TABLE_NAME)
class CommunityFriends(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "pkImage") var pkImage: String?,
    @ColumnInfo(name = "pokemon") val pokemon: String?,
    @ColumnInfo(name = "lastCaptured") val lastCaptured: String?
    )
{
    companion object {
        const val TABLE_NAME = "friends"
    }
}
@Dao
interface FriendsDAO{
    @Query("SELECT * FROM "+CommunityFriends.TABLE_NAME)
    fun getAll(): List<CommunityFriends>

    @Insert
    fun insert(friend: CommunityFriends)

    @Query("DELETE FROM "+CommunityFriends.TABLE_NAME)
    fun delete()

    @Query("UPDATE "+CommunityFriends.TABLE_NAME+" SET pkImage= :pkImage WHERE name= :name")
    fun update(pkImage:String,name:String)
}