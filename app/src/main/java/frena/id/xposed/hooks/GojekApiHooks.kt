// GojekApiHooks.kt
package frena.id.xposed.hooks

import android.location.Location
import android.location.LocationRequest

import frena.id.xposed.utils.LocationUtil
import frena.id.xposed.utils.GojekUtil
import frena.id.xposed.utils.PreferencesUtil
import frena.id.manager.ui.map.MapViewModel
import frena.id.manager.ui.map.components.MapViewContainer
import frena.id.xposed.hooks.LocationApiHooks
import frena.id.xposed.hooks.SystemServicesHooks

import android.app.Application
import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

import java.lang.Exception
import java.io.File
import java.lang.reflect.Method
import java.lang.reflect.Field

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

class GojekApiHooks{
    private val tag = "[FRina API.gp]"
    var versiGopartner : Int = 4186
                   
    fun hookBypassReguler(lpparam: XC_LoadPackage.LoadPackageParam) {              
        
    //    GojekUtil.gojekVersionCode()        
        
        if (versiGopartner == 4186) {
            try {
                if (lpparam.packageName == "com.gojek.partner") {
                    XposedBridge.log("$tag: initializing bypass")
                    

                }
            } catch (e: Exception) {
                XposedBridge.log("$tag: error")
                }
        }
                
        if (versiGopartner == 4185) {
            try {
                if (lpparam.packageName == "com.gojek.partner") {
                    XposedBridge.log("$tag: initializing bypass")
                    
                    
                }
            } catch (e: Exception) {
                XposedBridge.log("$tag: error")
                }
        }
        
    }
    
    fun autokillGojek(lpparam: XC_LoadPackage.LoadPackageParam) {

        try {
            if (lpparam.packageName == "com.gojek.partner") {
            
            if (PreferencesUtil.getIsPlaying() != true) return
            XposedBridge.log("$tag: initializing autokill service")

            val gojekautokillClass = XposedHelpers.findClass("com.gojek.driver.models.booking.BookingDetailsModel", lpparam.classLoader)
            
            XposedBridge.hookAllConstructors(
                gojekautokillClass,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
 
                        val isPlaying = mutableStateOf(false)
                        val autokilled = isPlaying.value
                        //param.args[0] = false
                        param.result = autokilled
                            //Toast.makeText(context, "FRina location stopped", Toast.LENGTH_SHORT).show()
                        XposedBridge.log("$tag: autokilled success")
                    }
                })
            }
        } catch (e: Exception) {
            XposedBridge.log("$tag: Error autokill service - ${e.message}")
                }

    }

    fun hookGojekVirtual(lpparam: XC_LoadPackage.LoadPackageParam) {

        try {
            if (lpparam.packageName == "com.gojek.partner") {
            
            if (PreferencesUtil.getIsPlaying() != true) return
            XposedBridge.log("$tag: initializing virtual location")
            
            val gojeklocationClass = XposedHelpers.findClass("com.gojek.driver.location.CurrentLocation", lpparam.classLoader)
            
            XposedBridge.hookAllConstructors(
                gojeklocationClass,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
    
                        val lat = param.thisObject.javaClass.getDeclaredField("O0OO0oOo")
                        lat.isAccessible = true                       
                        LocationUtil.updateLocation()
                        val win = LocationUtil.latitude
                        lat.set(param.thisObject, win)
                                               
                     //   XposedBridge.log("\t LAT : ${LocationUtil.latitude}")
                    }
                })

            XposedBridge.hookAllConstructors(
                gojeklocationClass,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        
                        val lon = param.thisObject.javaClass.getDeclaredField("O0OO0oOo0")
                        lon.isAccessible = true
                        LocationUtil.updateLocation()
                        val win = LocationUtil.longitude                        
                        lon.set(param.thisObject, win)
                                               
                    //    XposedBridge.log("\t LON : ${LocationUtil.longitude}")
                    }
                })
                                  
                
            val gojekvirtualClass = XposedHelpers.findClass("dark.onConnectFailed", lpparam.classLoader)            

            XposedBridge.hookAllConstructors(
                gojekvirtualClass,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
    
                        val lat = param.thisObject.javaClass.getDeclaredField("O0OO0oOo")
                        lat.isAccessible = true                       
                        LocationUtil.updateLocation()
                        val win = LocationUtil.latitude
                        lat.set(param.thisObject, win)
                                               
                     //   XposedBridge.log("\t LAT : ${LocationUtil.latitude}")
                    }
                })

            XposedBridge.hookAllConstructors(
                gojekvirtualClass,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        
                        val lon = param.thisObject.javaClass.getDeclaredField("O0OO0oOo0")
                        lon.isAccessible = true
                        LocationUtil.updateLocation()
                        val win = LocationUtil.longitude                        
                        lon.set(param.thisObject, win)
                                               
                    //    XposedBridge.log("\t LON : ${LocationUtil.longitude}")
                    }
                })
            
            }
        } catch (e: Exception) {
            XposedBridge.log("$tag: Error hooking virtual class - ${e.message}")
                }

    }


    fun hookGojekLocation(lpparam: XC_LoadPackage.LoadPackageParam) {

        try {
            if (lpparam.packageName == "com.gojek.partner") {
            
            if (PreferencesUtil.getIsPlaying() != true) return
            XposedBridge.log("$tag: initializing location")

            val gojeklocationClass = XposedHelpers.findClass("android.location.Location", lpparam.classLoader)
            
            XposedHelpers.findAndHookMethod(
                gojeklocationClass,
                "getLatitude",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                    //    XposedBridge.log("$tag Leaving method getLatitude()")
                    //    XposedBridge.log("\t Original latitude: ${param.result as Double}")
                        param.result = LocationUtil.latitude
                    //    XposedBridge.log("\t Modified to: ${LocationUtil.latitude}")
                    }
                })

            XposedHelpers.findAndHookMethod(
                gojeklocationClass,
                "getLongitude",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                    //    XposedBridge.log("$tag Leaving method getLongitude()")
                    //    XposedBridge.log("\t Original longitude: ${param.result as Double}")
                        param.result =  LocationUtil.longitude
                    //    XposedBridge.log("\t Modified to: ${LocationUtil.longitude}")
                    }
                })

            XposedHelpers.findAndHookMethod(
                gojeklocationClass,
                "getAccuracy",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                    //    XposedBridge.log("$tag Leaving method getAccuracy()")
                    //    XposedBridge.log("\t Original accuracy: ${param.result as Float}")
                        if (PreferencesUtil.getUseAccuracy() == true) {
                            param.result =  LocationUtil.accuracy
                    //        XposedBridge.log("\t Modified to: ${LocationUtil.accuracy}")
                        }
                    }
                })
                    
            val gojeklocationManagerClass = XposedHelpers.findClass("android.location.LocationManager", lpparam.classLoader)
           
            XposedHelpers.findAndHookMethod(
                gojeklocationManagerClass,
                "getLastKnownLocation",
                String::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                    //    XposedBridge.log("$tag Leaving method getLastKnownLocation(provider)")
                    //    XposedBridge.log("\t Original location: ${param.result as? Location}")
                        val provider = param.args[0] as String
                    //    XposedBridge.log("\t Requested data from: $provider")
                        val fakeLocation =  LocationUtil.createFakeLocation(provider = provider)
                        param.result = fakeLocation
                    //    XposedBridge.log("\t Fake location: $fakeLocation")
                    }
                })
            }
        } catch (e: Exception) {
            XposedBridge.log("$tag: Error hooking Location class - ${e.message}")
                }

    }
    
    fun hookServerLocationManager(lpparam: XC_LoadPackage.LoadPackageParam) {
                
        try {
            if (lpparam.packageName == "android") {
            
            if (PreferencesUtil.getIsPlaying() != true) return
            XposedBridge.log("$tag: initializing system location")
            
            val serverLocationManagerServiceClass = XposedHelpers.findClass("com.android.server.LocationManagerService", lpparam.classLoader)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                XposedHelpers.findAndHookMethod(
                    serverLocationManagerServiceClass,
                    "getLastLocation",
                    LocationRequest::class.java,
                    String::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            XposedBridge.log("$tag: System hook location")
                        //    XposedBridge.log("\t Request comes from: ${param.args[1] as String}")
                            val fakeLocation = LocationUtil.createFakeLocation()
                            param.result = fakeLocation
                            XposedBridge.log("\t Fake location: $fakeLocation")
                        }
                    })
            } else {
                XposedBridge.log("$tag: versi Android OS terlalu rendah, minimal OS 12.")
            }

            val methodsToReplace = arrayOf(
                "addGnssBatchingCallback",
                "addGnssMeasurementsListener",
                "addGnssNavigationMessageListener"
            )

            for (methodName in methodsToReplace) {
                XposedHelpers.findAndHookMethod(
                    serverLocationManagerServiceClass,
                    methodName,
                    XC_MethodReplacement.returnConstant(false)
                )
            }

            XposedHelpers.findAndHookMethod(
                XposedHelpers.findClass("com.android.server.LocationManagerService\$Receiver", lpparam.classLoader),
                "callLocationChangedLocked",
                Location::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        XposedBridge.log("$tag: System hook callLocationChangedLocked")
                        val fakeLocation = LocationUtil.createFakeLocation(param.args[0] as? Location)
                        param.args[0] = fakeLocation
                        XposedBridge.log("\t Fake location: $fakeLocation")
                    }
                })
                    
            }
        } catch (e: Exception) {
            XposedBridge.log("$tag: Error hooking Location class - ${e.message}")
            }
    }

}
