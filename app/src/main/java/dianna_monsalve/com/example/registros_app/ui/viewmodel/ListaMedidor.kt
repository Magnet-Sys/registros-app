package dianna_monsalve.com.example.registros_app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dianna_monsalve.com.example.registros_app.Aplicacion
import dianna_monsalve.com.example.registros_app.dao.MedidorDao
import dianna_monsalve.com.example.registros_app.entities.Medidor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaMedidor ( private val medidorDao: MedidorDao) : ViewModel() {
    // MutableState para almacenar la lista de medidores
    var medidores by mutableStateOf(listOf<Medidor>())
        // Función para insertar medidores en la base de datos
        fun insertarMedidores(medidor: Medidor){
            // Se lanza una corrutina en el dispatcher de IO para operaciones de base de datos
            viewModelScope.launch(Dispatchers.IO) {
                // Insertar el medidor en la base de datos
                medidorDao.insertAll(medidor)
                // Actualizar la lista de medidores
                obtenerMedidores()
            }
        }
        // Función para obtener la lista de medidores de la base de datos
        fun obtenerMedidores(): List<Medidor> {
            viewModelScope.launch(Dispatchers.IO) {
                // Obtener todos los medidores de la base de datos
                medidores = medidorDao.getAll()
            }
            // Devolver la lista de medidores
            return medidores
        }
        // Companion object para el factory del ViewModel
        companion object{
            // Factory para crear instancias de ListaMedidor
            val Factory: ViewModelProvider.Factory = viewModelFactory {
                // Inicializador para crear la instancia de ListaMedidor
                initializer {
                    // Obtener el SavedStateHandle
                    val savedStateHandle = createSavedStateHandle()
                    val aplicacion = (this[APPLICATION_KEY] as Aplicacion)
                    // Crear y devolver una instancia de ListaMedidor
                    ListaMedidor(aplicacion.medidorDao)
                }
            }
        }
}