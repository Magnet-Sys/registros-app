package dianna_monsalve.com.example.registros_app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
// Define la entidad Medidor para la base de datos Room
@Entity
data class Medidor (
    // Define la clave primaria autogenerada
    @PrimaryKey(autoGenerate = true) val id:Int,
    // Define el tipo de medidor (Luz, Gas, Agua)
    val tipo:String,
    // Define el valor de la medición
    val valor:Int,
    // Define la fecha de la medición
    val fecha:LocalDate
)
// Define un enumerado para los tipos de medidor
enum class TipoMedidor {
    // Medidor de Luz
    LUZ,
    // Medidor de Gas
    GAS,
    // Medidor de Agua
    AGUA,
}
