package nxt.abhranil.dhtapp.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import nxt.abhranil.dhtapp.R
import nxt.abhranil.dhtapp.data.utils.UiState
import nxt.abhranil.dhtapp.view.components.AppointmentCard
import nxt.abhranil.dhtapp.vm.DHTViewModel

@Composable
fun MedicalHistoryScreen(navController: NavController,
                         viewModel: DHTViewModel = hiltViewModel()) {
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

    val data = viewModel.getAllAppointmentsResponse.collectAsState().value
    fun getFirebaseAuthToken() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            currentUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        Log.d("DHTApp", "Token: $idToken")
                        viewModel.getAllAppointments(token = "Bearer $idToken")
                    } else {
                        errorMessage = task.exception?.localizedMessage
                    }
                }
        } else {
            errorMessage = "User is not logged in."
        }
    }

    when (data) {
        is UiState.Idle -> {
            getFirebaseAuthToken()
        }

        is UiState.Loading -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Loading....",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        is UiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(56.dp))
                Text(
                    text = "Medical History",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(data.data.appointments.size) {
                        AppointmentCard(
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("appointment_detail_screen/${data.data.appointments[it].id}")
                                },
                            name = data.data.appointments[it].name,
                            date = data.data.appointments[it].appointmentDate,
                            doctor = data.data.appointments[it].Doctor.name
                        )
                    }
                }

            }
        } else -> {}
    }
}