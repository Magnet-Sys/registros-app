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
        var medidores by mutableStateOf(listOf<Medidor>())

        fun insertarMedidores(medidor: Medidor){
            viewModelScope.launch(Dispatchers.IO) {
                medidorDao.insertAll(medidor)
                obtenerMedidores()
            }
        }

        fun obtenerMedidores(): List<Medidor> {
            viewModelScope.launch(Dispatchers.IO) {
                medidores = medidorDao.getAll()
            }
            return medidores
        }

        companion object{
            val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val savedStateHandle = createSavedStateHandle()
                    val aplicacion = (this[APPLICATION_KEY] as Aplicacion)
                    ListaMedidor(aplicacion.medidorDao)
                }
            }
        }
}