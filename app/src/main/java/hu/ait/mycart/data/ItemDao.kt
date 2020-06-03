package hu.ait.mycart.data

import androidx.room.*

@Dao
interface ItemDao {

    @Query("SELECT * FROM items")
    fun getAllItems() : List<Item>

    @Insert
    fun addItem(item: Item) : Long

    @Delete
    fun deleteItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Query("DELETE FROM items")
    fun deleteAllItems()
}