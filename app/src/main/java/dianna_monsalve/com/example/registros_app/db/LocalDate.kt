package dianna_monsalve.com.example.registros_app.db

import androidx.room.TypeConverter
import java.time.LocalDate
// Clase para convertir objetos LocalDate a un formato que Room pueda almacenar en la base de datos y viceversa.
class LocalDate {
    // Convierte un Long (timestamp en días desde la época) a un objeto LocalDate.
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        // Si el valor es nulo, devuelve nulo. De lo contrario, crea un LocalDate a partir del número de días desde la fecha.
        return value?.let { LocalDate.ofEpochDay(it) }
    }
    // Convierte un objeto LocalDate a un Long (timestamp en días desde la época).
    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        // Si la fecha es nula, devuelve nulo.
        return date?.toEpochDay()
    }
}