package nxt.abhranil.dhtapp.data.model

data class Result(
    val appointmentId: String,
    val createdAt: String,
    val id: String,
    val name: String,
    val prediction: Any,
    val referenceRange: Any,
    val unit: Any,
    val userId: String,
    val value: String
)