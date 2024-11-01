//SettingsViewModel.kt
package com.noobexon.xposedfakelocation.manager.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.noobexon.xposedfakelocation.data.*
import com.noobexon.xposedfakelocation.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesRepository = PreferencesRepository(application)

    private val _useAccuracy = MutableStateFlow(DEFAULT_USE_ACCURACY)
    val useAccuracy: StateFlow<Boolean> get() = _useAccuracy

    private val _accuracy = MutableStateFlow(DEFAULT_ACCURACY)
    val accuracy: StateFlow<Double> get() = _accuracy

    private val _useAltitude = MutableStateFlow(DEFAULT_USE_ALTITUDE)
    val useAltitude: StateFlow<Boolean> get() = _useAltitude

    private val _altitude = MutableStateFlow(DEFAULT_ALTITUDE)
    val altitude: StateFlow<Double> get() = _altitude

    private val _useRandomize = MutableStateFlow(DEFAULT_USE_RANDOMIZE)
    val useRandomize: StateFlow<Boolean> get() = _useRandomize

    private val _randomizeRadius = MutableStateFlow(DEFAULT_RANDOMIZE_RADIUS)
    val randomizeRadius: StateFlow<Double> get() = _randomizeRadius

    private val _useVerticalAccuracy = MutableStateFlow(DEFAULT_USE_VERTICAL_ACCURACY)
    val useVerticalAccuracy: StateFlow<Boolean> get() = _useVerticalAccuracy

    private val _verticalAccuracy = MutableStateFlow(DEFAULT_VERTICAL_ACCURACY)
    val verticalAccuracy: StateFlow<Float> get() = _verticalAccuracy

    private val _useMeanSeaLevel = MutableStateFlow(DEFAULT_USE_MEAN_SEA_LEVEL)
    val useMeanSeaLevel: StateFlow<Boolean> get() = _useMeanSeaLevel

    private val _meanSeaLevel = MutableStateFlow(DEFAULT_MEAN_SEA_LEVEL)
    val meanSeaLevel: StateFlow<Double> get() = _meanSeaLevel

    private val _useMeanSeaLevelAccuracy = MutableStateFlow(DEFAULT_USE_MEAN_SEA_LEVEL_ACCURACY)
    val useMeanSeaLevelAccuracy: StateFlow<Boolean> get() = _useMeanSeaLevelAccuracy

    private val _meanSeaLevelAccuracy = MutableStateFlow(DEFAULT_MEAN_SEA_LEVEL_ACCURACY)
    val meanSeaLevelAccuracy: StateFlow<Float> get() = _meanSeaLevelAccuracy

    private val _useSpeed = MutableStateFlow(DEFAULT_USE_SPEED)
    val useSpeed: StateFlow<Boolean> get() = _useSpeed

    private val _speed = MutableStateFlow(DEFAULT_SPEED)
    val speed: StateFlow<Float> get() = _speed

    private val _useSpeedAccuracy = MutableStateFlow(DEFAULT_USE_SPEED_ACCURACY)
    val useSpeedAccuracy: StateFlow<Boolean> get() = _useSpeedAccuracy

    private val _speedAccuracy = MutableStateFlow(DEFAULT_SPEED_ACCURACY)
    val speedAccuracy: StateFlow<Float> get() = _speedAccuracy

    init {
        viewModelScope.launch {
            _useAccuracy.value = preferencesRepository.getUseAccuracy()
            _accuracy.value = preferencesRepository.getAccuracy()
            _useAltitude.value = preferencesRepository.getUseAltitude()
            _altitude.value = preferencesRepository.getAltitude()
            _useRandomize.value = preferencesRepository.getUseRandomize()
            _randomizeRadius.value = preferencesRepository.getRandomizeRadius()
            _useVerticalAccuracy.value = preferencesRepository.getUseVerticalAccuracy()
            _verticalAccuracy.value = preferencesRepository.getVerticalAccuracy()
            _useMeanSeaLevel.value = preferencesRepository.getUseMeanSeaLevel()
            _meanSeaLevel.value = preferencesRepository.getMeanSeaLevel()
            _useMeanSeaLevelAccuracy.value = preferencesRepository.getUseMeanSeaLevelAccuracy()
            _meanSeaLevelAccuracy.value = preferencesRepository.getMeanSeaLevelAccuracy()
            _useSpeed.value = preferencesRepository.getUseSpeed()
            _speed.value = preferencesRepository.getSpeed()
            _useSpeedAccuracy.value = preferencesRepository.getUseSpeedAccuracy()
            _speedAccuracy.value = preferencesRepository.getSpeedAccuracy()
        }
    }

    fun setUseAccuracy(value: Boolean) {
        _useAccuracy.value = value
        preferencesRepository.saveUseAccuracy(value)
    }

    fun setAccuracy(value: Double) {
        _accuracy.value = value
        preferencesRepository.saveAccuracy(value)
    }

    fun setUseAltitude(value: Boolean) {
        _useAltitude.value = value
        preferencesRepository.saveUseAltitude(value)
    }

    fun setAltitude(value: Double) {
        _altitude.value = value
        preferencesRepository.saveAltitude(value)
    }

    fun setUseRandomize(value: Boolean) {
        _useRandomize.value = value
        preferencesRepository.saveUseRandomize(value)
    }

    fun setRandomizeRadius(value: Double) {
        _randomizeRadius.value = value
        preferencesRepository.saveRandomizeRadius(value)
    }

    fun setUseVerticalAccuracy(value: Boolean) {
        _useVerticalAccuracy.value = value
        preferencesRepository.saveUseVerticalAccuracy(value)
    }

    fun setVerticalAccuracy(value: Float) {
        _verticalAccuracy.value = value
        preferencesRepository.saveVerticalAccuracy(value)
    }

    fun setUseMeanSeaLevel(value: Boolean) {
        _useMeanSeaLevel.value = value
        preferencesRepository.saveUseMeanSeaLevel(value)
    }

    fun setMeanSeaLevel(value: Double) {
        _meanSeaLevel.value = value
        preferencesRepository.saveMeanSeaLevel(value)
    }

    fun setUseMeanSeaLevelAccuracy(value: Boolean) {
        _useMeanSeaLevelAccuracy.value = value
        preferencesRepository.saveUseMeanSeaLevelAccuracy(value)
    }

    fun setMeanSeaLevelAccuracy(value: Float) {
        _meanSeaLevelAccuracy.value = value
        preferencesRepository.saveMeanSeaLevelAccuracy(value)
    }

    fun setUseSpeed(value: Boolean) {
        _useSpeed.value = value
        preferencesRepository.saveUseSpeed(value)
    }

    fun setSpeed(value: Float) {
        _speed.value = value
        preferencesRepository.saveSpeed(value)
    }

    fun setUseSpeedAccuracy(value: Boolean) {
        _useSpeedAccuracy.value = value
        preferencesRepository.saveUseSpeedAccuracy(value)
    }

    fun setSpeedAccuracy(value: Float) {
        _speedAccuracy.value = value
        preferencesRepository.saveSpeedAccuracy(value)
    }


}