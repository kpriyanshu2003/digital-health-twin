package nxt.abhranil.dhtapp.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppointmentCard(
    modifier: Modifier,
    name: String,
    date: String,
    doctor: String
) {
    OutlinedCard(modifier = modifier
        .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFFF1F1F1)),
        border = BorderStroke(1.dp, Color.Transparent),
        shape = RoundedCornerShape(24.dp)) {
        Column(modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start) {
            Text(text = name,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall)

            HorizontalDivider(color = Color(0xFFB0B0B0),
                thickness = 1.dp,
                modifier = Modifier.padding(2.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Date:",
                    fontWeight = FontWeight.Normal,
                    color = Color.Black)
                Spacer(modifier = Modifier.weight( 1f))
                Text(text = date,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Doctor Name: ",
                    fontWeight = FontWeight.Normal,
                    color = Color.Black)
                Spacer(modifier = Modifier.weight( 1f))
                Text(text = doctor,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black)
            }
        }
    }
}