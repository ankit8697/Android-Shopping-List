package hu.ait.mycart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "catagory") var catagory: String,
    @ColumnInfo(name = "estimated_price") var estimated_price: Int,
    @ColumnInfo(name = "status") var status: Boolean
) : Serializable
