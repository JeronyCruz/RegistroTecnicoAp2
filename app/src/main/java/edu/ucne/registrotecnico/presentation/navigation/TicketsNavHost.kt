//package edu.ucne.registrotecnico.presentation.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.toRoute
//import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
//import edu.ucne.registrotecnico.data.local.entities.TicketEntity
//import edu.ucne.registrotecnico.presentation.prioridades.PrioridadListScreen
//import edu.ucne.registrotecnico.presentation.prioridades.PrioridadScreen
//import edu.ucne.registrotecnico.presentation.prioridades.PrioridadesViewModel
//import edu.ucne.registrotecnico.presentation.tickets.TicketListScreen
//import edu.ucne.registrotecnico.presentation.tickets.TicketScreen
//import edu.ucne.registrotecnico.presentation.tickets.TicketsViewModel
//
//@Composable
//fun TicketsNavHost(
//    navHostController: NavHostController,
//    ticketList: List<TicketEntity>,
//    viewModel: TicketsViewModel,
//    navcontrol: NavController
//) {
//    NavHost(
//        navController = navHostController,
//        startDestination = Screen.TicketList
//    ) {
//        composable<Screen.TicketList> {
//            TicketListScreen(
//                ticketList = ticketList,
//                onEditClick = { ticketId ->
//                    navHostController.navigate(Screen.Ticket(ticketId))
//                },
//                onDeleteClick = { ticket ->
//                    viewModel.deleteTicket(ticket)
//                }
//            )
//        }
//
//        composable<Screen.Ticket> { backStack ->
//            val ticketdId = backStack.toRoute<Screen.Ticket>().ticketId
//            TicketScreen(
//                ticketdId,
//                viewModel,
//                navcontrol
//            ) {
//            }
//        }
//    }
//}
