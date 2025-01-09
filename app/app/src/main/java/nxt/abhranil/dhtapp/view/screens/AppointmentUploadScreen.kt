package nxt.abhranil.dhtapp.view.screens

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nxt.abhranil.dhtapp.R
import nxt.abhranil.dhtapp.data.model.AppointmentUpload
import nxt.abhranil.dhtapp.view.components.AppointmentUploadCard
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun AppointmentUploadScreen() {
    val verticalScrollSate = rememberScrollState()
    val appointments = remember { mutableStateListOf<AppointmentUpload>() }

    Box(modifier = Modifier.fillMaxSize()
        .verticalScroll(verticalScrollSate)) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RectangleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg2),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Inside
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(460.dp))

            Text(
                text = "Enter Disease Appointments",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5F6ECF),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display all appointment components
            appointments.forEachIndexed { index, appointment ->
                AppointmentUploadCard(
                    appointment = appointment,
                    onImageSelected = { uri ->
                        // Update the selected image URI for the appointment
                        appointments[index] = appointment.copy(imageUri = uri)
                    },
                    onDelete = {
                        // Remove the appointment from the list
                        appointments.removeAt(index)
                    },
                    onNameChange = { newName ->
                        appointments[index] = appointment.copy(name = newName)
                    },
                    onDateChange = { newDate ->
                        appointments[index] = appointment.copy(date = newDate)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Row with Add and Save buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // Add New Appointment Button
                Button(
                    onClick = {
                        appointments.add(AppointmentUpload())
                    },
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .background(Brush.linearGradient(
                            colors = listOf(Color(0xFF707FDD), Color(0xFF1E2F98))
                        ))
                        .padding(start = 16.dp, end = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Add New Appointment",
                        color = Color.White
                    )
                }

                // Save Appointments Button
                Button(
                    onClick = {
                        // Print the list of appointments
                        println("Appointments List:")
                        appointments.forEachIndexed { index, appointment ->
                            println("Appointment ${index + 1}:")
                            println("  Name: ${appointment.name}")
                            println("  Date: ${appointment.date}")
                            println("  Image URI: ${appointment.imageUri}")
                        }
                    },
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .background(Brush.linearGradient(
                            colors = listOf(Color(0xFF707FDD), Color(0xFF1E2F98))
                        ))
                        .padding(start = 16.dp, end = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Save",
                        color = Color.White
                    )
                }
            }
        }
    }


}


fun getMultipartFromUri(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, getFileNameFromUri(contentResolver, uri))

    // Copy the input stream to the file
    inputStream.use { input ->
        file.outputStream().use { output ->
            input?.copyTo(output)
        }
    }

    // Create RequestBody
    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

    // Create MultipartBody.Part
    return MultipartBody.Part.createFormData(partName, file.name, requestBody)
}

fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String {
    var fileName = "file"
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex != -1 && it.moveToFirst()) {
            fileName = it.getString(nameIndex)
        }
    }
    return fileName
}
