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

       
    private val _useGojekBypassReg = MutableStateFlow(DEFAULT_USE_GOJEK_BYPASS_REG)
    val useGojekBypassReg: StateFlow<Boolean> get() = _useGojekBypassReg

    private val _gojekBypassReg = MutableStateFlow(DEFAULT_ACCURACY)
    val gojekBypassReg: StateFlow<Double> get() = _gojekBypassReg



    init {
        viewModelScope.launch {
            _useGojekBypassReg.value = preferencesRepository.getUseGojekBypassReg()
            _gojekBypassReg.value = preferencesRepository.getGojekBypassReg()
            
        }
    }


    fun setUseGojekBypassReg(value: Boolean) {
        _useGojekBypassReg.value = value
        preferencesRepository.saveUseGojekBypassReg(value)
    }
    
    fun setGojekBypassReg(value: Boolean) {
        _gojekBypassReg.value = value
        preferencesRepository.saveGojekBypassReg(value)
    }    



}