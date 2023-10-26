@file:Suppress("DEPRECATION")

package com.example.ejerciciosdia14.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ejerciciosdia14.components.Alert
import com.example.ejerciciosdia14.components.MainButton
import com.example.ejerciciosdia14.components.MainTextField
import com.example.ejerciciosdia14.components.ShowInfoCards
import com.example.ejerciciosdia14.components.SpaceH
import com.example.ejerciciosdia14.viewmodels.PrestamoViewModel
import java.math.BigDecimal

@Composable
fun ContentHomeView(paddingValues: PaddingValues, viewModel: PrestamoViewModel) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val state = viewModel.state

        ShowInfoCards(
            titleInteres = "Total: ",
            montoInteres = state.montoInteres,
            titleMonto = "Cuota: ",
            monto = state.montoCuota
        )

        MainTextField(
            value = state.montoPrestamo,
            onValueChange = {
                viewModel.onValue(value = it, campo = "montoPrestamo")
            },
            label = "Prestamo"
        )
        SpaceH()
        MainTextField(
            value = state.cantCuotas,
            onValueChange = {
                viewModel.onValue(value = it, campo = "cuotas")
            },
            label = "Cuotas"
        )
        SpaceH(10.dp)
        MainTextField(
            value = state.tasa,
            onValueChange = {
                viewModel.onValue(value = it, campo = "tasa")
            },
            label = "Tasa"
        )
        SpaceH(10.dp)
        MainButton(
            text = "Calcular",
        ) {
            viewModel.calcular()
        }
        MainButton(text = "Limpiar", color = Color.Red) {
            viewModel.limpiar()
        }
        if (viewModel.state.showAlert) {
            Alert(
                title = "Alerta",
                message = "Ingresa los datos",
                confirmText = "Aceptar",
                onConfirmClick = {
                    viewModel.confirmDialog()
                },
            ) {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: PrestamoViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Calculadora prestamos", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) {
        ContentHomeView(it, viewModel)
    }
}

fun calcularTotal(montoPresatmo: Double, cantCuotas: Int, tasaInteresAnual: Double): Double {
    val res = cantCuotas * calcularCuota(montoPresatmo, cantCuotas, tasaInteresAnual)
    return BigDecimal(res).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
}

fun calcularCuota(montoPresatmo: Double, cantCuotas: Int, tasaInteresAnual: Double): Double {
    val tasaInteresMensual = tasaInteresAnual / 12 / 100
    val cuota = montoPresatmo * tasaInteresMensual * Math.pow(
        1 + tasaInteresMensual,
        cantCuotas.toDouble()
    ) / (Math.pow(1 + tasaInteresMensual, cantCuotas.toDouble()) - 1)
    val cuotaRedondeada = BigDecimal(cuota).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

    return cuotaRedondeada
    return cuota
}