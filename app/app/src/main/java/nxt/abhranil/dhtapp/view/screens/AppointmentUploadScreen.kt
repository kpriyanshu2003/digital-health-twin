package nxt.abhranil.dhtapp.view.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import nxt.abhranil.dhtapp.R
import nxt.abhranil.dhtapp.data.model.AppointmentUpload
import nxt.abhranil.dhtapp.data.model.DiseaseCreate
import nxt.abhranil.dhtapp.data.utils.UiState
import nxt.abhranil.dhtapp.view.components.AppointmentUploadCard
import nxt.abhranil.dhtapp.view.navigation.DHTAppScreens
import nxt.abhranil.dhtapp.vm.DHTViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentUploadScreen(navController: NavController,
                            viewModel: DHTViewModel = hiltViewModel()) {

    var navigated by remember { mutableStateOf(false) }

    val data =viewModel.diseaseResponse.collectAsState().value

    LaunchedEffect(data) {
        if (data is UiState.Success && !navigated) {
            navigated = true // Mark navigation as done
            navController.navigate(DHTAppScreens.PatientDashboardScreen.route)
        }
    }


    val verticalScrollSate = rememberScrollState()
    val appointments = remember { mutableStateListOf<AppointmentUpload>() }

    var token by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var diseaseName by remember { mutableStateOf("") }

    fun getFirebaseAuthToken() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            currentUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        Log.d("DHTApp", "Token: $idToken")
                        val formattedString = appointments.joinToString(" ") { "${it.name} ${it.date}" }
                        Log.d("DHTApp", "Formatted String: $formattedString")
                        viewModel.createDisease(
                            token = "Bearer $idToken",
                            disease = DiseaseCreate(
                                name = diseaseName,
                                appointment = formattedString,
                                file = appointments.mapNotNull { appointment ->
                                    getMultipartFromUri(
                                        context = navController.context,
                                        uri = appointment.imageUri ?: return@mapNotNull null,
                                        partName = "file"
                                    )
                                }
                            ))
                        token = task.result?.token
                    } else {
                        errorMessage = task.exception?.localizedMessage
                    }
                }
        } else {
            errorMessage = "User is not logged in."
        }
    }

    Box(modifier = Modifier.fillMaxSize()
        .verticalScroll(verticalScrollSate)) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RectangleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg2_new),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Inside
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(300.dp))

            Text(
                text = "Enter Disease Appointments",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5F6ECF),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue, // Color when focused
                    unfocusedBorderColor = Color.Gray, // Color when not focused
                    disabledBorderColor = Color.LightGray, // Color when disabled
                    errorBorderColor = Color.Red,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray// Color when there's an error
                ),
                value = diseaseName,
                onValueChange = { diseaseName = it },
                label = { Text("Enter Disease Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true
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
                        getFirebaseAuthToken()
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

                when(data) {
                    is UiState.Loading -> {
                        Log.d("DHTApp", "Loading")
                    }
                    else -> {
                    }
                }
            }
        }
    }


}

fun getMultipartFromUri(
    context: Context,
    uri: Uri,
    partName: String
): MultipartBody.Part {
    val contentResolver = context.contentResolver

    // Get the original filename from the Uri
    val cursor = contentResolver.query(uri, null, null, null, null)
    val originalFilename = if (cursor != null && cursor.moveToFirst()) {
        cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
    } else {
        "unknown_file"
    }
    cursor?.close()

    // Open the file descriptor
    val fileDescriptor = contentResolver.openFileDescriptor(uri, "r") ?: return MultipartBody.Part.createFormData(partName, "")

    val file = File(context.cacheDir, originalFilename)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val outputStream = FileOutputStream(file)
    inputStream.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    // Create a RequestBody for the file
    val requestBody = file.asRequestBody(contentResolver.getType(uri)?.toMediaTypeOrNull())

    // Create the MultipartBody.Part with the original filename
    return MultipartBody.Part.createFormData(partName, originalFilename, requestBody)
}
