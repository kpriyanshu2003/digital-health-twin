package nxt.abhranil.dhtapp.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nxt.abhranil.dhtapp.data.model.CommonResponse
import nxt.abhranil.dhtapp.data.model.CreateUser
import nxt.abhranil.dhtapp.data.model.DiseaseCreate
import nxt.abhranil.dhtapp.data.model.GetDiseaseByIdResponse
import nxt.abhranil.dhtapp.data.model.GetUserDiseaseResponse
import nxt.abhranil.dhtapp.data.repo.DHTRepository
import nxt.abhranil.dhtapp.data.utils.UiState
import javax.inject.Inject

@HiltViewModel
class DHTViewModel @Inject constructor(private val repo: DHTRepository): ViewModel() {

    private val _userResponse: MutableStateFlow<UiState<CommonResponse>> = MutableStateFlow(UiState.Idle)
    val userResponse = _userResponse.asStateFlow()

    fun createUser(token: String, user: CreateUser) {
        createUserDetails(token, user)
    }

    private fun createUserDetails(token: String, user: CreateUser) {
        _userResponse.value = UiState.Loading

        viewModelScope.launch {
            try {
                _userResponse.value = repo.createUser(token, user)
            }
            catch (e: Exception) {
                Log.d("DHTViewModel", "createUserDetails: ${e.message}")
            }
        }
    }

    private val _diseaseResponse: MutableStateFlow<UiState<CommonResponse>> = MutableStateFlow(UiState.Idle)
    val diseaseResponse = _diseaseResponse.asStateFlow()

    fun createDisease(token: String, disease: DiseaseCreate) {
        createDiseaseDetails(token, disease)
    }

    private fun createDiseaseDetails(token: String, disease: DiseaseCreate) {
        _diseaseResponse.value = UiState.Loading

        viewModelScope.launch {
            try {
                _diseaseResponse.value = repo.createDisease(token, disease)
            }
            catch (e: Exception) {
                Log.d("DHTViewModel", "createDiseaseDetails: ${e.message}")
            }
        }
    }

    private val _getUserDiseaseResponse: MutableStateFlow<UiState<GetUserDiseaseResponse>> = MutableStateFlow(
        UiState.Idle)
    val getUserDiseaseResponse = _getUserDiseaseResponse.asStateFlow()

    fun getUserDiseases(token: String) {
        getUserDiseaseDetails(token)
    }

    private fun getUserDiseaseDetails(token: String) {
        _getUserDiseaseResponse.value = UiState.Loading

        viewModelScope.launch {
            try {
                _getUserDiseaseResponse.value = repo.getUserDiseases(token)
            } catch (e: Exception) {
                Log.d("DHTViewModel", "getUserDiseaseDetails: ${e.message}")
            }
        }
    }

    private val _getDiseaseByIdResponse: MutableStateFlow<UiState<GetDiseaseByIdResponse>> = MutableStateFlow(
        UiState.Idle)
    val getDiseaseByIdResponse = _getDiseaseByIdResponse.asStateFlow()

    fun getDiseaseById(token: String, diseaseID: String) {
        getDiseaseByIdDetails(token, diseaseID)
    }

    private fun getDiseaseByIdDetails(token: String, diseaseID: String) {
        _getDiseaseByIdResponse.value = UiState.Loading

        viewModelScope.launch {
            try {
                _getDiseaseByIdResponse.value = repo.getDiseaseById(token, diseaseID)
            } catch (e: Exception) {
                Log.d("DHTViewModel", "getDiseaseByIdDetails: ${e.message}")
            }
        }
    }
}