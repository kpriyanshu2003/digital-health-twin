package nxt.abhranil.dhtapp.view.navigation

sealed class DHTAppScreens(val route: String) {
    object SigninScreen : DHTAppScreens("login_screen")
    object SignupScreen : DHTAppScreens("signup_screen")
    object PatientDashboardScreen : DHTAppScreens("patient_dashboard_screen")
}