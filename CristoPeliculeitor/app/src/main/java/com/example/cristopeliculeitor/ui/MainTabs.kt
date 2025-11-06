package com.example.cristopeliculeitor.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cristopeliculeitor.ui.screen.BusquedaScreen
import com.example.cristopeliculeitor.ui.screen.DetalleCard
import com.example.cristopeliculeitor.ui.screen.FavoritosScreen
import com.example.cristopeliculeitor.ui.screen.PeliculasScreen
import com.example.cristopeliculeitor.ui.screen.SeriesScreen
import com.example.cristopeliculeitor.viewmodel.Modelo

// Definición de las rutas de las pestañas
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Peliculas : Screen("peliculas", "Películas", Icons.Filled.LocalMovies)
    object Series : Screen("series", "Series", Icons.Filled.Tv)
    object Favoritos : Screen("favoritos", "Favoritos", Icons.Filled.Favorite)
    object Detalles : Screen("detalles/{itemId}/{itemTipo}", "Detalles", Icons.Filled.Search)
    object Busqueda : Screen("busqueda", "Buscar", Icons.Filled.Search)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabs(modelo: Modelo = viewModel()) {
    val navController = rememberNavController()
    val items = listOf(Screen.Peliculas, Screen.Series, Screen.Favoritos)

    Scaffold(
        topBar = {
            // TopBar con Título y Botón de Búsqueda (solo en pantallas principales)
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showTopBar = items.any { it.route == currentDestination?.route }


            if (showTopBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = currentDestination?.route?.replaceFirstChar { it.uppercase() } ?: "App"
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.Busqueda.route) }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Buscar"
                            )
                        }
                    }

                )
            }
        },
        bottomBar = {
            // Mostrar barra de navegación solo en pantallas principales
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showBottomBar = items.any { it.route == currentDestination?.route }

            if (showBottomBar) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Peliculas.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pestaña de Películas
            composable(Screen.Peliculas.route) {
                PeliculasScreen(
                    modelo = modelo,
                    onPeliculaClick = { id, tipo ->
                        navController.navigate("detalles/$id/$tipo")
                    }
                )
            }

            // Pestaña de Series
            composable(Screen.Series.route) {
                SeriesScreen(
                    modelo = modelo,
                    onSerieClick = { id, tipo ->
                        navController.navigate("detalles/$id/$tipo")
                    }
                )
            }

            // Pestaña de Favoritos
            composable(Screen.Favoritos.route) {
                FavoritosScreen(
                    modelo = modelo,
                    onItemClick = { id, tipo ->
                        navController.navigate("detalles/$id/$tipo")
                    }
                )
            }

            composable(Screen.Busqueda.route) {
                BusquedaScreen(
                    modelo = modelo,
                    onBackClick = { navController.popBackStack() },
                    onItemClick = { id, tipo ->
                        navController.navigate("detalles/$id/$tipo")
                    }
                )
            }

            // Pantalla de Detalles (ruta con argumentos)
            composable(
                route = Screen.Detalles.route,
                arguments = listOf(
                    navArgument("itemId") { type = NavType.IntType },
                    navArgument("itemTipo") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                val itemTipo = backStackEntry.arguments?.getString("itemTipo") ?: ""
                DetalleCard(
                    itemId = itemId,
                    itemTipo = itemTipo,
                    onBackClick = { navController.popBackStack() },
                    modelo = modelo
                )
            }
        }
    }
}

