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

    private val _goBypassReg = MutableStateFlow(DEFAULT_GOJEK_BYPASS_REG)
    val goBypassReg: StateFlow<Boolean> get() = _goBypassReg



    init {
        viewModelScope.launch {
            _useGojekBypassReg.value = preferencesRepository.getUseGojekBypassReg()
            _goBypassReg.value = preferencesRepository.getGoBypassReg()
            
        }
    }


    fun setUseGojekBypassReg(value: Boolean) {
        _useGojekBypassReg.value = value
        preferencesRepository.saveUseGojekBypassReg(value)
    }
    
    fun setGoBypassReg(value: Boolean) {
        _goBypassReg.value = value
        preferencesRepository.saveGoBypassReg(value)
    }    



}