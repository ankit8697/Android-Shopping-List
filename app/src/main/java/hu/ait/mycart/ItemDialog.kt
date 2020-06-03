package hu.ait.mycart

import android.app.Dialog
import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.ait.mycart.data.Item
import kotlinx.android.synthetic.main.new_item_dialog.*
import kotlinx.android.synthetic.main.new_item_dialog.view.*

class ItemDialog : DialogFragment() {
    interface ItemHandler {
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException("The activity does not implement the ItemHandlerInterface")
        }
    }

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var spCategory: Spinner
    private lateinit var etPrice: EditText
    private lateinit var cbStatus: CheckBox


    var isEditMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New Item")

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_item_dialog, null
        )
        etName = rootView.etName
        etDescription = rootView.etDescription
        spCategory = rootView.spCategory
        etPrice = rootView.etPrice
        cbStatus = rootView.cbStatus



        builder.setView(rootView)

        isEditMode = (arguments != null) && (arguments!!.containsKey("KEY_ITEM"))

        if (isEditMode) {
            builder.setTitle("Edit Item")
            var item : Item = (arguments?.getSerializable("KEY_ITEM") as Item)

            etName.setText(item.name)
            etDescription.setText(item.description)
            etPrice.setText(item.estimated_price.toString())
            cbStatus.isChecked = item.status
            var selection = 0
            when(item.catagory) {
                "Food" -> selection = 0
                "Clothing" -> selection = 1
                "Entertainment" -> selection = 2
            }
            spCategory.setSelection(selection)
        }

        builder.setPositiveButton("OK") {
                dialog, witch -> // empty
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etName.text.isNotEmpty() && etDescription.text.isNotEmpty() && etPrice.text.isNotEmpty()) {
                if (isEditMode) {
                    handleTodoEdit()
                } else {
                    handleTodoCreate()
                }
                (dialog as AlertDialog).dismiss()
            } else {
                if (etName.text.isEmpty()) {
                    etName.error = "This field can not be empty"
                }
                if (etDescription.text.isEmpty()) {
                    etDescription.error = "The description cannot be empty"
                }
                if (etPrice.text.isEmpty()){
                    etPrice.error = "The price cannot be empty"
                }
            }
        }
    }

    private fun handleTodoEdit() {
        val itemToEdit = arguments?.getSerializable(
            "KEY_ITEM"
        ) as Item
        itemToEdit.name = etName.text.toString()
        itemToEdit.catagory = spCategory.selectedItem.toString()
        itemToEdit.description = etDescription.text.toString()
        itemToEdit.estimated_price = etPrice.text.toString().toInt()
        itemToEdit.status = cbStatus.isChecked

        itemHandler.itemUpdated(itemToEdit)
    }

    private fun handleTodoCreate() {
        itemHandler.itemCreated(
            Item(
                null,
                etName.text.toString(),
                etDescription.text.toString(),
                spCategory.selectedItem.toString(),
                etPrice.text.toString().toInt(),
                cbStatus.isChecked
            )
        )
    }
}
