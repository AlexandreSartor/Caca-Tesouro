package com.example.app_tesouro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TreasureApp()
        }
    }
}

@Composable
fun TreasureApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {

        // Tela Inicial
        composable("home") { HomeScreen(navController) }

        // Telas de Pistas (com resposta)
        composable("pista1") {
            PistaScreen(
                navController = navController,
                pista = "Qual seleção ganhou a Copa do Mundo de 2022?",
                respostaCorreta = "argentina",
                proximaTela = "pista2"
            )
        }

        composable("pista2") {
            PistaScreen(
                navController = navController,
                pista = "Qual jogador é conhecido como CR7?",
                respostaCorreta = "cristiano ronaldo",
                proximaTela = "pista3"
            )
        }

        composable("pista3") {
            PistaScreen(
                navController = navController,
                pista = "Qual time brasileiro tem o símbolo de um leão?",
                respostaCorreta = "sport",
                proximaTela = "treasure"
            )
        }

        // Tela final
        composable("treasure") { TreasureScreen(navController) }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Caça ao Tesouro Futebol") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { navController.navigate("pista1") }) {
                Text("Iniciar Caça ao Tesouro")
            }
        }
    }
}

@Composable
fun PistaScreen(
    navController: NavHostController,
    pista: String,
    respostaCorreta: String,
    proximaTela: String
) {
    var respostaUsuario by remember { mutableStateOf(TextFieldValue("")) }
    var mensagemErro by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pista") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = pista, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = respostaUsuario,
                onValueChange = { respostaUsuario = it },
                label = { Text("Digite sua resposta") }
            )

            if (mensagemErro.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = mensagemErro, color = MaterialTheme.colors.error)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Voltar")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    if (respostaUsuario.text.trim().lowercase() == respostaCorreta.lowercase()) {
                        mensagemErro = ""
                        navController.navigate(proximaTela)
                    } else {
                        mensagemErro = "Resposta incorreta! Tente novamente."
                    }
                }) {
                    Text("Enviar Resposta")
                }
            }
        }
    }
}

@Composable
fun TreasureScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Tesouro Encontrado!") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "🎉 Parabéns! Você encontrou o tesouro do futebol! ⚽",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }) {
                    Text("Voltar à Tela Inicial")
                }
            }
        }
    }
}
