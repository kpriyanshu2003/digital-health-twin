package nxt.abhranil.dhtapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MetricCard(metricName: String,
               quantity: String,
               unit: String) {
    Box(
        modifier = Modifier
            .size(width = 70.dp, height = 100.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Background split into two halves
        Column(modifier = Modifier.fillMaxSize()) {
            // Top half
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.5f)
                    .background(Color(0x307987DF)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Weight text (top)
                    Text(
                        text = quantity,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Label text (bottom)
                    Text(
                        text = unit,
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // Bottom half
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF4B6CB7))
                    .padding(4.dp),
                contentAlignment = Alignment.Center// Dark blue
            ) {
                Text(
                    text = metricName,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewWeightCard() {
    MetricCard(
        metricName = "Weight",
        quantity = "72.5",
        unit = "kg"
    )
}

