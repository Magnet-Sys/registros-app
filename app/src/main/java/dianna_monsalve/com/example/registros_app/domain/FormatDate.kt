package dianna_monsalve.com.example.registros_app.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

// Clase que formatea objetos LocalDate a una cadena con el formato "dd-MM-yyyy"
class FormatDate {
    // Crea un formateador de fecha personalizado
    val formatter = DateTimeFormatterBuilder()
        .appendValue(ChronoField.DAY_OF_MONTH) // Agrega el día del mes
        .appendLiteral("-")  // Agrega un guión como separador
        .appendValue(ChronoField.MONTH_OF_YEAR) // Agrega el mes del año
        .appendLiteral("-") // Agrega un guión como separador
        .appendValue(ChronoField.YEAR) // Agrega el año
        .toFormatter() // Crea el formateador
    // Sobrecarga del operador invoke para que la clase se pueda usar como una función
    operator fun invoke(fecha: LocalDate):String {
        // Formatea la fecha usando el formateador creado y devuelve la cadena resultante
        return fecha.format(formatter)
    }
}