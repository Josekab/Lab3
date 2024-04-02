package cr.ac.una.lab3

import java.io.Serializable

data class Transaction(
    var Dia: String,
    var Tipo_Tarjeta: String,
    var Monto: Double
) : Serializable