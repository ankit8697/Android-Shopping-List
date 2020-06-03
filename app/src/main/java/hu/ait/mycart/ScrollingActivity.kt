package hu.ait.mycart

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import hu.ait.mycart.adapter.ItemAdapter
import hu.ait.mycart.data.AppDatabase
import hu.ait.mycart.data.Item
import hu.ait.mycart.touch.ItemRecyclerTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity(), ItemDialog.ItemHandler {

    lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)

        fabAdd.setOnClickListener {
            showAddItemDialog()
        }

        fabDelete.setOnClickListener {
            itemAdapter.deleteAllItems()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {

        Thread {
            var items = AppDatabase.getInstance(this@ScrollingActivity).todoDao().getAllItems()
            runOnUiThread {
                itemAdapter = ItemAdapter(this, items)
                recyclerItem.adapter = itemAdapter

                var itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
                recyclerItem.addItemDecoration(itemDecorator)

                val callback = ItemRecyclerTouchCallback(itemAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerItem)
            }
        }.start()
    }

    private fun saveItem(item: Item) {
        Thread {

            var newId = AppDatabase.getInstance(this@ScrollingActivity).todoDao().addItem(item)

            item.itemId = newId

            runOnUiThread {
                itemAdapter.addItem(item)
            }

        }.start()
    }

    private var editIndex : Int = -1

    private fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager, "TAG_ITEM_DIALOG")
    }

    fun showEditItemDialog(itemToEdit: Item, idx: Int) {
        editIndex = idx

        var editDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable("KEY_ITEM", itemToEdit)

        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }

    override fun itemCreated(item: Item) {
        saveItem(item)
    }

    override fun itemUpdated(item: Item) {
        Thread {
            AppDatabase.getInstance(this@ScrollingActivity).todoDao().updateItem(item)

            runOnUiThread {
                itemAdapter.updateItemAtPosition(item, editIndex)
            }
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menuTotal -> {
            var total = 0
            itemAdapter.itemList.forEach {
                if (it.status) {
                    total += it.estimated_price
                }
            }
            Snackbar.make(layout_main, "The total cost of items left is: $total", Snackbar.LENGTH_SHORT).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
