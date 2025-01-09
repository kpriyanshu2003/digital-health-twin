package nxt.abhranil.dhtapp.data.model

data class Condition(
    val createdAt: String,
    val id: String,
    val imageUrl: Any,
    val medication: List<String>,
    val name: String,
    val notes: Any,
    val symptoms: List<String>,
    val userId: String
)