package nxt.abhranil.dhtapp.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInWithEmailPass(email: String, password: String,error: (String) -> Unit, home: () -> Unit) = viewModelScope.launch {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    home()
                }
            }
            .addOnFailureListener {
                Log.d("FB", "signInWithEmailAndPass: ${it.localizedMessage}")
                it.localizedMessage?.let { it1 -> error(it1) }
            }
    }

    fun createUserWithEmailPass(email: String, password: String, error: (String) -> Unit, home: () -> Unit) {
        if(_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if( task.isSuccessful) {
                        home()
                    }
                    _loading.value = false
                }
                .addOnFailureListener {
                    Log.d("FB", "createUserWithEmailAndPass: ${it.localizedMessage}")
                    it.localizedMessage?.let { it1 -> error(it1) }
                    _loading.value = false
                }
        }
    }
}