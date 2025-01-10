package nxt.abhranil.dhtapp.view.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import nxt.abhranil.dhtapp.R

@Composable
fun PrescriptionDownloadComponent(imageUrl: String) {

    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(16.dp)
            .border(width = 1.dp, color = Color.LightGray)
            .background(Color(0xFFF8F8F8)), // Light gray background
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(
                text = "Prescription",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // Handle download button click
                    // You can use the imageUrl to download the prescription
                    openImageInBrowser(context, imageUrl)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Download",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        ShimmerImage(modifier = Modifier
            .weight(1f)
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp)),
            imgUrl = imageUrl)
    }
}

fun openImageInBrowser(context: Context, imageUrl: String) {
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(imageUrl)
        }
        context.startActivity(browserIntent)
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle the error (e.g., notify the user if no browser is installed)
    }
}