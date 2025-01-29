package dianna_monsalve.com.example.registros_app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dianna_monsalve.com.example.registros_app.dao.MedidorDao
import dianna_monsalve.com.example.registros_app.entities.Medidor

// Define la clase de la base de datos usando la anotación @Database
@Database(entities = [Medidor::class], version = 1)
// Especifica los convertidores de tipos personalizados, en este caso, para LocalDate
@TypeConverters(LocalDate::class)
// Clase abstracta que extiende de RoomDatabase
abstract class Database : RoomDatabase() {
    // Metodo abstracto para obtener el DAO (Data Access Object) de Medidor
    abstract fun MedidorDao(): MedidorDao
}