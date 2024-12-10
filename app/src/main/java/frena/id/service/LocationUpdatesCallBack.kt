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


interface LocationUpdatesCallBack {
    fun onLocationUpdate()
 //   fun locationException(message: String)
}



