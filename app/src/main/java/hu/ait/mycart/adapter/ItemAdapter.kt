package hu.ait.mycart.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_row.view.*
import androidx.recyclerview.widget.RecyclerView
import hu.ait.mycart.R
import hu.ait.mycart.ScrollingActivity
import hu.ait.mycart.data.AppDatabase
import hu.ait.mycart.data.Item
import hu.ait.mycart.touch.ItemTouchHelperCallback
import java.util.*


class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>, ItemTouchHelperCallback {

    var itemList = mutableListOf<Item>()
    var context: Context

    constructor(context: Context, items: List<Item>) {
        this.context = context
        itemList.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemRow = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false)
        return ViewHolder(itemRow)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = itemList[holder.adapterPosition]

        holder.cbStatus.isChecked = item.status
        holder.tvName.text = item.name
        if (item.status) {
            holder.tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.tvName.paintFlags = 0
        }
        holder.cbStatus.setOnClickListener {
            if (item.status) {
                holder.tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.tvName.paintFlags = 0
            }
            item.status = !item.status
            updateItemAtPosition(item, position)
        }

        when(item.catagory) {
            "Food" -> holder.ivIcon.setImageResource(R.drawable.food_icon)
            "Clothing" -> holder.ivIcon.setImageResource(R.drawable.icon_clothing)
            "Entertainment" -> holder.ivIcon.setImageResource(R.drawable.video_game_icon)
        }

        holder.view.setOnClickListener {
            (context as ScrollingActivity).showEditItemDialog(item, position)
        }
    }

    fun updateItem(item: Item) {
        Thread {
            AppDatabase.getInstance(context).todoDao().updateItem(item)
        }.start()
    }

    fun updateItemAtPosition(item: Item, index: Int) {
        itemList[index] = item
        updateItem(item)
        notifyItemChanged(index)
    }

    fun deleteItem(index: Int) {

        Thread {
            AppDatabase.getInstance(context).todoDao().deleteItem(itemList[index])
            (context as ScrollingActivity).runOnUiThread {
                itemList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun deleteAllItems() {

        Thread {
            AppDatabase.getInstance(context).todoDao().deleteAllItems()

            (context as ScrollingActivity).runOnUiThread {
                itemList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addItem(item: Item) {
        itemList.add(item)
        notifyItemInserted(itemList.lastIndex)
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(itemList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivIcon = itemView.ivIcon
        var tvName = itemView.tvName
        var cbStatus = itemView.cbStatus
        var view = itemView
    }
}