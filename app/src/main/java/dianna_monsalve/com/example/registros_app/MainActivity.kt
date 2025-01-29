package dianna_monsalve.com.example.registros_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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

class MainActivity : ComponentActivity() {
    private lateinit var registros:Registros
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registros = Registros.getInstance(this)

        setContent {
            Apptareas()

        }
    }
    companion object {
        var medidorData: Medidor = Medidor(1,"",1, LocalDate.now())
    }
}

@Composable
fun AppMedidores(
    PageFormularioUI: () -> Unit,
    EliminarFormularioUI: () -> Unit,
    borrado: Boolean = false,
    creado: Boolean = false,
) {
    val contexto = LocalContext.current
    var medidores by remember {
        mutableStateOf( emptyList<Medidor>() )
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            if(borrado){
                Registros.getInstance(contexto).eliminar(MainActivity.medidorData)
            }
            if(creado){
                Registros.getInstance(contexto).agregar(MainActivity.medidorData)
            }
            medidores = Registros.getInstance(contexto).obtenerTodos()
        }
    }

    Scaffold(
        floatingActionButton = {
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
            ListaMedidoresUI(medidores,EliminarFormularioUI)
        }
    }
}

@Composable
fun ListaMedidoresUI(
    medidores:List<Medidor>,
    EliminarFormularioUI: () -> Unit,
) {
    LazyColumn() {
        items(medidores){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = FormatDate()(it.fecha),
                        style = TextStyle(
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconoMedidor(it)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column() {
                        Text(it.tipo)
                        Text(
                            text = (it.valor.toString()),
                            style = TextStyle(
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Row() {
                    IconButton(onClick = {
                        MainActivity.medidorData = it
                        EliminarFormularioUI()
                    }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
            }
            Divider()
        }
    }
}

@Composable
fun Apptareas(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "inicio"){
        composable("inicio"){
            AppMedidores(
                {navController.navigate("formulario")},
                {navController.navigate("eliminar")},
            )
        }
        composable("formulario"){
            PageFormularioUI(
                {navController.navigate("inicio")},
                {navController.navigate("crear")},
                )
        }
        composable("eliminar"){
            AppMedidores(
                {navController.navigate("formulario")},
                {navController.navigate("eliminar")},
                true,
            )
        }
        composable("crear"){
            AppMedidores(
                {navController.navigate("formulario")},
                {navController.navigate("eliminar")},
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
){
    var valor by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    val context = LocalContext.current
    val types = arrayOf("Luz", "Gas", "Agua")
    var selectedText by remember { mutableStateOf(types[0]) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {VolverUI()}) {
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
                modifier = Modifier.padding(bottom = 8.dp).align(alignment = Alignment.CenterHorizontally)
            )

            // Campo de valor
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
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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
                    MainActivity.medidorData = Medidor(0, selectedText, valor.toInt(), LocalDate.now())
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
    when (TipoMedidor.valueOf(medidor.tipo.toUpperCase())) {
        TipoMedidor.LUZ -> Image(
            painter = painterResource(id = R.drawable.luz),
            contentDescription = TipoMedidor.LUZ.toString(),
            modifier = Modifier.size(32.dp)
        )
        TipoMedidor.GAS -> Image(
            painter = painterResource(id = R.drawable.gas),
            contentDescription = TipoMedidor.GAS.toString(),
            modifier = Modifier.size(32.dp)
        )
        TipoMedidor.AGUA -> Image(
            painter = painterResource(id = R.drawable.agua),
            contentDescription = TipoMedidor.AGUA.toString(),
            modifier = Modifier.size(32.dp)
        )
    }
}



