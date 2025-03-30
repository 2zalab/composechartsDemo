package com.touzalab.chartscompo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.touzalab.composecharts.components.*
import com.touzalab.composecharts.data.DataPoint
import com.touzalab.composecharts.data.DataSeries
import com.touzalab.composecharts.data.PieChartSegment
import com.touzalab.composecharts.theme.ChartThemes
import com.touzalab.composecharts.theme.ColorPalettes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ChartsDemoApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsDemoApp() {
    // Type de graphique sélectionné
    var selectedChartType by remember { mutableStateOf(ChartType.LINE) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barre supérieure avec le titre
        TopAppBar(
            title = { Text("ComposeCharts Demo") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White
            )
        )

        // Menu de sélection du type de graphique
        ChartTypeSelector(
            selectedChartType = selectedChartType,
            onChartTypeSelected = { selectedChartType = it }
        )

        // Graphique sélectionné
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (selectedChartType) {
                ChartType.LINE -> LineChartExample()
                ChartType.BAR -> BarChartExample()
                ChartType.PIE -> PieChartExample()
                ChartType.HISTOGRAM -> HistogramExample()
                ChartType.RADAR -> RadarChartExample()
            }
        }
    }
}

enum class ChartType {
    LINE, BAR, PIE, HISTOGRAM, RADAR
}

@Composable
fun ChartTypeSelector(
    selectedChartType: ChartType,
    onChartTypeSelected: (ChartType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ChartType.entries.forEach { chartType ->
            ChartTypeButton(
                type = chartType,
                isSelected = chartType == selectedChartType,
                onClick = { onChartTypeSelected(chartType) }
            )
        }
    }
}

@Composable
fun ChartTypeButton(
    type: ChartType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(type.name)
    }
}

@Composable
fun LineChartExample() {
    val salesData = createSalesData()

    Column {
        Text(
            text = "Évolution des Ventes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        LineChart(
            dataSeries = salesData,
            title = "Ventes mensuelles",
            xAxisTitle = "Mois",
            yAxisTitle = "Ventes (K€)",
            smoothCurve = true,
            showPoints = true,
            fillArea = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
fun BarChartExample() {
    val salesData = createSalesData()

    Column {
        Text(
            text = "Comparaison des Ventes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        BarChart(
            dataSeries = salesData,
            title = "Ventes par mois",
            xAxisTitle = "Mois",
            yAxisTitle = "Ventes (K€)",
            stacked = false,
            horizontal = false,
            //showValue = true,
            style = ChartThemes.Colorful,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
fun PieChartExample() {
    val segments = listOf(
        PieChartSegment(label = "Produit A", value = 35f, color = ColorPalettes.Pastel[0]),
        PieChartSegment(label = "Produit B", value = 25f, color = ColorPalettes.Pastel[1]),
        PieChartSegment(label = "Produit C", value = 20f, color = ColorPalettes.Pastel[2]),
        PieChartSegment(label = "Produit D", value = 15f, color = ColorPalettes.Pastel[3]),
        PieChartSegment(label = "Produit E", value = 5f, color = ColorPalettes.Pastel[4])
    )

    Column {
        Text(
            text = "Répartition des Ventes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        PieChart(
            segments = segments,
            title = "Ventes par produit",
            donut = true,
            showPercentages = true,
            //highlightSelection = true,
            //explodeSelection = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
fun HistogramExample() {
    val ages = listOf(
        22f, 23f, 24f, 25f, 26f, 27f, 27f, 28f, 28f, 29f,
        30f, 30f, 30f, 31f, 31f, 32f, 32f, 33f, 34f, 35f,
        35f, 36f, 37f, 38f, 39f, 40f, 41f, 42f, 43f, 45f,
        46f, 48f, 50f, 52f, 55f, 58f, 60f, 62f, 65f
    )

    Column {
        Text(
            text = "Distribution des Âges",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Histogram(
            data = ages,
            title = "Âges des clients",
            xAxisTitle = "Âge",
            yAxisTitle = "Fréquence",
            bins = 8,
            barColor = ColorPalettes.Vibrant[2],
            //showValues = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
fun RadarChartExample() {
    val categories = listOf("Vitesse", "Puissance", "Autonomie", "Confort", "Prix", "Design")
    val dataSeries = listOf(
        DataSeries(
            name = "Modèle X",
            color = ColorPalettes.Vibrant[0],
            points = listOf(
                DataPoint(x = 0f, y = 80f, label = "Vitesse"),
                DataPoint(x = 1f, y = 90f, label = "Puissance"),
                DataPoint(x = 2f, y = 70f, label = "Autonomie"),
                DataPoint(x = 3f, y = 85f, label = "Confort"),
                DataPoint(x = 4f, y = 60f, label = "Prix"),
                DataPoint(x = 5f, y = 95f, label = "Design")
            )
        ),
        DataSeries(
            name = "Modèle Y",
            color = ColorPalettes.Vibrant[1],
            points = listOf(
                DataPoint(x = 0f, y = 70f, label = "Vitesse"),
                DataPoint(x = 1f, y = 65f, label = "Puissance"),
                DataPoint(x = 2f, y = 90f, label = "Autonomie"),
                DataPoint(x = 3f, y = 75f, label = "Confort"),
                DataPoint(x = 4f, y = 80f, label = "Prix"),
                DataPoint(x = 5f, y = 85f, label = "Design")
            )
        )
    )

    Column {
        Text(
            text = "Comparaison de Produits",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        RadarChart(
            dataSeries = dataSeries,
            categories = categories,
            title = "Comparaison des performances",
            fillArea = true,
            showPoints = true,
            style = ChartThemes.Dark,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )
    }
}

fun createSalesData(): List<DataSeries> {
    return listOf(
        DataSeries(
            name = "Ventes 2024",
            color = ColorPalettes.Default[0],
            points = listOf(
                DataPoint(x = 0f, y = 10f, label = "Jan"),
                DataPoint(x = 1f, y = 15f, label = "Fév"),
                DataPoint(x = 2f, y = 8f, label = "Mar"),
                DataPoint(x = 3f, y = 12f, label = "Avr"),
                DataPoint(x = 4f, y = 20f, label = "Mai"),
                DataPoint(x = 5f, y = 18f, label = "Jun")
            )
        ),
        DataSeries(
            name = "Ventes 2023",
            color = ColorPalettes.Default[1],
            points = listOf(
                DataPoint(x = 0f, y = 8f, label = "Jan"),
                DataPoint(x = 1f, y = 12f, label = "Fév"),
                DataPoint(x = 2f, y = 10f, label = "Mar"),
                DataPoint(x = 3f, y = 9f, label = "Avr"),
                DataPoint(x = 4f, y = 15f, label = "Mai"),
                DataPoint(x = 5f, y = 16f, label = "Jun")
            )
        )
    )
}