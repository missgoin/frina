//SettingsViewModel.kt
package frena.id.manager.ui.gojek

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import frena.id.data.*
import frena.id.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoJekViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesRepository = PreferencesRepository(application)
        

    private val _useGojekBypassReg = MutableStateFlow(DEFAULT_USE_GOJEK_BYPASS_REG)
    val useGojekBypassReg: StateFlow<Boolean> get() = _useGojekBypassReg

    init {
        viewModelScope.launch {
            _useGojekBypassReg = preferencesRepository.getGojekBypassReg()
        }
    }


    fun setuseGojekBypassReg(value: Boolean) {
        _useGojekBypassReg.value = value
        preferencesRepository.saveGojekBypassReg(value)
    }





}