package dianna_monsalve.com.example.registros_app.entities

import android.content.Context
import dianna_monsalve.com.example.registros_app.db.MedidorDaoDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Clase para gestionar los registros de medidores, actuando como una capa entre la UI y la base de datos
class Registros private constructor(contexto: Context) {
    // Instancia de la clase auxiliar para la base de datos de medidores
    private val db: MedidorDaoDbHelper = MedidorDaoDbHelper(contexto)

    // Función suspendida para obtener todos los medidores
    // withContext(Dispatchers.IO) cambia la ejecución a un hilo de E/S para no bloquear el hilo principal
    suspend fun obtenerTodos(): List<Medidor> = withContext(Dispatchers.IO) {
        // Llama a la función getAll() del DAO para obtener todos los medidores
        db.getAll()
    }

    // Función suspendida para agregar un nuevo medidor
    // withContext(Dispatchers.IO) cambia la ejecución a un hilo de E/S
    suspend fun agregar(medidor: Medidor) = withContext(Dispatchers.IO) {
        // Llama a la función insert() del DAO para agregar el medidor a la base de datos
        db.insert(medidor)
    }

    // Función suspendida para eliminar un medidor existente
    // withContext(Dispatchers.IO) cambia la ejecución a un hilo de E/S
    suspend fun eliminar(medidor: Medidor) = withContext(Dispatchers.IO) {
        // Llama a la función delete() del DAO para eliminar el medidor de la base de datos
        db.delete(medidor)
    }

    // Objeto complementario para implementar el patrón Singleton
    companion object {
        // Instancia única de la clase Registros
        private var instancia: Registros? = null

        // Metodo para obtener la instancia única de la clase Registros (Singleton)
        fun getInstance(contexto: Context): Registros {
            // Si la instancia es nula, crea una nueva
            if( instancia == null )
                instancia = Registros(contexto)
            // Devuelve la instancia existente o la recién creada
            return instancia!!
        }
    }
}