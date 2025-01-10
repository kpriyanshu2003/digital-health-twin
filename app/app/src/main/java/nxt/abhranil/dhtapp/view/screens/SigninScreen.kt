package nxt.abhranil.dhtapp.view.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nxt.abhranil.dhtapp.R
import nxt.abhranil.dhtapp.view.navigation.DHTAppScreens
import nxt.abhranil.dhtapp.vm.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigninScreen(navController: NavController,
                 viewModel: LoginViewModel = viewModel()) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RectangleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg1),
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
                text = "Welcome back!",
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
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
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
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))



            Spacer(modifier = Modifier.height(16.dp))

            // Signup Button
            Button(
                onClick = {
                    viewModel.signInWithEmailPass(email, password, error = {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }) {
                        Toast.makeText(context, "Sign in Successful!", Toast.LENGTH_LONG).show()
                        navController.navigate(DHTAppScreens.PatientDashboardScreen.route) {
                            popUpTo(DHTAppScreens.SigninScreen.route){
                                inclusive = true
                                saveState = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
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
                    text = "SignIn",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sign In Navigation
            TextButton(onClick = {
                navController.navigate(DHTAppScreens.SignupScreen.route) {
                    popUpTo(DHTAppScreens.SigninScreen.route) {
                        inclusive = true
                        saveState = true
                    }
                }
            }) {
                Text(
                    text = "Don't have an account? Sign Up",
                    color = Color(0xFF4A00E0)
                )
            }
        }
    }
}
