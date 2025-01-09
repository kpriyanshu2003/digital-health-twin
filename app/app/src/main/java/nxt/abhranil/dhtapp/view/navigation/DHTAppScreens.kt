package nxt.abhranil.dhtapp.view.navigation

sealed class DHTAppScreens(val route: String) {
    object SigninScreen : DHTAppScreens("login_screen")
    object SignupScreen : DHTAppScreens("signup_screen")
    object PatientDashboardScreen : DHTAppScreens("patient_dashboard_screen")
    object BodyMetricsScreen : DHTAppScreens("body_metrics_screen")
    object AppointmentUploadScreen : DHTAppScreens("appointment_upload_screen")
    object DiseaseDetailScreen : DHTAppScreens("disease_detail_screen")
}