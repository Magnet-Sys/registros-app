package dianna_monsalve.com.example.registros_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dianna_monsalve.com.example.registros_app.domain.FormatDate
import dianna_monsalve.com.example.registros_app.entities.Medidor
import dianna_monsalve.com.example.registros_app.entities.Registros
import dianna_monsalve.com.example.registros_app.entities.TipoMedidor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    // Variable para almacenar los registros
    private lateinit var registros: Registros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa la instancia de Registros
        registros = Registros.getInstance(this)

        setContent {
            // Llama a la función principal de la aplicación
            Apptareas()

        }
    }

    // Companion object para almacenar datos del medidor
    companion object {
        // Valor por defecto para evitar problemas al iniciar
        var medidorData: Medidor = Medidor(1, "", 1, LocalDate.now())
    }
}

@Composable
fun AppMedidores(
    PageFormularioUI: () -> Unit,
    EliminarFormularioUI: () -> Unit,
    borrado: Boolean = false,
    creado: Boolean = false,
) {
    // Contexto local
    val contexto = LocalContext.current
    // Estado para la lista de medidores
    var medidores by remember {
        mutableStateOf(emptyList<Medidor>())
    }
    // Usar LaunchedEffect para ejecutar la corrutina cuando la composición se lance
    LaunchedEffect(borrado, creado) {
        // Usar viewModelScope para lanzar la corrutina en un ámbito de ViewModel
        // Si no estás en un ViewModel, usa CoroutineScope(Dispatchers.IO) en su lugar
        withContext(Dispatchers.IO) {
            // Si se borra un medidor, se elimina de la base de datos
            if (borrado) {
                Registros.getInstance(contexto).eliminar(MainActivity.medidorData)
            }
            // Si se crea un medidor, se agrega a la base de datos
            if (creado) {
                Registros.getInstance(contexto).agregar(MainActivity.medidorData)
            }
            // Obtiene todos los medidores de la base de datos
            medidores = Registros.getInstance(contexto).obtenerTodos()
        }
    }
    // Scaffold para la estructura básica de la UI
    Scaffold(
        floatingActionButton = {
            // Botón flotante para agregar medidores
            FloatingActionButton(onClick = {
                PageFormularioUI()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = innerPadding.calculateLeftPadding(LayoutDirection.Ltr))
        ) {
            // Muestra la lista de medidores
            ListaMedidoresUI(medidores, EliminarFormularioUI)
        }
    }
}

@Composable
fun ListaMedidoresUI(
    medidores: List<Medidor>,
    EliminarFormularioUI: () -> Unit,
) {
    // LazyColumn para mostrar la lista de medidores
    LazyColumn(Modifier.padding(8.dp)) {
        items(medidores) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Contenedor para el icono, tipo y valor del medidor
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Muestra el icono del medidor
                    IconoMedidor(it)
                    Spacer(modifier = Modifier.width(8.dp))

                    // Contenedor para el tipo y valor del medidor
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Muestra el tipo de medidor
                        Text(
                            text = it.tipo,
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre tipo y valor

                        // Muestra el valor del medidor
                        Text(
                            text = it.valor.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Contenedor para la fecha y el botón de eliminar, alineados a la derecha
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Muestra la fecha del medidor
                    Text(
                        text = FormatDate()(it.fecha),
                        style = TextStyle(fontSize = 14.sp),
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    // Botón para eliminar el medidor
                    IconButton(onClick = {
                        MainActivity.medidorData = it
                        EliminarFormularioUI()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            // Divisor entre medidores
            Divider()
        }
    }
}

@Composable
fun Apptareas() {
    // Controlador de navegación
    val navController = rememberNavController()
    // NavHost para la navegación entre pantallas
    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        // Pantalla de inicio
        composable("inicio") {
            AppMedidores(
                { navController.navigate("formulario") },
                { navController.navigate("eliminar") },
            )
        }
        // Pantalla de formulario
        composable("formulario") {
            PageFormularioUI(
                { navController.navigate("inicio") },
                { navController.navigate("crear") },
            )
        }
        // Pantalla de eliminación
        composable("eliminar") {
            AppMedidores(
                { navController.navigate("formulario") },
                { navController.navigate("eliminar") },
                true,
            )
        }
        // Pantalla de creación
        composable("crear") {
            AppMedidores(
                { navController.navigate("formulario") },
                { navController.navigate("eliminar") },
                creado = true,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageFormularioUI(
    VolverUI: () -> Unit,
    CrearUI: () -> Unit,
) {
    // Estado para el valor del medidor
    var valor by remember { mutableStateOf("") }
    // Estado para la fecha del medidor
    var fecha by remember { mutableStateOf("") }
    // Contexto local
    val context = LocalContext.current
    // Tipos de medidores
    val types = arrayOf("Luz", "Gas", "Agua")
    // Estado para el tipo de medidor seleccionado
    var selectedText by remember { mutableStateOf(types[0]) }
    // Scaffold para la estructura básica de la UI
    Scaffold(
        floatingActionButton = {
            // Botón flotante para volver
            FloatingActionButton(onClick = { VolverUI() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
        })
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = innerPadding.calculateLeftPadding(LayoutDirection.Ltr))
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Título y subtítulo
            Text(
                text = "Registro Medidor",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp).align(alignment = Alignment.CenterHorizontally)
            )

            // Campo de valor (regitro medidor)
            TextField(
                value = valor,
                onValueChange = { value -> valor = value.filter { it.isDigit() } },
                label = { Text("Medidor") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de fecha
            TextField(
                value = fecha,
                onValueChange = { value ->
                    fecha = value // Actualizar el estado de la fecha
                },
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text), // Cambiado a KeyboardType.Text
                placeholder = { Text("Ingrese la fecha en formato YYYY-MM-DD") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selección de tipo de medidor con RadioButtons
            Text(
                text = "Medidor de:",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column {
                types.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        // RadioButton para cada tipo de medidor
                        RadioButton(
                            selected = selectedText == item,
                            onClick = {
                                selectedText = item
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = item)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registro
            Button(
                onClick = {
                    // Aquí se crea el objeto Medidor con la fecha ingresada
                    val fechaSeleccionada = try {
                        LocalDate.parse(fecha, DateTimeFormatter.ISO_DATE)
                    } catch (e: Exception) {
                        Log.e("PageFormularioUI", "Error al parsear la fecha: ${e.message}")
                        LocalDate.now() // Fecha actual como fallback
                    }

                    MainActivity.medidorData = Medidor(0, selectedText, valor.toInt(), fechaSeleccionada)
                    CrearUI()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar medición")
            }
        }
    }
}

@Composable
fun IconoMedidor(medidor: Medidor) {
    // Muestra un icono diferente según el tipo de medidor
    when (TipoMedidor.valueOf(medidor.tipo.toUpperCase())) {
        TipoMedidor.LUZ -> Image(
            painter = painterResource(id = R.drawable.luz),
            contentDescription = TipoMedidor.LUZ.toString(),
            modifier = Modifier.size(24.dp)
        )
        TipoMedidor.GAS -> Image(
            painter = painterResource(id = R.drawable.gas),
            contentDescription = TipoMedidor.GAS.toString(),
            modifier = Modifier.size(24.dp)
        )
        TipoMedidor.AGUA -> Image(
            painter = painterResource(id = R.drawable.agua),
            contentDescription = TipoMedidor.AGUA.toString(),
            modifier = Modifier.size(24.dp)
        )
    }
}