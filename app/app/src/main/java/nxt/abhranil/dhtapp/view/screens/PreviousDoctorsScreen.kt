package nxt.abhranil.dhtapp.view.screens

import android.R.attr.name
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
fun PreviousDoctorsScreen(navController: NavController,
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

    val data = viewModel.getPassDoctorResponse.collectAsState().value
    fun getFirebaseAuthToken() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            currentUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        Log.d("DHTApp", "Token: $idToken")
                        viewModel.getAllPastDoctors(token = "Bearer $idToken")
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
                Spacer(modifier = Modifier.height(44.dp))
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
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = "Previously Consulted Doctors",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(32.dp))

                LazyColumn {
                    items(data.data.data.size) { index ->
                        // State for thumbs up and down
                        var isThumbsUpSelected by remember { mutableStateOf(false) }
                        var isThumbsDownSelected by remember { mutableStateOf(false) }

                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(Color(0x80F1F1F1)),
                            border = BorderStroke(1.dp, Color.Transparent),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = data.data.data[index].name,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 20.sp
                                )

                                HorizontalDivider(
                                    color = Color(0xFFB0B0B0),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(
                                        text = "Designation:",
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = data.data.data[index].designation,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(
                                        text = "Phone Number: ",
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = data.data.data[index].phNo,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Thumbs Up/Down Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    IconButton(onClick = {
                                        isThumbsUpSelected = !isThumbsUpSelected
                                        if (isThumbsUpSelected) isThumbsDownSelected = false
                                    }) {
                                        Icon(
                                            imageVector = if (isThumbsUpSelected)
                                                Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                            contentDescription = "Thumbs Up",
                                            tint = if (isThumbsUpSelected) Color.Green else Color.Gray
                                        )
                                    }
                                    IconButton(onClick = {
                                        isThumbsDownSelected = !isThumbsDownSelected
                                        if (isThumbsDownSelected) isThumbsUpSelected = false
                                    }) {
                                        Icon(modifier = Modifier.rotate(180f),
                                            imageVector = if (isThumbsDownSelected)
                                                Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                            contentDescription = "Thumbs Down",
                                            tint = if (isThumbsDownSelected) Color.Red else Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else -> {}
    }

}