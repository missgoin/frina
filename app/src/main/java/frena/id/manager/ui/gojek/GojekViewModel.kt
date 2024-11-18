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
    
    
    private val _useGojekBypassReg = MutableStateFlow(DEFAULT_USE_GOJEK_BYPASS_REG)
    val useGojekBypassReg: StateFlow<Boolean> get() = _useGojekBypassReg




    init {
        viewModelScope.launch {

            _useGojekBypassReg.value = preferencesRepository.getUseGojekBypassReg()

            _useSpeed.value = preferencesRepository.getUseSpeed()
            _speed.value = preferencesRepository.getSpeed()

        }
    }


    fun setUseGojekBypassReg(value: Boolean) {
        _useGojekBypassReg.value = value
        preferencesRepository.saveUseGojekBypassReg(value)
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