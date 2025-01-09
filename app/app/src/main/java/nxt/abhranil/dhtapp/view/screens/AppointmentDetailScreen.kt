package nxt.abhranil.dhtapp.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nxt.abhranil.dhtapp.R
import nxt.abhranil.dhtapp.view.components.MetricTable
import nxt.abhranil.dhtapp.view.components.PrescriptionDownloadComponent

data class Metric(
    val metricName: String,
    val quant: String
)

val metrics = listOf(
    Metric("BP (mmHg)", "135/85"),
    Metric("Heart Rate (bpm)", "82"),
    Metric("Ejection Fraction (%)", "40"),
    Metric("Weight (kg)", "82"),
    Metric("Edema Level (1-10)", "4"),
    Metric("Symptoms", "Shortness of breath"),
    Metric("Outcome", "Stable condition"),
    Metric("Intervention/Medication", "Low Sodium Diet")
)


@Composable
fun AppointmentDetailsScreen() {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RectangleShape
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg3),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    val verticalScrollSate = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp)
            .verticalScroll(verticalScrollSate)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 40.dp),
                text = "Quarterly Checkup",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "09.01.24",
                fontSize = 16.sp,
                color = Color(0xFF6C63FF)
            )
        }
        MetricTable(metrics = metrics)
        PrescriptionDownloadComponent()
    }
}














