package nxt.abhranil.dhtapp.view.navigation

import PatientDashboardScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import nxt.abhranil.dhtapp.view.screens.AppointmentDetailsScreen
import nxt.abhranil.dhtapp.view.screens.AppointmentUploadScreen
import nxt.abhranil.dhtapp.view.screens.BodyMetricsScreen
import nxt.abhranil.dhtapp.view.screens.DiseaseDetailScreen
import nxt.abhranil.dhtapp.view.screens.GetPersonalisedTipsScreen
import nxt.abhranil.dhtapp.view.screens.MedicalHistoryScreen
import nxt.abhranil.dhtapp.view.screens.SigninScreen
import nxt.abhranil.dhtapp.view.screens.SignupScreen

@Composable
fun DHTAppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (FirebaseAuth.getInstance().currentUser == null) {
            DHTAppScreens.SignupScreen.route
        } else {
            DHTAppScreens.PatientDashboardScreen.route
        }
    ) {
        composable(DHTAppScreens.SigninScreen.route) {
            SigninScreen(navController)
        }
        composable(DHTAppScreens.SignupScreen.route) {
            SignupScreen(navController)
        }
        composable(DHTAppScreens.PatientDashboardScreen.route) {
            PatientDashboardScreen(navController)
        }
        composable(DHTAppScreens.BodyMetricsScreen.route) {
            BodyMetricsScreen(navController)
        }
        composable(DHTAppScreens.AppointmentUploadScreen.route) {
            AppointmentUploadScreen(navController)
        }

        composable(DHTAppScreens.MedicalHistoryScreen.route) {
            MedicalHistoryScreen(navController)
        }

        composable(DHTAppScreens.GetPersonalisedTipsScreen.route) {
            GetPersonalisedTipsScreen(navController)
        }

        val detailScreen = DHTAppScreens.DiseaseDetailScreen.route
        composable("$detailScreen/{diseaseID}", arguments = listOf(navArgument(name = "diseaseID") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("diseaseID").let {
                if(it != null)
                    DiseaseDetailScreen(navController, diseaseId = it)
            }
        }

        val appointmentDetailScreen = DHTAppScreens.AppointmentDetailScreen.route
        composable("$appointmentDetailScreen/{appointmentID}", arguments = listOf(navArgument(name = "appointmentID") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("appointmentID").let {
                if(it != null)
                    AppointmentDetailsScreen(navController, appointmentId = it)
            }
        }
    }
}