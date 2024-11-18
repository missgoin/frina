//GojekViewModel.kt
package frena.id.manager.ui.gojek

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import frena.id.data.*
import frena.id.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GojekViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesRepository = PreferencesRepository(application)

       
    private val _useSpeed = MutableStateFlow(DEFAULT_USE_SPEED)
    val useSpeed: StateFlow<Boolean> get() = _useSpeed

    private val _speed = MutableStateFlow(DEFAULT_SPEED)
    val speed: StateFlow<Float> get() = _speed


    init {
        viewModelScope.launch {

            _useSpeed.value = preferencesRepository.getUseSpeed()
            _speed.value = preferencesRepository.getSpeed()

        }
    }



    fun setUseSpeed(value: Boolean) {
        _useSpeed.value = value
        preferencesRepository.saveUseSpeed(value)
    }

    fun setSpeed(value: Float) {
        _speed.value = value
        preferencesRepository.saveSpeed(value)
    }



}