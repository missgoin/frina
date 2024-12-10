package frena.id.service

//import android.location.GnssNavigationMessage
//import android.location.Location

import frena.id.manager.ui.map.*
import frena.id.data.repository.PreferencesRepository
import frena.id.manager.ui.map.MapScreen
import frena.id.manager.ui.map.MapViewModel
import frena.id.data.*
import frena.id.xposed.hooks.GojekApiHooks
import frena.id.xposed.utils.PreferencesUtil
import frena.id.xposed.utils.LocationUtil
import frena.id.xposed.utils.GojekUtil

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

interface LocationUpdatesCallBack {
    fun onLocationUpdate(geoPoint: GeoPoint)
 //   fun locationException(message: String)
}



