package nxt.abhranil.dhtapp.data.model

data class Appointment(
    val Doctor: Doctor,
    val appointmentDate: String,
    val category: Any,
    val id: String,
    val name: String,
    val notes: Any
)