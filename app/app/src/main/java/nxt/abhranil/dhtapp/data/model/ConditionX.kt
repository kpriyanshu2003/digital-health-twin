package nxt.abhranil.dhtapp.data.model

data class ConditionX(
    val Appointments: List<AppointmentXXX>,
    val createdAt: String,
    val id: String,
    val imageUrl: Any,
    val medication: List<String>,
    val name: String,
    val notes: Any,
    val symptoms: List<String>,
    val userId: String
)