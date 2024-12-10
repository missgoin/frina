package frena.id.manager.ui.map

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.DialogInterface
import android.content.ServiceConnection

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Binder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.graphics.Color
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import frena.id.data.model.FavoriteLocation
import frena.id.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

//import frena.id.service.HEREBackgroundPositioningService
//import frena.id.service.NotificationUtils


class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository = PreferencesRepository(application)

    val isPlaying = mutableStateOf(false)
    val lastClickedLocation = mutableStateOf<GeoPoint?>(null)
    val userLocation = mutableStateOf<GeoPoint?>(null)
    val isLoading = mutableStateOf(true)
    val mapZoom = mutableStateOf<Double?>(null)
    val showGoToPointDialog = mutableStateOf(false)
    val showAddToFavoritesDialog = mutableStateOf(false)

    private val _goToPointEvent = MutableSharedFlow<GeoPoint>()
    val goToPointEvent: SharedFlow<GeoPoint> = _goToPointEvent.asSharedFlow()

    private val _centerMapEvent = MutableSharedFlow<Unit>()
    val centerMapEvent: SharedFlow<Unit> = _centerMapEvent.asSharedFlow()

    data class InputFieldState(var value: String = "", var errorMessage: String? = null)

    // GoToPointDialog state
    val goToPointState = mutableStateOf(InputFieldState() to InputFieldState())

    // AddToFavoritesDialog state
    val addToFavoritesState = mutableStateOf(FavoritesInputState())

    // FAB clickability
    val isFabClickable: Boolean
        get() = lastClickedLocation.value != null

    // Toggle the play/stop status
    fun togglePlaying() {        
        isPlaying.value = !isPlaying.value
        if (!isPlaying.value) {
            updateClickedLocation(null)

        }
        preferencesRepository.saveIsPlaying(isPlaying.value)
    }

    // Updates the user's current location
    fun updateUserLocation(location: GeoPoint) {
        userLocation.value = location
    }

    // Update the last clicked location
    fun updateClickedLocation(geoPoint: GeoPoint?) {
        lastClickedLocation.value = geoPoint
        geoPoint?.let {
            preferencesRepository.saveLastClickedLocation(
                it.latitude,
                it.longitude
            )
        } ?: preferencesRepository.clearLastClickedLocation()
    }

    fun addFavoriteLocation(favoriteLocation: FavoriteLocation) {
        preferencesRepository.addFavorite(favoriteLocation)
    }

    // Update specific fields in the FavoritesInputState
    fun updateAddToFavoritesField(fieldName: String, newValue: String) {
        val currentState = addToFavoritesState.value
        val errorMessage = when (fieldName) {
            "name" -> if (newValue.isBlank()) "Please provide a name" else null
            "latitude" -> validateInput(newValue, -90.0..90.0, "Latitude must be between -90 and 90")
            "longitude" -> validateInput(newValue, -180.0..180.0, "Longitude must be between -180 and 180")
            else -> null
        }

        addToFavoritesState.value = when (fieldName) {
            "name" -> currentState.copy(name = currentState.name.copy(value = newValue, errorMessage = errorMessage))
            "latitude" -> currentState.copy(latitude = currentState.latitude.copy(value = newValue, errorMessage = errorMessage))
            "longitude" -> currentState.copy(longitude = currentState.longitude.copy(value = newValue, errorMessage = errorMessage))
            else -> currentState
        }
    }

    // Go to point logic
    fun goToPoint(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _goToPointEvent.emit(GeoPoint(latitude, longitude))
        }
    }

    // Update specific fields in the GoToPointDialog state
    fun updateGoToPointField(fieldName: String, newValue: String) {
        val (latitudeField, longitudeField) = goToPointState.value
        goToPointState.value = when (fieldName) {
            "latitude" -> latitudeField.copy(value = newValue) to longitudeField
            "longitude" -> latitudeField to longitudeField.copy(value = newValue)
            else -> latitudeField to longitudeField
        }
    }

    // Center map
    fun triggerCenterMapEvent() {
        viewModelScope.launch {
            _centerMapEvent.emit(Unit)
        }
    }

    fun setLoadingStarted() {
        isLoading.value = true
    }

    // Set loading finished
    fun setLoadingFinished() {
        isLoading.value = false
        isPlaying.value = preferencesRepository.getIsPlaying()
        lastClickedLocation.value = preferencesRepository.getLastClickedLocation()?.let {
            GeoPoint(it.latitude, it.longitude)
        }
    }

    // Dialog show/hide logic
    fun showGoToPointDialog() { showGoToPointDialog.value = true }
    fun hideGoToPointDialog() {
        showGoToPointDialog.value = false
        clearGoToPointInputs()
    }

    fun showAddToFavoritesDialog() { showAddToFavoritesDialog.value = true }
    fun hideAddToFavoritesDialog() {
        showAddToFavoritesDialog.value = false
        clearAddToFavoritesInputs()
    }

    // Helper for input validation
    private fun validateInput(
        input: String, range: ClosedRange<Double>, errorMessage: String
    ): String? {
        val value = input.toDoubleOrNull()
        return if (value == null || value !in range) errorMessage else null
    }

    // Validate GoToPoint inputs
    fun validateAndGo(onSuccess: (latitude: Double, longitude: Double) -> Unit) {
        val (latField, lonField) = goToPointState.value
        val latitudeError = validateInput(latField.value, -90.0..90.0, "Latitude must be between -90 and 90")
        val longitudeError = validateInput(lonField.value, -180.0..180.0, "Longitude must be between -180 and 180")

        goToPointState.value = latField.copy(errorMessage = latitudeError) to lonField.copy(errorMessage = longitudeError)

        if (latitudeError == null && longitudeError == null) {
            onSuccess(latField.value.toDouble(), lonField.value.toDouble())
        }
    }

    // Clear GoToPoint inputs
    fun clearGoToPointInputs() {
        goToPointState.value = InputFieldState() to InputFieldState()
    }

    // Prefill AddToFavorites latitude/longitude with marker values (if available)
    fun prefillCoordinatesFromMarker(latitude: Double?, longitude: Double?) {
        val currentState = addToFavoritesState.value
        addToFavoritesState.value = currentState.copy(
            latitude = currentState.latitude.copy(value = latitude?.toString() ?: ""),
            longitude = currentState.longitude.copy(value = longitude?.toString() ?: "")
        )
    }

    // Validate and add favorite location
    fun validateAndAddFavorite(onSuccess: (name: String, latitude: Double, longitude: Double) -> Unit) {
        val currentState = addToFavoritesState.value

        val latitudeError = validateInput(currentState.latitude.value, -90.0..90.0, "Latitude must be between -90 and 90")
        val longitudeError = validateInput(currentState.longitude.value, -180.0..180.0, "Longitude must be between -180 and 180")
        val nameError = if (currentState.name.value.isBlank()) "Please provide a name" else null

        addToFavoritesState.value = currentState.copy(
            name = currentState.name.copy(errorMessage = nameError),
            latitude = currentState.latitude.copy(errorMessage = latitudeError),
            longitude = currentState.longitude.copy(errorMessage = longitudeError)
        )

        if (nameError == null && latitudeError == null && longitudeError == null) {
            onSuccess(currentState.name.value, currentState.latitude.value.toDouble(), currentState.longitude.value.toDouble())
        }
    }

    // Clear AddToFavorites inputs
    fun clearAddToFavoritesInputs() {
        addToFavoritesState.value = FavoritesInputState() // Reset to the default state with empty fields
    }


    
    
    
    
    
    
    
}

data class FavoritesInputState(
    var name: MapViewModel.InputFieldState = MapViewModel.InputFieldState(),
    var latitude: MapViewModel.InputFieldState = MapViewModel.InputFieldState(),
    var longitude: MapViewModel.InputFieldState = MapViewModel.InputFieldState()
)
