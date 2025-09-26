package nxt.abhranil.dhtapp.data.model

data class Recommendations(
    val lifestyle_modifications: List<String>,
    val medication_management: List<String>,
    val monitor_vital_signs: List<String>
)