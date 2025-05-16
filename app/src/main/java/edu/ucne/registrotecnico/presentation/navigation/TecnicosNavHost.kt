package edu.ucne.registrotecnico.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicosViewModel

@Composable
fun TecnicosNavHost(
    navHostController: NavHostController,
    tecnicoList: List<TecnicoEntity>,
    viewModel: TecnicosViewModel,
    navcontrol: NavController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.TecnicoList
    ) {
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                tecnicoList = tecnicoList,
                onEditClick = { tecnicoId ->
                    navHostController.navigate(Screen.Tecnico(tecnicoId))
                },
                onDeleteClick = { tecnico ->
                   viewModel.deleteTecnico(tecnico)
                }
            )
        }

        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(
                tecnicoId,
                viewModel,
                navcontrol
            ) {
            }
        }
    }
}
