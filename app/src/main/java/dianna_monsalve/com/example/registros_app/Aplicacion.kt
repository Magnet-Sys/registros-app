package dianna_monsalve.com.example.registros_app

import android.app.Application
import androidx.room.Room
import dianna_monsalve.com.example.registros_app.db.Database
// Clase que representa la aplicación en sí, extendiendo de Application
class Aplicacion : Application() {
    // Inicialización lazy de la base de datos Room
    val db by lazy { Room.databaseBuilder(
        this, // Contexto de la aplicación
        Database::class.java, // Clase que define la base de datos
        "Database" // Nombre del archivo de la base de datos
    ).build()} // Construye la instancia de la base de datos
    // Inicialización perezosa del DAO de Medidor
    val medidorDao by lazy { db.MedidorDao()}
}

