package dianna_monsalve.com.example.registros_app.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import dianna_monsalve.com.example.registros_app.dao.MedidorDao
import dianna_monsalve.com.example.registros_app.entities.Medidor
import java.time.LocalDate

// Clase que ayuda a interactuar con la base de datos SQLite, implementando la interfaz MedidorDao
class MedidorDaoDbHelper(contexto: Context)
    : SQLiteOpenHelper(contexto, DB_NAME, null, DB_VERSION)
    , MedidorDao
{
    // Objeto estático que contiene constantes relacionadas con la base de datos
    companion object {
        // Nombre del archivo de la base de datos
        const val DB_NAME = "medidor.db"
        // Versión de la base de datos
        const val DB_VERSION = 1
        // Nombre de la tabla de medidores
        const val TABLE_NAME = "medidor"
        // Constantes para los nombres de las columnas de la tabla
        const val COL_ID            = "id"
        const val COL_FECHA         = "fecha"
        const val COL_TIPO          = "tipo"
        const val COL_VALOR         = "valor"
        // Sentencia SQL para crear la tabla de medidores
        const val DB_SQL_CREATE_TABLES = """
            CREATE TABLE IF NOT EXISTS ${TABLE_NAME}(
                ${COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${COL_TIPO} TEXT,
                ${COL_FECHA} INTEGER,
                ${COL_VALOR} INTEGER
            );
        """
    }
    // Metodo llamado cuando se crea la base de datos por primera vez
    override fun onCreate(db: SQLiteDatabase?) {
        // Ejecuta la sentencia SQL para crear la tabla de medidores
        db?.execSQL(DB_SQL_CREATE_TABLES)
    }
    // Metodo llamado cuando se actualiza la versión de la base de datos
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // Implementar la lógica para actualizar la base de datos en futuras versiones
        TODO("Not yet implemented")
    }
    // Metodo para obtener todos los medidores de la base de datos
    override suspend fun getAll(): List<Medidor> {
        // Realiza una consulta a la base de datos para obtener todos los registros de la tabla medidor
        val cursor = this.readableDatabase.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        // Lista mutable para almacenar los medidores obtenidos
        val medidores = mutableListOf<Medidor>()
        // Itera sobre el cursor para extraer los datos de cada medidor
        with(cursor) {
            while(moveToNext()) {
                // Obtiene el ID del medidor
                val id = getInt( getColumnIndexOrThrow(COL_ID) )
                // Obtiene el valor del medidor
                val valor = getInt( getColumnIndexOrThrow(COL_VALOR) )
                // Obtiene la fecha del medidor como un Long (timestamp)
                val fechaNum = getLong( getColumnIndexOrThrow(COL_FECHA) )
                // Convierte el timestamp a un objeto LocalDate, usando LocalDate.now() como valor predeterminado si es nulo
                val fecha = LocalDate().fromTimestamp(fechaNum) ?: LocalDate.now()
                // Obtiene el tipo de medidor
                val tipo = getString( getColumnIndexOrThrow(COL_TIPO))
                // Crea un objeto Medidor con los datos obtenidos
                val medidor = Medidor(id, tipo, valor, fecha)
                // Agrega el medidor a la lista
                medidores.add(medidor)
            }
        }
        // Cierra el cursor
        cursor.close()
        // Devuelve la lista de medidores
        return medidores
    }
    // Metodo para buscar un medidor por su ID
    override suspend fun findById(id: Int): Medidor? {
        // Implementar la búsqueda de un medidor por su ID
        TODO("Not yet implemented")
    }
    // Metodo para obtener la cantidad de medidores en la base de datos
    override suspend fun count(): Int {
        // Implementar la consulta para obtener la cantidad de medidores
        TODO("Not yet implemented")
    }
    // Metodo para insertar un nuevo medidor en la base de datos
    suspend fun insert(medidor:Medidor) {
        Log.v("MedidorDaoDbHelper", "::insert()")
        // Crea un objeto ContentValues para almacenar los valores a insertar
        val valores = ContentValues().apply {
            // Agrega el tipo de medidor al ContentValues
            put(COL_TIPO, medidor.tipo)
            // Convierte la fecha a timestamp y la agrega al ContentValues
            put(COL_FECHA, LocalDate().dateToTimestamp(medidor.fecha))
            // Agrega el valor del medidor al ContentValues
            put(COL_VALOR, medidor.valor)
        }
        // Inserta el nuevo registro en la base de datos
        this.writableDatabase.insert(
            TABLE_NAME,
            null, // No se especifica una columna nullColumnHack
            valores
        )
    }
    // Metodo para actualizar un medidor existente en la base de datos
    override suspend fun update(medidor: Medidor) {
        // Implementar la actualización de un medidor
        TODO("Not yet implemented")
    }
    // Metodo para eliminar un medidor de la base de datos
    override suspend fun delete(medidor: Medidor) {
        // Crea una cadena con la condición WHERE para la eliminación
        val sb = StringBuilder()
        sb.append(COL_ID).append(" = ?")
        val c = sb.toString()
        // Ejecuta la sentencia DELETE en la base de datos
        this.writableDatabase.delete(TABLE_NAME, c ,arrayOf(medidor.id.toString()))
    }
    // Metodo para insertar varios medidores en la base de datos
    override suspend fun insertAll(vararg medidores: Medidor) {
        // Itera sobre la lista de medidores y los inserta uno por uno
        medidores.forEach {
            insert(it)
        }
    }
}