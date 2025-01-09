import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nxt.abhranil.dhtapp.view.components.InfoCard
import nxt.abhranil.dhtapp.view.components.MetricCard
import nxt.abhranil.dhtapp.R
import java.util.Calendar

@Composable
fun PatientDashboardScreen(navController: NavController) {
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
        // Greeting and Patient Info
        Column(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val greeting = when (currentHour) {
                in 0..11 -> "Good morning"
                in 12..17 -> "Good afternoon"
                else -> "Good evening"
            }
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 40.dp),
                text = greeting,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "72 years, MALE",
                fontSize = 16.sp,
                color = Color(0xFF6C63FF)
            )
        }


        val scrollableState = rememberScrollState()
        Row(
            modifier = Modifier
                .horizontalScroll(scrollableState)
        ) {
            InfoCard()
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            InfoCard()
        }

        // Health Metrics Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            // Metrics Grid
            HealthMetricsGrid()

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                RiskIndicator()
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AlertCard(message = "! Edema spike - Reduce sodium intake, consider a diuretic dose adjustment.")
                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons
                    ActionButtons()
                }
            }

        }


    }
}

@Composable
fun HealthMetricsGrid() {
    data class Metric(
        val value: String,
        val unit: String,
        val label: String
    )
    val metricsList = listOf<Metric>(
        Metric("140/90", "mmHg", "Blood Pressure"),
        Metric("85", "bpm", "Heart Rate"),
        Metric("94", "%", "Oxygen Generation"),
        Metric("80.5", "kg", "Weight"),
        Metric("24.75", "", "BMI"),
        Metric("6.0", "mg/dL", "Uric Acid"),
        Metric("138", "mmol/L", "Sodium"),
        Metric("4.5", "mmol/L", "Potassium"),
        Metric("200", "mg/dL", "Cholesterol")
    )

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(220.dp)
                .width(130.dp) // Set the desired size for the Box
        ) {
            Image(
                painter = painterResource(id = R.drawable.human_body_ic),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(), // Ensure the image fills the Box completely
                contentScale = ContentScale.FillBounds // Scale the image as needed
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in metricsList.chunked(3)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    i.forEach { metric ->
                        MetricCard(
                            metricName = metric.label,
                            quantity = metric.value,
                            unit = metric.unit
                        )
                    }
                }
            }
        }
    }


}


@Composable
fun RiskIndicator() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFF5F6FA), shape = CircleShape)
                .border(width = 4.dp, color = Color(0xFF1E2F98), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "5.26 %",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Text(
            modifier = Modifier.width(150.dp),
            text = "Risk of Deterioration in Next 30 Days",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AlertCard(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF5F5), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = message,
            fontSize = 12.sp,
            color = Color.Red
        )
    }
}

@Composable
fun ActionButtons() {
    Column {
        Button(
            onClick = { /* View Medical History Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "View Medical History", color = Color.White)
        }

        Button(
            onClick = { /* Personalized Tips Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Personalized Tips", color = Color.White)
        }
    }
}
