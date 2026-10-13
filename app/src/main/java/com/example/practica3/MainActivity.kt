package com.example.practica3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//este es un cambio
data class Registro(
    val campos: List<String>
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                val clientes = remember { mutableStateListOf<Registro>() }
                val membresias = remember { mutableStateListOf<Registro>() }
                val rutinas = remember { mutableStateListOf<Registro>() }
                val entrenadores = remember { mutableStateListOf<Registro>() }
                val pagos = remember { mutableStateListOf<Registro>() }

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginGym(navController)
                    }

                    composable("menu") {
                        MenuGym(navController)
                    }
//JUAN JOSE ARREDONDO ZAVALA AGREGUE EL CODIGO DE LA TABLA DE CLIENTES
                    composable("clientes") {
                        ModuloGym(
                            "Clientes",
                            listOf("Nombre completo", "Teléfono", "Correo electrónico"),
                            clientes,
                            navController
                        )
                    }
//MARTIN JAREK AGREGUE EL CODIGO DE LA TABLA DE MEMBRESIAS
                    composable("membresias") {
                        ModuloGym(
                            "Membresías",
                            listOf("Tipo de membresía", "Duración", "Precio"),
                            membresias,
                            navController
                        )
                    }
//BRAYAN EDUARDO AGREGUE EL CODIGO DE LA TABLA DE RUTINAS
                    composable("rutinas") {
                        ModuloGym(
                            "Rutinas",
                            listOf("Nombre de rutina", "Descripción", "Días por semana"),
                            rutinas,
                            navController
                        )
                    }
//AHARON EZEQUIEL AGREGUE EL CODIGO DE LA TABLA DE ENTRENADORES
                    composable("entrenadores") {
                        ModuloGym(
                            "Entrenadores",
                            listOf("Nombre completo", "Especialidad", "Teléfono"),
                            entrenadores,
                            navController
                        )
                    }

                    composable("pagos") {
                        ModuloGym(
                            "Pagos",
                            listOf("Nombre del cliente", "Concepto del pago", "Cantidad"),
                            pagos,
                            navController
                        )
                    }
                }
            }
        }
    }
}
//JUAN JOSE ARREDONDO ZAVALA AGREGUE EL CODIGO INICIO DE SESION
@Composable
fun LoginGym(navController: NavController) {
    val context = LocalContext.current
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "PAIN ZONE GYM",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(8.dp))
        Text("Sistema de administración del gimnasio")

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (usuario.isNotBlank() && contrasena.isNotBlank()) {
                    navController.navigate("menu")
                } else {
                    Toast.makeText(
                        context,
                        "Ingresa usuario y contraseña",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
    }
}
//JUAN JOSE ARREDONDO ZAVALA AGREGUE EL CODIGO DEL MENU
@Composable
fun MenuGym(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "MENÚ PRINCIPAL",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))
        Text("Administración del gimnasio")
        Spacer(Modifier.height(24.dp))

        val modulos = listOf(
            "Clientes" to "clientes",
            "Membresías" to "membresias",
            "Rutinas" to "rutinas",
            "Entrenadores" to "entrenadores",
            "Pagos" to "pagos"
        )

        modulos.forEach { (nombre, ruta) ->
            Button(
                onClick = { navController.navigate(ruta) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(nombre)
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
fun ModuloGym(
    titulo: String,
    etiquetas: List<String>,
    registros: MutableList<Registro>,
    navController: NavController
) {
    val context = LocalContext.current

    var campo1 by remember { mutableStateOf("") }
    var campo2 by remember { mutableStateOf("") }
    var campo3 by remember { mutableStateOf("") }
    var indiceEdicion by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "GESTIÓN DE $titulo".uppercase(),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = campo1,
            onValueChange = { campo1 = it },
            label = { Text(etiquetas[0]) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = campo2,
            onValueChange = { campo2 = it },
            label = { Text(etiquetas[1]) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = campo3,
            onValueChange = { campo3 = it },
            label = { Text(etiquetas[2]) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                if (
                    campo1.isNotBlank() &&
                    campo2.isNotBlank() &&
                    campo3.isNotBlank()
                ) {
                    val registro = Registro(
                        listOf(campo1.trim(), campo2.trim(), campo3.trim())
                    )

                    if (indiceEdicion >= 0) {
                        registros[indiceEdicion] = registro
                    } else {
                        registros.add(registro)
                    }

                    campo1 = ""
                    campo2 = ""
                    campo3 = ""
                    indiceEdicion = -1

                    Toast.makeText(
                        context,
                        "Registro guardado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (indiceEdicion >= 0) "Guardar cambios" else "Registrar")
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()

        Text(
            "LISTA DE $titulo".uppercase(),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        if (registros.isEmpty()) {
            Text("Todavía no hay registros.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(registros) { index, registro ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                "${etiquetas[0]}: ${registro.campos[0]}",
                                fontWeight = FontWeight.Bold
                            )
                            Text("${etiquetas[1]}: ${registro.campos[1]}")
                            Text("${etiquetas[2]}: ${registro.campos[2]}")

                            Spacer(Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        campo1 = registro.campos[0]
                                        campo2 = registro.campos[1]
                                        campo3 = registro.campos[2]
                                        indiceEdicion = index
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Editar")
                                }

                                OutlinedButton(
                                    onClick = {
                                        registros.removeAt(index)

                                        if (indiceEdicion == index) {
                                            campo1 = ""
                                            campo2 = ""
                                            campo3 = ""
                                            indiceEdicion = -1
                                        } else if (indiceEdicion > index) {
                                            indiceEdicion--
                                        }

                                        Toast.makeText(
                                            context,
                                            "Registro eliminado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regresar al menú")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaLogin() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("GYM FITNESS", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Usuario") }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = {}) {
                Text("Iniciar sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaMenu() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("MENÚ PRINCIPAL", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Administración del gimnasio")

            listOf(
                "Clientes",
                "Membresías",
                "Rutinas",
                "Entrenadores",
                "Pagos"
            ).forEach { nombre ->
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(nombre)
                }
            }
        }
    }
}