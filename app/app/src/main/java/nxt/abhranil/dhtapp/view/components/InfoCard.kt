package nxt.abhranil.dhtapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoCard(
    modifier: Modifier,
    disease: String,
    medication: String
) {
    Box(
        modifier = modifier
            .width(330.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x307987DF))
//            .background(Color(0xFFE8F0FF))
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left content area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Section 1: Currently Suffering From
                Text(
                    text = "Currently Suffering From",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 15.sp,
                    lineHeight = 4.sp
                )
                Text(
                    text = disease,
                    color = Color.Black,
                    fontSize = 15.sp,
                    lineHeight = 4.sp
                )

                // Section 2: Current Medication
                Text(
                    text = "Current Medication",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                    ,fontSize = 15.sp,
                    lineHeight = 4.sp

                )
                Text(
                    text = medication,
                    color = Color.Black,
                    fontSize = 15.sp,
                    lineHeight = 4.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }

            // Right colored strip
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF4B6CB7)) // Dark blue color
            )
        }
    }
}
