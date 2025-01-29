package dianna_monsalve.com.example.registros_app

import android.app.Application
import androidx.room.Room
import dianna_monsalve.com.example.registros_app.db.Database

class Aplicacion : Application() {
    val db by lazy { Room.databaseBuilder(
        this, Database::class.java,"Database"
    ).build()}
    val medidorDao by lazy { db.MedidorDao()}
}

