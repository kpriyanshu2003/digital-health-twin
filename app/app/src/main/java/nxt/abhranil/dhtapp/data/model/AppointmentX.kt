package nxt.abhranil.dhtapp.data.model

data class AppointmentX(
    val appointmentDate: String,
    val category: Any,
    val conditionId: String,
    val createdAt: String,
    val doctorId: String,
    val id: String,
    val imageUrl: Any,
    val isDigital: Boolean,
    val name: String,
    val notes: Any,
    val userId: String,
    val LabResult: List<Result>
)