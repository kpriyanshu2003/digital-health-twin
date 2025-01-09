package nxt.abhranil.dhtapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nxt.abhranil.dhtapp.view.screens.Metric

@Composable
fun MetricTable(metrics: List<Metric>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        metrics.forEachIndexed { index, metric ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (index % 2 == 0) Color(0xFFF8F8F8) else Color.White)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                // Metric Name Column
                Text(
                    text = metric.metricName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                // Metric Value Column
                Text(
                    text = metric.quant,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}