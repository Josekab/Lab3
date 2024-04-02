package cr.ac.una.lab3

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    private var isNewTransaction = true
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_movimiento)

        val datePicker = findViewById<DatePicker>(R.id.selectorFecha)
        val typeSpinner = findViewById<Spinner>(R.id.SelectorTIpoTarjeta)
        val items = resources.getStringArray(R.array.opciones_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        typeSpinner.adapter = adapter
        val amountEditText = findViewById<EditText>(R.id.EspacioMonto)

        isNewTransaction = intent.getBooleanExtra("isNewTransaction", true)
        if (!isNewTransaction) {
            transaction = intent.getSerializableExtra("transaction") as Transaction
            val dateParts = transaction.Dia.split("/")
            datePicker.updateDate(dateParts[2].toInt(), dateParts[1].toInt() - 1, dateParts[0].toInt())
            val spinnerPosition = (typeSpinner.adapter as ArrayAdapter<String>).getPosition(transaction.Tipo_Tarjeta)
            typeSpinner.setSelection(spinnerPosition)
            amountEditText.setText(transaction.Monto.toString())
        }

        val insertButton = findViewById<Button>(R.id.insertar)
        insertButton.setOnClickListener {
            val date = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
            val type = typeSpinner.selectedItem.toString()
            val amountText = amountEditText.text.toString()

            // Verifica si el monto ingresado tiene el formato correcto
            if (!amountText.matches(Regex("^\\d+(\\.\\d{1,2})?$"))) {
                // Muestra un mensaje de error
                return@setOnClickListener
            }

            val amount = amountText.toDouble()

            if (isNewTransaction) {
                transaction = Transaction(date, type, amount)
                val returnIntent = Intent().apply {
                    putExtra("transaction", transaction)
                    putExtra("isNewTransaction", isNewTransaction)
                }
                setResult(RESULT_OK, returnIntent)
                finish()
            } else {
                transaction.Dia = date
                transaction.Tipo_Tarjeta = type
                transaction.Monto = amount

                val builder = AlertDialog.Builder(this)
                builder.setMessage("¿Desea modificar el registro?")
                    .setPositiveButton("Sí") { dialog, id ->
                        val returnIntent = Intent().apply {
                            putExtra("transaction", transaction)
                            putExtra("isNewTransaction", isNewTransaction)
                            putExtra("position", intent.getIntExtra("position", -1))
                        }
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                builder.create().show()
            }
        }

        val exitButton = findViewById<Button>(R.id.Salir)
        exitButton.setOnClickListener {
            finish()
        }
    }
}