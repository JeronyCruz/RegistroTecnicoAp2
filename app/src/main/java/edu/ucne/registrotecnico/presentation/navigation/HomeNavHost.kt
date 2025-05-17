package edu.ucne.registrotecnico.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.presentation.home.HomeScreen
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
            val prioridades by prioridadesViewModel.prioridades.collectAsState()

            PrioridadListScreen(
                prioridadList = prioridades,
                onEditClick = { id ->
                    navHostController.navigate(Screen.Prioridad(id ?: 0))
                },
                onDeleteClick = { prioridad ->
                    prioridadesViewModel.deletePrioridad(prioridad)
                }
            )
        }

        composable<Screen.Prioridad> { backStack ->
            val prioridadId = backStack.toRoute<Screen.Prioridad>().priodidadId
            PrioridadScreen(
                prioridadId = prioridadId,
                viewModel = prioridadesViewModel,
                navController = navHostController,
                function = { navHostController.popBackStack() }
            )
        }

        // Pantallas de Técnicos
        composable<Screen.TecnicoList> {
            val tecnicos by tecnicosViewModel.tecnicos.collectAsState()

            TecnicoListScreen(
                tecnicoList = tecnicos,
                onEditClick = { id ->
                    navHostController.navigate(Screen.Tecnico(id ?: 0))
                },
                onDeleteClick = { tecnico ->
                    tecnicosViewModel.deleteTecnico(tecnico)
                }
            )
        }

        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(
                tecniId = tecnicoId,
                viewModel = tecnicosViewModel,
                navController = navHostController,
                function = { navHostController.popBackStack() }
            )
        }

        // Pantallas de Tickets
        composable<Screen.TicketList> {
            val tickets by ticketsViewModel.ticketsS.collectAsState()

            TicketListScreen(
                ticketList = tickets,
                onEditClick = { id ->
                    navHostController.navigate(Screen.Ticket(id ?: 0))
                },
                onDeleteClick = { ticket ->
                    ticketsViewModel.deleteTicket(ticket)
                }
            )
        }

        composable<Screen.Ticket> { backStack ->
            val ticketId = backStack.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketId = ticketId,
                viewModel = ticketsViewModel,
                navController = navHostController,
                function = { navHostController.popBackStack() }
            )
        }
    }
}