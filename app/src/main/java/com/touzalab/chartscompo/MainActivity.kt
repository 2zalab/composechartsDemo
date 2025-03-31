package com.touzalab.chartscompo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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

// Définition de nos couleurs de thème vert
val PrimaryGreen = Color(0xFF2E7D32)
val LightGreen = Color(0xFF4CAF50)
val DarkGreen = Color(0xFF1B5E20)
val BackgroundColor = Color(0xFFF5F9F5)
val SurfaceColor = Color(0xFFFFFFFF)
val TextColor = Color(0xFF212121)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = DarkGreen.toArgb()
        setContent {
            GreenChartTheme {
                ChartsDemoApp()
            }
        }
    }
}

@Composable
fun GreenChartTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = PrimaryGreen,
        onPrimary = Color.White,
        primaryContainer = LightGreen,
        onPrimaryContainer = Color.White,
        secondary = LightGreen,
        onSecondary = TextColor,
        background = BackgroundColor,
        onBackground = TextColor,
        surface = SurfaceColor,
        onSurface = TextColor,
        surfaceVariant = Color(0xFFEDF7ED)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsDemoApp() {
    // Type de graphique sélectionné
    var selectedChartType by remember { mutableStateOf(ChartType.LINE) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ComposeCharts Démo",
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundColor)
        ) {
            // Menu scrollable de sélection du type de graphique
            ChartTypeSelector(
                selectedChartType = selectedChartType,
                onChartTypeSelected = { selectedChartType = it }
            )

            // Zone de séparation
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            // Boîte contenant le graphique
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(5.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceColor
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        //.padding(5.dp)
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
    }
}

enum class ChartType(val displayName: String) {
    LINE("Linéaire"),
    BAR("Barres"),
    PIE("Camembert"),
    HISTOGRAM("Histogramme"),
    RADAR("Radar")
}

@Composable
fun ChartTypeSelector(
    selectedChartType: ChartType,
    onChartTypeSelected: (ChartType) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(ChartType.entries) { chartType ->
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
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = if (isSelected) PrimaryGreen else Color.White,
            contentColor = if (isSelected) Color.White else PrimaryGreen
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.height(44.dp)
    ) {
        Text(type.displayName, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun LineChartExample() {
    val salesData = createSalesData()

    Column {
        ChartTitleHeader(title = "Évolution des Ventes")

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
                .height(500.dp)
        )
    }
}

@Composable
fun BarChartExample() {
    val salesData = createSalesData()

    Column {
        ChartTitleHeader(title = "Comparaison des Ventes")

        BarChart(
            dataSeries = salesData,
            title = "Ventes par mois",
            xAxisTitle = "Mois",
            yAxisTitle = "Ventes (K€)",
            stacked = false,
            horizontal = false,
            style = ChartThemes.Colorful,
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
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
        ChartTitleHeader(title = "Répartition des Ventes")

        PieChart(
            segments = segments,
            title = "Ventes par produit",
            donut = true,
            showPercentages = true,
            //highlightSelection = true,
            //explodeSelection = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(bottom = 16.dp)
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
        ChartTitleHeader(title = "Distribution des Âges")

        Histogram(
            data = ages,
            title = "Âges des clients",
            xAxisTitle = "Âge",
            yAxisTitle = "Fréquence",
            bins = 10,
            barColor = ColorPalettes.Vibrant[1],
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
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
        ChartTitleHeader(title = "Comparaison de Produits")

        RadarChart(
            dataSeries = dataSeries,
            categories = categories,
            title = "Comparaison des performances",
            fillArea = true,
            showPoints = true,
            style = ChartThemes.Colorful,
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
        )
    }
}

@Composable
fun ChartTitleHeader(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = DarkGreen,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    )
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