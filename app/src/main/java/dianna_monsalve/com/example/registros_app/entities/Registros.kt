package dianna_monsalve.com.example.registros_app.entities

import android.content.Context
import androidx.room.Room
import dianna_monsalve.com.example.registros_app.dao.MedidorDao
import dianna_monsalve.com.example.registros_app.db.Database
import dianna_monsalve.com.example.registros_app.db.MedidorDaoDbHelper

class Registros(
    private val medidorDao: MedidorDao
) {
    suspend fun obtenerTodos():List<Medidor> = medidorDao.getAll()

    suspend fun agregar(medidor:Medidor) = medidorDao.insertAll(medidor)

    suspend fun eliminar(medidor:Medidor) = medidorDao.delete(medidor)

    suspend fun contarRegistros():Int = medidorDao.count()

    companion object {
        @Volatile
        private var instance: Registros? = null

        fun getInstance(contexto:Context):Registros {
            return getInstanceDSDbHelper(contexto)
        }

        fun getInstanceDSDbHelper(contexto:Context):Registros {
            return instance ?: synchronized(this) {
                Registros(
                    MedidorDaoDbHelper(contexto)
                )
            }
        }

        fun getInstanceDSRoom(contexto: Context):Registros {
            return instance ?: synchronized(this) {
                Registros(
                    Room.databaseBuilder(
                        contexto.applicationContext,
                        Database::class.java,
                        "medidor.db"
                    ).build().MedidorDao()
                ).also {
                    instance = it
                }
            }
        }
    }
}