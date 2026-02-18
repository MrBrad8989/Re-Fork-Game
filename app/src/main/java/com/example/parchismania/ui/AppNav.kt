package com.example.parchismania.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parchismania.GameViewModel
import com.example.parchismania.ui.screens.GameScreen
import com.example.parchismania.ui.screens.ShopScreen
import com.example.parchismania.ui.screens.VictoryScreen
import com.example.parchismania.ui.pantallas.PantallaMenuPrincipal
import com.example.parchismania.ui.pantallas.PantallaConfiguracionPartida
import com.example.parchismania.ui.pantallas.PantallaConfiguracionGeneral

object Rutas {
    const val MenuPrincipal = "menu_principal"
    const val ConfiguracionPartida = "configuracion_partida"
    const val Juego = "juego"
    const val Victoria = "victoria"
    const val Tienda = "tienda"
    const val Configuracion = "configuracion"
}

@Composable
fun AppNav(vm: GameViewModel) {
    val nav = rememberNavController()
    val state by vm.gameState.collectAsState()

    LaunchedEffect(state?.gameOver) {
        if (state?.gameOver == true) {
            nav.navigate(Rutas.Victoria) {
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = nav, startDestination = Rutas.MenuPrincipal) {
        // Pantalla principal del menú
        composable(Rutas.MenuPrincipal) {
            PantallaMenuPrincipal(
                vm = vm,
                onJugar = {
                    nav.navigate(Rutas.ConfiguracionPartida) {
                        launchSingleTop = true
                    }
                },
                onConfiguracion = {
                    nav.navigate(Rutas.Configuracion) {
                        launchSingleTop = true
                    }
                },
                onTienda = {
                    nav.navigate(Rutas.Tienda) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Configuración de la partida
        composable(Rutas.ConfiguracionPartida) {
            PantallaConfiguracionPartida(
                vm = vm,
                onIniciar = {
                    nav.navigate(Rutas.Juego) {
                        launchSingleTop = true
                    }
                },
                onVolver = { nav.popBackStack() }
            )
        }

        // Pantalla de juego
        composable(Rutas.Juego) {
            GameScreen(
                vm = vm,
                onExit = {
                    vm.resetGame()
                    nav.popBackStack(Rutas.MenuPrincipal, inclusive = false)
                }
            )
        }

        // Pantalla de victoria
        composable(Rutas.Victoria) {
            VictoryScreen(
                vm = vm,
                onPlayAgain = {
                    vm.startFromSetup()
                    nav.navigate(Rutas.Juego) {
                        popUpTo(Rutas.MenuPrincipal) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBackToMenu = {
                    vm.resetGame()
                    nav.popBackStack(Rutas.MenuPrincipal, inclusive = false)
                }
            )
        }

        // Tienda
        composable(Rutas.Tienda) {
            ShopScreen(
                vm = vm,
                onBack = { nav.popBackStack() }
            )
        }

        // Configuración general
        composable(Rutas.Configuracion) {
            PantallaConfiguracionGeneral(
                vm = vm,
                onVolver = { nav.popBackStack() }
            )
        }
    }
}
