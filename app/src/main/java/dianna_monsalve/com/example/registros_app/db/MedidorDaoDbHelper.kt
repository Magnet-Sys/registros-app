@file:Suppress("UNREACHABLE_CODE")

package dianna_monsalve.com.example.registros_app.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import dianna_monsalve.com.example.registros_app.dao.MedidorDao
import dianna_monsalve.com.example.registros_app.entities.Medidor
import java.time.LocalDate

class MedidorDaoDbHelper(contexto: Context)
    : SQLiteOpenHelper(contexto, DB_NAME, null, DB_VERSION)
    , MedidorDao
{

    companion object {
        const val DB_NAME = "medidor.db"
        const val DB_VERSION = 1
        const val TABLE_NAME = "medidor"
        const val COL_ID            = "id"
        const val COL_FECHA         = "fecha"
        const val COL_TIPO          = "tipo"
        const val COL_VALOR         = "valor"
        const val DB_SQL_CREATE_TABLES = """
            CREATE TABLE IF NOT EXISTS ${TABLE_NAME}(
                ${COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${COL_TIPO} TEXT,
                ${COL_FECHA} INTEGER,
                ${COL_VALOR} INTEGER
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DB_SQL_CREATE_TABLES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<Medidor> {
        val cursor = this.readableDatabase.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val medidores = mutableListOf<Medidor>()
        with(cursor) {
            while(moveToNext()) {
                val id = getInt( getColumnIndexOrThrow(COL_ID) )
                val valor = getInt( getColumnIndexOrThrow(COL_VALOR) )
                val fechaNum = getLong( getColumnIndexOrThrow(COL_FECHA) )
                val fecha = LocalDate().fromTimestamp(fechaNum) ?: LocalDate.now()
                val tipo = getString( getColumnIndexOrThrow(COL_TIPO))
                val medidor = Medidor(id, tipo, valor, fecha)
                medidores.add(medidor)
            }
        }
        return medidores
    }

    override suspend fun findById(id: Int): Medidor? {
        TODO("Not yet implemented")
    }

    override suspend fun count(): Int {
        TODO("Not yet implemented")
    }

    suspend fun insert(medidor:Medidor) {
        Log.v("MedidorDaoDbHelper", "::insert()")
        val valores = ContentValues().apply {
            put(COL_TIPO, medidor.tipo)
            put(COL_FECHA, LocalDate().dateToTimestamp(medidor.fecha))
            put(COL_VALOR, medidor.valor)
        }
        this.writableDatabase.insert(
            TABLE_NAME,
            null,
            valores
        )
    }

    override suspend fun update(medidor: Medidor) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(medidor: Medidor) {
        val sb = StringBuilder()
        sb.append(COL_ID).append(" = ?")
        val c = sb.toString()
        this.writableDatabase.delete(TABLE_NAME, c ,arrayOf(medidor.id.toString()))
    }

    override suspend fun insertAll(vararg medidores: Medidor) {
        medidores.forEach {
            insert(it)
        }
    }
}