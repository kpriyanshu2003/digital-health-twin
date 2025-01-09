package nxt.abhranil.dhtapp.view.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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

@Composable
fun SignupScreen(navController: NavController,
                 viewModel: LoginViewModel = viewModel()) {

    val context = LocalContext.current


    var email by rememberSaveable() { mutableStateOf("") }
    var password by rememberSaveable() { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

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

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(400.dp))

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Create Your Digital Medical Twin!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5F6ECF),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input fields
            OutlinedTextField(
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
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Google Signup Button
            Button(
                onClick = { /* Handle Google Signup */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F2F2)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.google_ic), // Replace with actual resource
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Signup with Google",
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Terms and Conditions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(checkmarkColor = Color(0xFF4A00E0))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    lineHeight = 16.sp,
                    text = "By creating an account you agree to the terms of use and our privacy policy",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Signup Button
            Button(
                onClick = {
                    viewModel.createUserWithEmailPass(email, password, error = {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }) {
                        Toast.makeText(context, "Sign up Successful!", Toast.LENGTH_LONG).show()
                        navController.navigate(DHTAppScreens.BodyMetricsScreen.route) {
                            popUpTo(DHTAppScreens.SignupScreen.route){
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
                    text = "SignUp",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sign In Navigation
            TextButton(onClick = {
                navController.navigate(DHTAppScreens.SigninScreen.route) {
                    popUpTo(DHTAppScreens.SignupScreen.route) {
                        inclusive = true
                        saveState = true
                    }
                }
            }) {
                Text(
                    text = "Already have an account? Sign In",
                    color = Color(0xFF4A00E0)
                )
            }
        }
    }
}
