package nxt.abhranil.dhtapp.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import nxt.abhranil.dhtapp.R
import nxt.abhranil.dhtapp.data.utils.UiState
import nxt.abhranil.dhtapp.view.components.AppointmentCard
import nxt.abhranil.dhtapp.view.components.InfoCard
import nxt.abhranil.dhtapp.view.navigation.DHTAppScreens
import nxt.abhranil.dhtapp.vm.DHTViewModel
import kotlin.collections.forEach

@Composable
fun DiseaseDetailScreen(navController: NavController,
                        diseaseId: String,
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

    val data = viewModel.getDiseaseByIdResponse.collectAsState().value
    fun getFirebaseAuthToken() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            currentUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        Log.d("DHTApp", "Token: $idToken")
                        viewModel.getDiseaseById(token = "Bearer $idToken",
                            diseaseID = diseaseId)
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
                    text = data.data.data.condition.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))

                val scrollableState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollableState)
                ) {
                    data.data.data.response.forEach {
                        Box(
                            modifier = Modifier
                                .width(330.dp)
                                .padding(end = 8.dp, bottom = 16.dp)
                                .height(110.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x307987DF))
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
                                        text = "Recommended Doctor ✬",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontSize = 15.sp,
                                        lineHeight = 4.sp
                                    )
                                    Text(
                                        text = it.name,
                                        color = Color.Black,
                                        fontSize = 15.sp,
                                        lineHeight = 4.sp
                                    )

                                    // Section 2: Current Medication
                                    Text(
                                        text = it.designation,
                                        color = Color.Black,
                                        fontSize = 15.sp,
                                        lineHeight = 4.sp
                                    )
                                    Text(
                                        text = it.phNo,
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
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                            append("Symptoms: ")
                        }
                        val symptoms = data.data.data.condition.symptoms.joinToString(", ")
                        append(symptoms)
                    },
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                            append("Medication: ")
                        }
                        val medication = data.data.data.condition.medication.joinToString(", ")
                        append(medication)
                    },
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(data.data.data.condition.Appointments.size) {
                        AppointmentCard(
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("appointment_detail_screen/${data.data.data.condition.Appointments[it].id}")
                                },
                            name = data.data.data.condition.Appointments[it].name,
                            date = data.data.data.condition.Appointments[it].appointmentDate,
                            doctor = data.data.data.condition.Appointments[it].Doctor.name
                        )
                    }
                }

            }
        } else -> {}
    }

}