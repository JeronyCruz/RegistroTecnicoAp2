package edu.ucne.registrotecnico.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnico.presentation.home.HomeScreen
import edu.ucne.registrotecnico.presentation.mensaje.MensajeScreen
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadListScreen
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadScreen
import edu.ucne.registrotecnico.presentation.prioridades.PrioridadesViewModel
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnico.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnico.presentation.tickets.TicketScreen
import edu.ucne.registrotecnico.presentation.tickets.TicketsViewModel

@Composable
fun HomeNavHost(
    navHostController: NavHostController,
    prioridadesViewModel: PrioridadesViewModel,
    tecnicosViewModel: TecnicosViewModel,
    ticketsViewModel: TicketsViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(navController = navHostController)
        }

        // Pantallas de Prioridades
        composable<Screen.PrioridadList> {
            PrioridadListScreen(
                goToPrioridad = { id ->
                    navHostController.navigate(Screen.Prioridad(id))
                },
                createPrioridad = {
                    navHostController.navigate(Screen.Prioridad(null))
                }
            )
        }

        composable<Screen.Prioridad> { backStack ->
            val prioridadId = backStack.toRoute<Screen.Prioridad>().priodidadId
            PrioridadScreen(
                prioridadId = prioridadId ?: 0, // Manejo del valor null
                goBack = { navHostController.popBackStack() }
            )
        }

        // Pantallas de Técnicos
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                goToTecnico = { id ->
                    navHostController.navigate(Screen.Tecnico(id))
                },
                createTecnico = {
                    navHostController.navigate(Screen.Tecnico(null))
                }
            )
        }

        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(
                tecnicoId = tecnicoId ?: 0, // Manejo del valor null
                goBack = { navHostController.popBackStack() }
            )
        }

        // Pantallas de Tickets
        composable<Screen.TicketList> {
            TicketListScreen(
                goToTicket = { id ->
                    navHostController.navigate(Screen.Ticket(id))
                },
                createTicket = {
                    navHostController.navigate(Screen.Ticket(null))
                },
                goToMessages = { ticketId ->
                    // Asegurarnos de que ticketId no sea null aquí
                    require(ticketId != null) { "Ticket ID no puede ser null" }
                    navHostController.navigate(Screen.Mensaje(ticketId))
                }
            )
        }

        composable<Screen.Ticket> { backStack ->
            val ticketId = backStack.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketId = ticketId ?: 0, // Manejo del valor null
                goBack = { navHostController.popBackStack() }
            )
        }

        composable<Screen.Mensaje> { backStack ->
            val ticketId = backStack.toRoute<Screen.Mensaje>().ticketId
            require(ticketId != null) { "Ticket ID no puede ser null para MensajeScreen" }
            MensajeScreen(
                ticketId = ticketId,
                onBackClick = { navHostController.popBackStack() }
            )
        }
    }
}