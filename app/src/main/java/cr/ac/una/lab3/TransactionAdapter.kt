package cr.ac.una.lab3

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class TransactionAdapter(context: Context, private val transactions: MutableList<Transaction>) :
    ArrayAdapter<Transaction>(context, 0, transactions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val transaction = transactions[position]

        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = transaction.toString()

        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("¿Desea eliminar el registro?")
                .setPositiveButton("Sí") { dialog, id ->
                    transactions.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            builder.create().show()
        }

        return view
    }
}