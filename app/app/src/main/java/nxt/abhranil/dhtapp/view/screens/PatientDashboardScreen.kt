import android.R.attr.fontWeight
import android.R.attr.text
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.key.Key.Companion.D
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import nxt.abhranil.dhtapp.R
import nxt.abhranil.dhtapp.data.model.Result
import nxt.abhranil.dhtapp.data.utils.UiState
import nxt.abhranil.dhtapp.view.components.InfoCard
import nxt.abhranil.dhtapp.view.components.MetricCard
import nxt.abhranil.dhtapp.view.navigation.DHTAppScreens
import nxt.abhranil.dhtapp.vm.DHTViewModel
import java.util.Calendar

@Composable
fun PatientDashboardScreen(navController: NavController,
                           viewModel: DHTViewModel = hiltViewModel()) {

    var token by remember { mutableStateOf<String?>(null) }
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
    val verticalScrollSate = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp)
            .verticalScroll(verticalScrollSate)
    ) {
        val data = viewModel.getUserDiseaseResponse.collectAsState().value
        fun getFirebaseAuthToken() {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                currentUser.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val idToken = task.result?.token
                            Log.d("DHTApp", "Token: $idToken")
                            token = task.result?.token
                            viewModel.getUserDiseases(token = "Bearer $idToken")
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
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    val greeting = when (currentHour) {
                        in 0..11 -> "Good morning"
                        in 12..17 -> "Good afternoon"
                        else -> "Good evening"
                    }
                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 40.dp),
                        text = greeting,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "${data.data.data.user.age} years old, ${data.data.data.user.gender}",
                        fontSize = 16.sp,
                        color = Color(0xFF6C63FF)
                    )
                }


                val scrollableState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollableState),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(modifier = Modifier
                        .size(80.dp), onClick = {
                        navController.navigate(DHTAppScreens.AppointmentUploadScreen.route)
                    }, shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B6CB7),
                            contentColor = Color.White
                        )) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                    data.data.data.condition.forEach {
                        InfoCard(modifier = Modifier
                            .padding(4.dp)
                            .clickable{
                                navController.navigate(DHTAppScreens.DiseaseDetailScreen.route + "/${it.id}")
                            }, disease = it.name, medication = it.medication.toString())
                    }
                }

                // Health Metrics Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    // Metrics Grid
                    HealthMetricsGrid(
                        data.data.data.result
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        RiskIndicator()
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AlertCard(message = "! Edema spike - Reduce sodium intake, consider a diuretic dose adjustment.")
                            Spacer(modifier = Modifier.height(16.dp))

                            // Buttons
                            ActionButtons(navController)
                        }
                    }

                }


            }

            is UiState.Error -> TODO()
        }
        }
        // Greeting and Patient Info

    }

    @Composable
    fun HealthMetricsGrid(listOfMetrics: List<Result>) {

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(220.dp)
                    .width(130.dp) // Set the desired size for the Box
            ) {
                Image(
                    painter = painterResource(id = R.drawable.human_body_ic),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(), // Ensure the image fills the Box completely
                    contentScale = ContentScale.FillBounds // Scale the image as needed
                )
            }

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                for (i in listOfMetrics.chunked(3)) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        i.forEach { metric ->
                            MetricCard(
                                metricName = metric.name,
                                quantity = metric.value,
                                unit = metric.unit.toString()
                            )
                        }
                    }
                }
            }
        }


    }


@Composable
fun RiskIndicator() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFF5F6FA), shape = CircleShape)
                .border(width = 4.dp, color = Color(0xFF1E2F98), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "5.26 %",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Text(
            modifier = Modifier.width(150.dp),
            text = "Risk of Deterioration in Next 30 Days",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

//    @Composable
//    fun RiskIndicator(
//        viewModel: DHTViewModel = hiltViewModel()
//    ) {
//        var errorMessage by remember { mutableStateOf<String?>(null) }
//        val data = viewModel.getPersonalisedTipsResponse.collectAsState().value
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(100.dp)
//                    .background(Color(0xFFF5F6FA), shape = CircleShape)
//                    .border(width = 4.dp, color = Color(0xFF1E2F98), shape = CircleShape),
//                contentAlignment = Alignment.Center
//            ) {
//                when (data) {
//                    is UiState.Idle -> {
//                        val currentUser = FirebaseAuth.getInstance().currentUser
//                        if (currentUser != null) {
//                            currentUser.getIdToken(true)
//                                .addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        val idToken = task.result?.token
//                                        Log.d("DHTApp", "Token: $idToken")
//                                        viewModel.getPersonalisedTips(token = "Bearer $idToken")
//                                    } else {
//                                        errorMessage = task.exception?.localizedMessage
//                                    }
//                                }
//                        } else {
//                            errorMessage = "User is not logged in."
//                        }
//                    }
//                    is UiState.Loading -> {
//                        CircularProgressIndicator()
//                    }
//
//                    is UiState.Success -> {
//                        Text(
//                            text = data.data.data.riskFactor.toString(),
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Black
//                        )
//                    }
//                    else -> {}
//                }
//
//            }
//            Text(
//                modifier = Modifier.width(150.dp),
//                text = "Risk of Deterioration in Next 30 Days",
//                fontSize = 12.sp,
//                color = Color.Gray,
//                textAlign = TextAlign.Center
//            )
//        }
//    }

    @Composable
    fun AlertCard(message: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF5F5), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = message,
                fontSize = 12.sp,
                color = Color.Red
            )
        }
    }

    @Composable
    fun ActionButtons(navController: NavController) {
        Column {
            Button(
                onClick = { navController.navigate(DHTAppScreens.MedicalHistoryScreen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "View Medical History", color = Color.White)
            }

            Button(
                onClick = { navController.navigate(DHTAppScreens.GetPersonalisedTipsScreen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Personalized Tips", color = Color.White)
            }

            Button(
                onClick = { navController.navigate(DHTAppScreens.PreviousDoctorsScreen.route)  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6CB7)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Previous consulted Doctors", color = Color.White)
            }
        }
    }