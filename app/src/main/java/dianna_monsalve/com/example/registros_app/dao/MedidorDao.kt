package dianna_monsalve.com.example.registros_app.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dianna_monsalve.com.example.registros_app.entities.Medidor
// Interfaz que define las operaciones de acceso a datos para la entidad Medidor
@Dao
interface MedidorDao {
    // Obtiene todos los registros de Medidor ordenados por ID
    @Query("SELECT * FROM medidor ORDER BY id")
    suspend fun getAll(): List<Medidor>
    // Busca un registro de Medidor por su ID
    @Query("SELECT * FROM medidor WHERE id = :id")
    suspend fun findById(id:Int): Medidor?
    // Cuenta la cantidad de registros en la tabla Medidor
    @Query("SELECT COUNT(*) FROM medidor")
    suspend fun count():Int
    // Actualiza un registro de Medidor
    @Update
    suspend fun update(medidorDao:Medidor)
    // Elimina un registro de Medidor
    @Delete
    suspend fun delete(medidorDao: Medidor)
    // Inserta uno o más registros de Medidor
    @Insert
    suspend fun insertAll(vararg medidores: Medidor)
}