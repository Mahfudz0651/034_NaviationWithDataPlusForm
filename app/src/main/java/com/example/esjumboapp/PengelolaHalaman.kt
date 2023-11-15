@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.esjumboapp

import Data.SumberData.flavors
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import theme.OrderViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

enum class PengelolaHalaman {
    Home,
    Formulir,
    Rasa,
    Summary,
    Detail
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsJumboAppBar(
    bisaNavigasiBack: Boolean,
    navigasiUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text (stringResource(id = R.string.app_name))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor =
                MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (bisaNavigasiBack) {
                IconButton(onClick = navigasiUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription =
                            stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsJumboApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            EsJumboAppBar(
                bisaNavigasiBack = false,
                navigasiUp = { /*TODO: implement back navigation*/
                }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.stateUI.collectAsState()
        NavHost(
            navController = navController,
            startDestination = PengelolaHalaman.Home.name,
            modifier = Modifier.padding(innerPadding)
        )
        {
            composable(route = PengelolaHalaman.Home.name) {
                HalamanHome(
                    onNextButtonCliked = {
                        navController.navigate(PengelolaHalaman.Formulir.name)
                    })
            }
            composable(PengelolaHalaman.Formulir.name) {
                HalamanForm(
                    onSubmitButtonClicked = {
                        viewModel.setContact(it)
                        navController.navigate(PengelolaHalaman.Detail.name)
                    })
            }
            composable(route = PengelolaHalaman.Rasa.name) {

                val context = LocalContext.current
                HalamanSatu(
                    pilihanRasa = flavors.map { id ->
                        context.resources.getString(id)
                    },
                    onSelectionChanged = { viewModel.setRasa(it) },
                    onConfirmButtonCliked = {
                        viewModel.setJumlah(it)
                    },
                    onNextButtonClicked = {
                        navController.navigate(PengelolaHalaman.Summary.name)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToHome(
                            viewModel,
                            navController
                        )
                    })
            }
            composable(route = PengelolaHalaman.Summary.name) {
                HalamanDua(
                    orderUIState = uiState,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToRasa(navController)
                    },
                )
            }
        }
    }


}

private fun cancelOrderAndNavigateToHome(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(
        PengelolaHalaman.Home.name, inclusive
    = false)
}
private fun cancelOrderAndNavigateToRasa(
    navController: NavHostController
) {
    navController.popBackStack(
        PengelolaHalaman.Rasa.name, inclusive
    = false)
}


