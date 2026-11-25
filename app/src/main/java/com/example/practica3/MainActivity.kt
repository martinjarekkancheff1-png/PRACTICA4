package com.example.practica3

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
/
data class Registro(
    val id: Int,
    val campo1: String,
    val campo2: String,
    val campo3: String,
    val campo4: String = "",
    val campo5: String = ""
)

data class ClienteApi(
    val id_clientes: Int,
    val nombre: String,
    val apellido: String,
    val edad: String,
    val telefono: String
)

data class EntrenadorApi(
    val id_entrenadores: Int,
    val nombre: String,
    val apellido: String,
    val especialidad: String,
    val telefono: String
)

data class MembresiaApi(
    val id_membresias: Int,
    val tipo: String,
    val precio: String,
    val duracion_dias: String,
    val id_clientes: String,
    val id_rutinas: String
)

data class RutinaApi(
    val id_rutinas: Int,
    val nombre: String,
    val objetivo: String,
    val dias_semana: String,
    val id_entrenadores: String
)

data class PagoApi(
    val id_pagos: Int,
    val fecha_pago: String,
    val monto: String,
    val metodo_pago: String,
    val id_clientes: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppGym()
            }
        }
    }
}

class Preferencias(context: Context) {
    private val preferences =
        context.getSharedPreferences("painzone", Context.MODE_PRIVATE)

    fun guardar(prop: String, value: String) {
        preferences.edit().putString(prop, value).apply()
    }

    fun obtener(prop: String, def: String): String? {
        return preferences.getString(prop, def)
    }

    fun remover(prop: String) {
        preferences.edit().remove(prop).apply()
    }
}

// API SERVICE

interface ApiService {

    @POST("iniciarSesion.php")
    @FormUrlEncoded
    suspend fun iniciarSesion(
        @Field("nombreUsuario") nombreUsuario: String,
        @Field("contrasena") contrasena: String
    ): Response<String>

// CLIENTES

    @GET("clientes.php?buscar&busqueda")
    suspend fun buscarClientes(
    ): List<ClienteApi>

    @POST("clientes.php?registrar")
    @FormUrlEncoded
    suspend fun registrarCliente(
        @Field("nombre") nombre: String,
        @Field("apellido") apellido: String,
        @Field("edad") edad: String,
        @Field("telefono") telefono: String
    ): Response<String>

    @POST("clientes.php?modificar")
    @FormUrlEncoded
    suspend fun modificarCliente(
        @Field("id_clientes") id: Int,
        @Field("nombre") nombre: String,
        @Field("apellido") apellido: String,
        @Field("edad") edad: String,
        @Field("telefono") telefono: String
    ): Response<String>

    @POST("clientes.php?eliminar")
    @FormUrlEncoded
    suspend fun eliminarCliente(
        @Field("id_clientes") id: Int
    ): Response<String>

// ENTRENADORES

    @GET("entrenadores.php?buscar")
    suspend fun buscarEntrenadores(
        @Query("busqueda") busqueda: String = ""
    ): List<EntrenadorApi>

    @POST("entrenadores.php?registrar")
    @FormUrlEncoded
    suspend fun registrarEntrenador(
        @Field("nombre") nombre: String,
        @Field("apellido") apellido: String,
        @Field("especialidad") especialidad: String,
        @Field("telefono") telefono: String
    ): Response<Unit>

    @POST("entrenadores.php?modificar")
    @FormUrlEncoded
    suspend fun modificarEntrenador(
        @Field("id_entrenadores") id: Int,
        @Field("nombre") nombre: String,
        @Field("apellido") apellido: String,
        @Field("especialidad") especialidad: String,
        @Field("telefono") telefono: String
    ): Response<Unit>

    @POST("entrenadores.php?eliminar")
    @FormUrlEncoded
    suspend fun eliminarEntrenador(
        @Field("id_entrenadores") id: Int
    ): Response<Unit>

// MEMBRESIAS

    @GET("membresias.php?buscar")
    suspend fun buscarMembresias(
        @Query("busqueda") busqueda: String = ""
    ): List<MembresiaApi>

    @POST("membresias.php?registrar")
    @FormUrlEncoded
    suspend fun registrarMembresia(
        @Field("tipo") tipo: String,
        @Field("precio") precio: String,
        @Field("duracion_dias") duracion: String,
        @Field("id_clientes") idCliente: String,
        @Field("id_rutinas") idRutina: String
    ): Response<Unit>

    @POST("membresias.php?modificar")
    @FormUrlEncoded
    suspend fun modificarMembresia(
        @Field("id_membresias") id: Int,
        @Field("tipo") tipo: String,
        @Field("precio") precio: String,
        @Field("duracion_dias") duracion: String,
        @Field("id_clientes") idCliente: String,
        @Field("id_rutinas") idRutina: String
    ): Response<Unit>

    @POST("membresias.php?eliminar")
    @FormUrlEncoded
    suspend fun eliminarMembresia(
        @Field("id_membresias") id: Int
    ): Response<Unit>

// RUTINAS

    @GET("rutinas.php?buscar")
    suspend fun buscarRutinas(
        @Query("busqueda") busqueda: String = ""
    ): List<RutinaApi>

    @POST("rutinas.php?registrar")
    @FormUrlEncoded
    suspend fun registrarRutina(
        @Field("nombre") nombre: String,
        @Field("objetivo") objetivo: String,
        @Field("dias_semana") dias: String,
        @Field("id_entrenadores") idEntrenador: String
    ): Response<Unit>

    @POST("rutinas.php?modificar")
    @FormUrlEncoded
    suspend fun modificarRutina(
        @Field("id_rutinas") id: Int,
        @Field("nombre") nombre: String,
        @Field("objetivo") objetivo: String,
        @Field("dias_semana") dias: String,
        @Field("id_entrenadores") idEntrenador: String
    ): Response<Unit>

    @POST("rutinas.php?eliminar")
    @FormUrlEncoded
    suspend fun eliminarRutina(
        @Field("id_rutinas") id: Int
    ): Response<Unit>

// PAGOS

    @GET("pagos.php?buscar")
    suspend fun buscarPagos(
        @Query("busqueda") busqueda: String = ""
    ): List<PagoApi>

    @POST("pagos.php?registrar")
    @FormUrlEncoded
    suspend fun registrarPago(
        @Field("fecha_pago") fecha: String,
        @Field("monto") monto: String,
        @Field("metodo_pago") metodo: String,
        @Field("id_clientes") idCliente: String
    ): Response<Unit>
    ///
    @POST("pagos.php?modificar")
    @FormUrlEncoded
    suspend fun modificarPago(
        @Field("id_pagos") id: Int,
        @Field("fecha_pago") fecha: String,
        @Field("monto") monto: String,
        @Field("metodo_pago") metodo: String,
        @Field("id_clientes") idCliente: String
    ): Response<Unit>

    @POST("pagos.php?eliminar")
    @FormUrlEncoded
    suspend fun eliminarPago(
        @Field("id_pagos") id: Int
    ): Response<Unit>
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://discovery-cope-obligation-spatial.trycloudflare.com/api2gym/")
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api = retrofit.create(ApiService::class.java)

// COMPONENTE REUTILIZABLE PARA LOS COMBOBOX DE LLAVES FORANEAS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownId(
    etiqueta: String,
    idSeleccionado: String,
    opciones: List<Pair<String, String>>,
    onSeleccionar: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    val nombreSeleccionado =
        opciones.firstOrNull { it.first == idSeleccionado }?.second ?: ""

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido }
    ) {
        OutlinedTextField(
            value = nombreSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text(etiqueta) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion.second) },
                    onClick = {
                        onSeleccionar(opcion.first)
                        expandido = false
                    }
                )
            }
        }
    }
}

// NAVEGACION

@Composable
fun AppGym() {
    val context = LocalContext.current
    val preferencias = Preferencias(context)

    val destination =
        if (preferencias.obtener("id", "") != "") "menu" else "login"

    val navController = rememberNavController()

    val clientes = remember { mutableStateListOf<Registro>() }
    val membresias = remember { mutableStateListOf<Registro>() }
    val rutinas = remember { mutableStateListOf<Registro>() }
    val entrenadores = remember { mutableStateListOf<Registro>() }
    val pagos = remember { mutableStateListOf<Registro>() }

    NavHost(
        navController = navController,
        startDestination = destination
    ) {
        composable("login") { LoginGym(navController) }
        composable("menu") { MenuGym(navController) }
        composable("clientes") { ClientesGym(navController, clientes) }
        composable("membresias") { MembresiasGym(navController, membresias) }
        composable("rutinas") { RutinasGym(navController, rutinas) }
        composable("entrenadores") { EntrenadoresGym(navController, entrenadores) }
        composable("pagos") { PagosGym(navController, pagos) }
    }
}

// INICIO DE SESION

@Composable
fun LoginGym(navController: NavHostController) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val context = LocalContext.current
    val preferencias = Preferencias(context)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "PAIN ZONE GYM",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text("INICIAR SESIÓN", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val respuesta = api.iniciarSesion(usuario, contrasena)
                        val id = respuesta.body()?.trim().orEmpty()

                        if (respuesta.isSuccessful && id.toIntOrNull() != null) {
                            preferencias.guardar("id", id)
                            navController.navigate("menu") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Usuario y/o contraseña incorrecto(s).",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Log.e("API", "Error al iniciar sesión", e)
                        Toast.makeText(
                            context,
                            "No se pudo conectar con el servidor",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7967D8)
            )
        ) {
            Text("Entrar")
        }
    }
}

// MENU PRINCIPAL

@Composable
fun MenuGym(navController: NavHostController) {
    val preferencias = Preferencias(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "PAIN ZONE GYM",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text("MENÚ PRINCIPAL", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate("clientes") }, modifier = Modifier.fillMaxWidth()) {
            Text("Clientes")
        }

        Button(onClick = { navController.navigate("membresias") }, modifier = Modifier.fillMaxWidth()) {
            Text("Membresías")
        }

        Button(onClick = { navController.navigate("rutinas") }, modifier = Modifier.fillMaxWidth()) {
            Text("Rutinas")
        }

        Button(onClick = { navController.navigate("entrenadores") }, modifier = Modifier.fillMaxWidth()) {
            Text("Entrenadores")
        }

        Button(onClick = { navController.navigate("pagos") }, modifier = Modifier.fillMaxWidth()) {
            Text("Pagos")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                preferencias.remover("id")
                navController.navigate("login") {
                    popUpTo("menu") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}

// CLIENTES

@Composable
fun ClientesGym(
    navController: NavHostController,
    lista: MutableList<Registro>
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var seleccionado by remember { mutableStateOf(-1) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    fun limpiar() {
        nombre = ""
        apellido = ""
        edad = ""
        telefono = ""
        seleccionado = -1
    }

    suspend fun cargarClientes() {
        try {
            val respuesta = api.buscarClientes()

            lista.clear()
            lista.addAll(
                respuesta.map {
                    Registro(
                        it.id_clientes,
                        it.nombre,
                        it.apellido,
                        it.edad,
                        it.telefono
                    )
                }
            )

            Log.d("CLIENTES", "Clientes recibidos: ${respuesta.size}")
        } catch (e: Exception) {
            Log.e("CLIENTES", "Error al cargar clientes", e)
            Toast.makeText(
                contexto,
                "Error al cargar clientes: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        cargarClientes()
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Eliminar cliente?") },
            text = { Text("¿Estás seguro de que deseas eliminar este cliente?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (seleccionado != -1) {
                            scope.launch {
                                try {
                                    val id = lista[seleccionado].id
                                    val r = api.eliminarCliente(id)

                                    if (r.isSuccessful) {
                                        limpiar()
                                        cargarClientes()
                                        Toast.makeText(contexto, "Cliente eliminado", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(contexto, "No se pudo eliminar: ${r.code()}", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("CLIENTES", "Error al eliminar", e)
                                    Toast.makeText(contexto, "Error al eliminar: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        mostrarDialogo = false
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "GESTIÓN DE CLIENTES",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(apellido, { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(edad, { edad = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(telefono, { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && apellido.isNotBlank() && edad.isNotBlank() && telefono.isNotBlank()) {
                    scope.launch {
                        try {
                            val r = if (seleccionado == -1) {
                                api.registrarCliente(nombre, apellido, edad, telefono)
                            } else {
                                api.modificarCliente(
                                    lista[seleccionado].id,
                                    nombre,
                                    apellido,
                                    edad,
                                    telefono
                                )
                            }

                            if (r.isSuccessful) {
                                val mensaje = if (seleccionado == -1) "Cliente agregado" else "Cliente actualizado"
                                limpiar()
                                cargarClientes()
                                Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("CLIENTES", "HTTP ${r.code()}: ${r.errorBody()?.string()}")
                                Toast.makeText(contexto, "No se pudo guardar: ${r.code()}", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Log.e("CLIENTES", "Error al guardar cliente", e)
                            Toast.makeText(contexto, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(contexto, "Completa los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (seleccionado == -1) "Agregar cliente" else "Guardar cambios")
        }

        Button(
            onClick = {
                if (seleccionado != -1) mostrarDialogo = true
                else Toast.makeText(contexto, "Selecciona un cliente", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar seleccionado")
        }

        Button(
            onClick = { navController.navigate("menu") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regresar al menú")
        }

        Divider()
        Text("LISTA DE CLIENTES", fontWeight = FontWeight.Bold)

        lista.forEachIndexed { index, cliente ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable {
                        seleccionado = index
                        nombre = cliente.campo1
                        apellido = cliente.campo2
                        edad = cliente.campo3
                        telefono = cliente.campo4
                    }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Nombre: ${cliente.campo1}")
                    Text("Apellido: ${cliente.campo2}")
                    Text("Edad: ${cliente.campo3}")
                    Text("Teléfono: ${cliente.campo4}")
                    Text("Toca para editar")
                }
            }
        }
    }
}

// MEMBRESIAS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembresiasGym(
    navController: NavHostController,
    lista: MutableList<Registro>
) {
    var tipo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var idCliente by remember { mutableStateOf("") }
    var idRutina by remember { mutableStateOf("") }

    var seleccionado by remember { mutableStateOf(-1) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    var gymTipoMembresia by remember { mutableStateOf(false) }
    var tipoSeleccionado by remember { mutableStateOf("") }

    val clientesOpciones = remember { mutableStateListOf<Pair<String, String>>() }
    val rutinasOpciones = remember { mutableStateListOf<Pair<String, String>>() }

    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    fun limpiar() {
        tipo = ""
        tipoSeleccionado = ""
        precio = ""
        duracion = ""
        idCliente = ""
        idRutina = ""
        seleccionado = -1
    }

    suspend fun cargarOpciones() {
        try {
            val clientes = api.buscarClientes()
            clientesOpciones.clear()
            clientesOpciones.addAll(
                clientes.map {
                    it.id_clientes.toString() to "${it.nombre} ${it.apellido}"
                }
            )

            val rutinas = api.buscarRutinas("")
            rutinasOpciones.clear()
            rutinasOpciones.addAll(
                rutinas.map {
                    it.id_rutinas.toString() to it.nombre
                }
            )
        } catch (e: Exception) {
            Log.e("MEMBRESIAS", "Error al cargar opciones", e)
            Toast.makeText(contexto, "Error al cargar clientes o rutinas", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun cargarMembresias() {
        try {
            val respuesta = api.buscarMembresias("")
            lista.clear()
            lista.addAll(
                respuesta.map {
                    Registro(
                        it.id_membresias,
                        it.tipo,
                        it.precio,
                        it.duracion_dias,
                        it.id_clientes,
                        it.id_rutinas
                    )
                }
            )
        } catch (e: Exception) {
            Log.e("API", "Error al cargar membresías", e)
            Toast.makeText(contexto, "Error al cargar membresías", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        cargarOpciones()
        cargarMembresias()
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Eliminar membresía?") },
            text = { Text("¿Estás seguro de que deseas eliminar esta membresía?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (seleccionado != -1) {
                            scope.launch {
                                try {
                                    val r = api.eliminarMembresia(lista[seleccionado].id)
                                    if (r.isSuccessful) {
                                        limpiar()
                                        cargarMembresias()
                                        Toast.makeText(contexto, "Membresía eliminada", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(contexto, "No se pudo eliminar", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(contexto, "Error al eliminar membresía", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        mostrarDialogo = false
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("GESTIÓN DE MEMBRESÍAS", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))

        ExposedDropdownMenuBox(
            expanded = gymTipoMembresia,
            onExpandedChange = { gymTipoMembresia = !gymTipoMembresia }
        ) {
            OutlinedTextField(
                value = tipoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de membresía") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gymTipoMembresia) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = gymTipoMembresia,
                onDismissRequest = { gymTipoMembresia = false }
            ) {
                listOf("Bronce", "Plata", "Oro", "Platino", "Diamante").forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            tipoSeleccionado = it
                            tipo = it
                            gymTipoMembresia = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(precio, { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(duracion, { duracion = it }, label = { Text("Duración en días") }, modifier = Modifier.fillMaxWidth())

        DropdownId(
            etiqueta = "Cliente",
            idSeleccionado = idCliente,
            opciones = clientesOpciones,
            onSeleccionar = { idCliente = it }
        )

        DropdownId(
            etiqueta = "Rutina",
            idSeleccionado = idRutina,
            opciones = rutinasOpciones,
            onSeleccionar = { idRutina = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (tipo.isNotBlank() && precio.isNotBlank() && duracion.isNotBlank() && idCliente.isNotBlank() && idRutina.isNotBlank()) {
                    scope.launch {
                        try {
                            val r = if (seleccionado == -1) {
                                api.registrarMembresia(tipo, precio, duracion, idCliente, idRutina)
                            } else {
                                api.modificarMembresia(lista[seleccionado].id, tipo, precio, duracion, idCliente, idRutina)
                            }

                            if (r.isSuccessful) {
                                val mensaje = if (seleccionado == -1) "Membresía agregada" else "Membresía actualizada"
                                limpiar()
                                cargarMembresias()
                                Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(contexto, "No se pudo guardar la membresía", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("API", "Error al guardar membresía", e)
                            Toast.makeText(contexto, "Error al guardar membresía", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(contexto, "Completa los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (seleccionado == -1) "Agregar membresía" else "Guardar cambios")
        }

        Button(
            onClick = {
                if (seleccionado != -1) mostrarDialogo = true
                else Toast.makeText(contexto, "Selecciona una membresía", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Eliminar seleccionado") }

        Button(onClick = { navController.navigate("menu") }, modifier = Modifier.fillMaxWidth()) {
            Text("Regresar al menú")
        }

        Divider()
        Text("LISTA DE MEMBRESÍAS", fontWeight = FontWeight.Bold)

        lista.forEachIndexed { index, membresia ->
            val nombreCliente = clientesOpciones.firstOrNull { it.first == membresia.campo4 }?.second ?: membresia.campo4
            val nombreRutina = rutinasOpciones.firstOrNull { it.first == membresia.campo5 }?.second ?: membresia.campo5

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable {
                    seleccionado = index
                    tipo = membresia.campo1
                    tipoSeleccionado = membresia.campo1
                    precio = membresia.campo2
                    duracion = membresia.campo3
                    idCliente = membresia.campo4
                    idRutina = membresia.campo5
                }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Tipo: ${membresia.campo1}")
                    Text("Precio: ${membresia.campo2}")
                    Text("Duración en días: ${membresia.campo3}")
                    Text("Cliente: $nombreCliente")
                    Text("Rutina: $nombreRutina")
                    Text("Toca para editar")
                }
            }
        }
    }
}

// RUTINAS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasGym(
    navController: NavHostController,
    lista: MutableList<Registro>
) {
    var nombre by remember { mutableStateOf("") }
    var objetivo by remember { mutableStateOf("") }
    var dias by remember { mutableStateOf("") }
    var idEntrenador by remember { mutableStateOf("") }

    var seleccionado by remember { mutableStateOf(-1) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val entrenadoresOpciones = remember { mutableStateListOf<Pair<String, String>>() }

    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    fun limpiar() {
        nombre = ""
        objetivo = ""
        dias = ""
        idEntrenador = ""
        seleccionado = -1
    }

    suspend fun cargarEntrenadoresOpciones() {
        try {
            val respuesta = api.buscarEntrenadores("")
            entrenadoresOpciones.clear()
            entrenadoresOpciones.addAll(
                respuesta.map {
                    it.id_entrenadores.toString() to "${it.nombre} ${it.apellido}"
                }
            )
        } catch (e: Exception) {
            Log.e("RUTINAS", "Error al cargar entrenadores", e)
            Toast.makeText(contexto, "Error al cargar entrenadores", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun cargarRutinas() {
        try {
            val respuesta = api.buscarRutinas("")
            lista.clear()
            lista.addAll(
                respuesta.map {
                    Registro(it.id_rutinas, it.nombre, it.objetivo, it.dias_semana, it.id_entrenadores)
                }
            )
        } catch (e: Exception) {
            Log.e("API", "Error al cargar rutinas", e)
            Toast.makeText(contexto, "Error al cargar rutinas", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        cargarEntrenadoresOpciones()
        cargarRutinas()
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Eliminar rutina?") },
            text = { Text("¿Estás seguro de que deseas eliminar esta rutina?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (seleccionado != -1) {
                            scope.launch {
                                try {
                                    val r = api.eliminarRutina(lista[seleccionado].id)
                                    if (r.isSuccessful) {
                                        limpiar()
                                        cargarRutinas()
                                        Toast.makeText(contexto, "Rutina eliminada", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(contexto, "No se pudo eliminar", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(contexto, "Error al eliminar rutina", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        mostrarDialogo = false
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        Text("GESTIÓN DE RUTINAS", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre de la rutina") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(objetivo, { objetivo = it }, label = { Text("Objetivo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(dias, { dias = it }, label = { Text("Días por semana") }, modifier = Modifier.fillMaxWidth())

        DropdownId(
            etiqueta = "Entrenador",
            idSeleccionado = idEntrenador,
            opciones = entrenadoresOpciones,
            onSeleccionar = { idEntrenador = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && objetivo.isNotBlank() && dias.isNotBlank() && idEntrenador.isNotBlank()) {
                    scope.launch {
                        try {
                            val r = if (seleccionado == -1) {
                                api.registrarRutina(nombre, objetivo, dias, idEntrenador)
                            } else {
                                api.modificarRutina(lista[seleccionado].id, nombre, objetivo, dias, idEntrenador)
                            }

                            if (r.isSuccessful) {
                                val mensaje = if (seleccionado == -1) "Rutina agregada" else "Rutina actualizada"
                                limpiar()
                                cargarRutinas()
                                Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(contexto, "No se pudo guardar la rutina", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("API", "Error al guardar rutina", e)
                            Toast.makeText(contexto, "Error al guardar rutina", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(contexto, "Completa los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (seleccionado == -1) "Agregar rutina" else "Guardar cambios")
        }

        Button(
            onClick = {
                if (seleccionado != -1) mostrarDialogo = true
                else Toast.makeText(contexto, "Selecciona una rutina", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Eliminar seleccionado") }

        Button(onClick = { navController.navigate("menu") }, modifier = Modifier.fillMaxWidth()) {
            Text("Regresar al menú")
        }

        Divider()
        Text("LISTA DE RUTINAS", fontWeight = FontWeight.Bold)

        lista.forEachIndexed { index, rutina ->
            val nombreEntrenador = entrenadoresOpciones.firstOrNull { it.first == rutina.campo4 }?.second ?: rutina.campo4

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable {
                    seleccionado = index
                    nombre = rutina.campo1
                    objetivo = rutina.campo2
                    dias = rutina.campo3
                    idEntrenador = rutina.campo4
                }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Nombre: ${rutina.campo1}")
                    Text("Objetivo: ${rutina.campo2}")
                    Text("Días por semana: ${rutina.campo3}")
                    Text("Entrenador: $nombreEntrenador")
                    Text("Toca para editar")
                }
            }
        }
    }
}

// ENTRENADORES

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrenadoresGym(
    navController: NavHostController,
    lista: MutableList<Registro>
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var seleccionado by remember { mutableStateOf(-1) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    var gymEspecialidad by remember { mutableStateOf(false) }
    var especialidadSeleccionada by remember { mutableStateOf("") }

    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    fun limpiar() {
        nombre = ""
        apellido = ""
        especialidad = ""
        telefono = ""
        especialidadSeleccionada = ""
        seleccionado = -1
    }

    suspend fun cargarEntrenadores() {
        try {
            val respuesta = api.buscarEntrenadores("")
            lista.clear()
            lista.addAll(
                respuesta.map {
                    Registro(it.id_entrenadores, it.nombre, it.apellido, it.especialidad, it.telefono)
                }
            )
        } catch (e: Exception) {
            Log.e("API", "Error al cargar entrenadores", e)
            Toast.makeText(contexto, "Error al cargar entrenadores", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) { cargarEntrenadores() }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Eliminar entrenador?") },
            text = { Text("¿Estás seguro de que deseas eliminar este entrenador?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (seleccionado != -1) {
                            scope.launch {
                                try {
                                    val r = api.eliminarEntrenador(lista[seleccionado].id)
                                    if (r.isSuccessful) {
                                        limpiar()
                                        cargarEntrenadores()
                                        Toast.makeText(contexto, "Entrenador eliminado", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(contexto, "No se pudo eliminar", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(contexto, "Error al eliminar entrenador", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        mostrarDialogo = false
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        Text("GESTIÓN DE ENTRENADORES", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(apellido, { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())

        ExposedDropdownMenuBox(
            expanded = gymEspecialidad,
            onExpandedChange = { gymEspecialidad = !gymEspecialidad }
        ) {
            OutlinedTextField(
                value = especialidadSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Especialidad") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gymEspecialidad) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = gymEspecialidad,
                onDismissRequest = { gymEspecialidad = false }
            ) {
                listOf("Cardio", "Musculación", "Cross Training", "Resistencia", "Flexibilidad", "Entrenamiento funcional").forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            especialidadSeleccionada = it
                            especialidad = it
                            gymEspecialidad = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(telefono, { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && apellido.isNotBlank() && especialidad.isNotBlank() && telefono.isNotBlank()) {
                    scope.launch {
                        try {
                            val r = if (seleccionado == -1) {
                                api.registrarEntrenador(nombre, apellido, especialidad, telefono)
                            } else {
                                api.modificarEntrenador(lista[seleccionado].id, nombre, apellido, especialidad, telefono)
                            }

                            if (r.isSuccessful) {
                                val mensaje = if (seleccionado == -1) "Entrenador agregado" else "Entrenador actualizado"
                                limpiar()
                                cargarEntrenadores()
                                Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(contexto, "No se pudo guardar el entrenador", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("API", "Error al guardar entrenador", e)
                            Toast.makeText(contexto, "Error al guardar entrenador", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(contexto, "Completa los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (seleccionado == -1) "Agregar entrenador" else "Guardar cambios")
        }

        Button(
            onClick = {
                if (seleccionado != -1) mostrarDialogo = true
                else Toast.makeText(contexto, "Selecciona un entrenador", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Eliminar seleccionado") }

        Button(onClick = { navController.navigate("menu") }, modifier = Modifier.fillMaxWidth()) {
            Text("Regresar al menú")
        }

        Divider()
        Text("LISTA DE ENTRENADORES", fontWeight = FontWeight.Bold)

        lista.forEachIndexed { index, entrenador ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable {
                    seleccionado = index
                    nombre = entrenador.campo1
                    apellido = entrenador.campo2
                    especialidad = entrenador.campo3
                    especialidadSeleccionada = entrenador.campo3
                    telefono = entrenador.campo4
                }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Nombre: ${entrenador.campo1}")
                    Text("Apellido: ${entrenador.campo2}")
                    Text("Especialidad: ${entrenador.campo3}")
                    Text("Teléfono: ${entrenador.campo4}")
                    Text("Toca para editar")
                }
            }
        }
    }
}

// PAGOS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagosGym(
    navController: NavHostController,
    lista: MutableList<Registro>
) {
    var idCliente by remember { mutableStateOf("") }
    var fechaPago by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }

    var seleccionado by remember { mutableStateOf(-1) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    var gymMetodoPago by remember { mutableStateOf(false) }
    var metodoSeleccionado by remember { mutableStateOf("") }

    val clientesOpciones = remember { mutableStateListOf<Pair<String, String>>() }

    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    fun limpiar() {
        idCliente = ""
        fechaPago = ""
        monto = ""
        metodoSeleccionado = ""
        seleccionado = -1
    }

    suspend fun cargarClientesOpciones() {
        try {
            val respuesta = api.buscarClientes()
            clientesOpciones.clear()
            clientesOpciones.addAll(
                respuesta.map {
                    it.id_clientes.toString() to "${it.nombre} ${it.apellido}"
                }
            )
        } catch (e: Exception) {
            Log.e("PAGOS", "Error al cargar clientes", e)
            Toast.makeText(contexto, "Error al cargar clientes", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun cargarPagos() {
        try {
            val respuesta = api.buscarPagos("")
            lista.clear()
            lista.addAll(
                respuesta.map {
                    Registro(
                        it.id_pagos,
                        it.id_clientes,
                        it.fecha_pago,
                        it.monto,
                        it.metodo_pago
                    )
                }
            )
        } catch (e: Exception) {
            Log.e("API", "Error al cargar pagos", e)
            Toast.makeText(contexto, "Error al cargar pagos", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        cargarClientesOpciones()
        cargarPagos()
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Eliminar pago?") },
            text = { Text("¿Estás seguro de que deseas eliminar este pago?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (seleccionado != -1) {
                            scope.launch {
                                try {
                                    val r = api.eliminarPago(lista[seleccionado].id)
                                    if (r.isSuccessful) {
                                        limpiar()
                                        cargarPagos()
                                        Toast.makeText(contexto, "Pago eliminado", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(contexto, "No se pudo eliminar", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(contexto, "Error al eliminar pago", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        mostrarDialogo = false
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        Text("GESTIÓN DE PAGOS", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))

        DropdownId(
            etiqueta = "Cliente",
            idSeleccionado = idCliente,
            opciones = clientesOpciones,
            onSeleccionar = { idCliente = it }
        )

        OutlinedTextField(
            fechaPago,
            { fechaPago = it },
            label = { Text("Fecha de pago (AAAA-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            monto,
            { monto = it },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = gymMetodoPago,
            onExpandedChange = { gymMetodoPago = !gymMetodoPago }
        ) {
            OutlinedTextField(
                value = metodoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Método de pago") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gymMetodoPago) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = gymMetodoPago,
                onDismissRequest = { gymMetodoPago = false }
            ) {
                listOf("Efectivo", "Tarjeta", "Transferencia").forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            metodoSeleccionado = it
                            gymMetodoPago = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (idCliente.isNotBlank() && fechaPago.isNotBlank() && monto.isNotBlank() && metodoSeleccionado.isNotBlank()) {
                    scope.launch {
                        try {
                            val r = if (seleccionado == -1) {
                                api.registrarPago(fechaPago, monto, metodoSeleccionado, idCliente)
                            } else {
                                api.modificarPago(lista[seleccionado].id, fechaPago, monto, metodoSeleccionado, idCliente)
                            }

                            if (r.isSuccessful) {
                                val mensaje = if (seleccionado == -1) "Pago agregado" else "Pago actualizado"
                                limpiar()
                                cargarPagos()
                                Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(contexto, "No se pudo guardar el pago", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("API", "Error al guardar pago", e)
                            Toast.makeText(contexto, "Error al guardar pago", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(contexto, "Completa los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (seleccionado == -1) "Agregar pago" else "Guardar cambios")
        }

        Button(
            onClick = {
                if (seleccionado != -1) mostrarDialogo = true
                else Toast.makeText(contexto, "Selecciona un pago", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Eliminar seleccionado") }

        Button(onClick = { navController.navigate("menu") }, modifier = Modifier.fillMaxWidth()) {
            Text("Regresar al menú")
        }

        Divider()
        Text("LISTA DE PAGOS", fontWeight = FontWeight.Bold)

        lista.forEachIndexed { index, pago ->
            val nombreCliente = clientesOpciones.firstOrNull { it.first == pago.campo1 }?.second ?: pago.campo1

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable {
                    seleccionado = index
                    idCliente = pago.campo1
                    fechaPago = pago.campo2
                    monto = pago.campo3
                    metodoSeleccionado = pago.campo4
                }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Cliente: $nombreCliente")
                    Text("Fecha de pago: ${pago.campo2}")
                    Text("Monto: ${pago.campo3}")
                    Text("Método de pago: ${pago.campo4}")
                    Text("Toca para editar")
                }
            }
        }
    }
}
