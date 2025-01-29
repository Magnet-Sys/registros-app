package dianna_monsalve.com.example.registros_app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dianna_monsalve.com.example.registros_app.dao.MedidorDao
import dianna_monsalve.com.example.registros_app.entities.Medidor


@Database(entities = [Medidor::class], version = 1)
@TypeConverters(LocalDate::class)
abstract class Database : RoomDatabase() {
    abstract fun MedidorDao(): MedidorDao
}