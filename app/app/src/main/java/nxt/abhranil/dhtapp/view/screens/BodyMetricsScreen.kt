package nxt.abhranil.dhtapp.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import nxt.abhranil.dhtapp.R
import nxt.abhranil.dhtapp.data.model.CreateUser
import nxt.abhranil.dhtapp.data.utils.UiState
import nxt.abhranil.dhtapp.view.navigation.DHTAppScreens
import nxt.abhranil.dhtapp.vm.DHTViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyMetricsScreen(navController: NavController,
                      viewModel: DHTViewModel = hiltViewModel()
) {

    var navigated by remember { mutableStateOf(false) }

    val data =viewModel.userResponse.collectAsState().value

    LaunchedEffect(data) {
        if (data is UiState.Success && !navigated) {
            navigated = true // Mark navigation as done
            navController.navigate(DHTAppScreens.AppointmentUploadScreen.route)
        }
    }

    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    var token by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val bmi = try {
        val heightValue = height.toFloat() / 100
        val weightValue = weight.toFloat()
        if (heightValue > 0) (weightValue / (heightValue * heightValue)).toString()
        else ""
    } catch (e: NumberFormatException) {
        ""
    }

    // Function to fetch the token
    fun getFirebaseAuthToken() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            currentUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        Log.d("DHTApp", "Token: $idToken")
                        viewModel.createUser(
                            token = "Bearer $idToken",
                            user = CreateUser(
                                age = age.toInt(),
                                height = height,
                                weight = weight,
                                bmi = bmi.toFloat(),
                                gender = gender
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




    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RectangleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg2),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(416.dp))

            Text(
                text = "Enter Body Metrics",
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
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                value = age.toString(),
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                value = gender,
                onValueChange = { gender = it },
                label = { Text("Gender") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true
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
                value = bmi,
                onValueChange = {}, // No-op: read-only field
                label = { Text("BMI") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true,
                readOnly = true // Ensure the field is read-only
            )
            Spacer(modifier = Modifier.height(16.dp))




            Button(
                onClick = {
                    getFirebaseAuthToken()
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