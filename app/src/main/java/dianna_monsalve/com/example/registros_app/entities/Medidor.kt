package dianna_monsalve.com.example.registros_app.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Medidor (
    @PrimaryKey(autoGenerate = true) val id:Int,
    val tipo:String,
    val valor:Int,
    val fecha:LocalDate
)

enum class TipoMedidor {
    LUZ,
    GAS,
    AGUA,
}
