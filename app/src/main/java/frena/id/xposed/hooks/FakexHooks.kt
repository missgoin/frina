    // GojekApiHooks.kt
package frena.id.xposed.hooks

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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

class FakexHooks(val appLpparam: LoadPackageParam) {
    private val tag = "[FRina FX]"
    var mLastUpdated: Long = 0

    fun fakexLocationAPI() {
        if (PreferencesUtil.getIsPlaying() != true) return
        hookGojekLocation(appLpparam.classLoader)
        hookGojekLocationManager(appLpparam.classLoader)
    }

    private fun update() {
        try {
            mLastUpdated = System.currentTimeMillis()
            latitude = LocationUtil.latitude
            longitude = LocationUtil.longitude
            accuracy = LocationUtil.accuracy

        } catch (e: Exception) {
            //Timber.tag("GPS Setter").e(e, "Failed to get XposedSettings for %s", context.packageName)
            XposedBridge.log("$tag error - ${e.message}")
          }
    }


    private fun hookGojekLocation(classLoader: ClassLoader) {    

      if (PreferencesUtil.getIsPlaying() == true) {
      
        XposedBridge.log("$tag initializing service FX......")        
        
        try {
                       
            XposedBridge.log("$tag starting FX location......")

            val gojeklocationClass = XposedHelpers.findClass("android.location.Location", classLoader)
            
            XposedHelpers.findAndHookMethod(
                gojeklocationClass,
                //"android.location.Location",
                //classLoader,
                "getLatitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                      //  super.beforeHookedMethod(param)
                        if (System.currentTimeMillis() - mLastUpdated > 200) {
                            update()
                        }
                        LocationUtil.updateLocation()
                    //    XposedBridge.log("$tag Leaving method getLatitude()")
                    //    XposedBridge.log("\t Original latitude: ${param.result as Double}")
                        param.result = LocationUtil.latitude
                    //    XposedBridge.log("\t Modified to: ${LocationUtil.latitude}")
                    }
                })

            XposedHelpers.findAndHookMethod(
                gojeklocationClass,
                //"android.location.Location",
                //classLoader,
                "getLongitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                      //  super.beforeHookedMethod(param)
                        if (System.currentTimeMillis() - mLastUpdated > 200) {
                            update()
                        }
                        LocationUtil.updateLocation()
                    //    XposedBridge.log("$tag Leaving method getLongitude()")
                    //    XposedBridge.log("\t Original longitude: ${param.result as Double}")
                        param.result =  LocationUtil.longitude
                    //    XposedBridge.log("\t Modified to: ${LocationUtil.longitude}")
                    }
                })

            XposedHelpers.findAndHookMethod(
                gojeklocationClass,
                //"android.location.Location",
                //classLoader,
                "getAccuracy",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                     //   super.beforeHookedMethod(param)
                        if (System.currentTimeMillis() - mLastUpdated > 200) {
                            update()
                        }
                        LocationUtil.updateLocation()
                    //    XposedBridge.log("$tag Leaving method getAccuracy()")
                    //    XposedBridge.log("\t Original accuracy: ${param.result as Float}")
                        if (PreferencesUtil.getUseAccuracy() == true) {
                            param.result =  LocationUtil.accuracy
                    //        XposedBridge.log("\t Modified to: ${LocationUtil.accuracy}")
                        }
                    }
                })
                
            XposedHelpers.findAndHookMethod(
                gojeklocationClass,
                //"android.location.LocationManager",
                //classLoader,
                "set",
                Location::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                     //   super.beforeHookedMethod(param)
                        if (System.currentTimeMillis() - mLastUpdated > 200) {
                            update()
                        }
                        val fakeLocation = LocationUtil.createFakeLocation(param.args[0] as? Location)
                        param.args[0] = fakeLocation
                    //    XposedBridge.log("\t Fake location: $fakeLocation")
                    }
                })

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking Location class - ${e.message}")
                }
      
      }

    }
    
    
    private fun hookGojekLocationManager(classLoader: ClassLoader) {

      if (PreferencesUtil.getIsPlaying() == true) {

        try {
                    
            val gojeklocationManagerClass = XposedHelpers.findClass("android.location.LocationManager", classLoader)
           
            XposedHelpers.findAndHookMethod(
                gojeklocationManagerClass,
                //"android.location.LocationManager",
                //classLoader,
                "getLastKnownLocation",
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                     //   super.beforeHookedMethod(param)
                    //    XposedBridge.log("$tag Leaving method getLastKnownLocation(provider)")
                    //    XposedBridge.log("\t Original location: ${param.result as? Location}")
                        val provider = param.args[0] as String
                    //    XposedBridge.log("\t Requested data from: $provider")
                        val fakeLocation =  LocationUtil.createFakeLocation(provider = provider)
                        param.result = fakeLocation
                    //    XposedBridge.log("\t Fake location: $fakeLocation")
                    }
                })            

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking Location class - ${e.message}")
                }

      }

    }
    
                                            
    
    private fun hookServerLocationManager(classLoader: ClassLoader) {
                
        try {
            
           // if (PreferencesUtil.getIsPlaying() != true) return
            XposedBridge.log("$tag initializing FX system location")
            
            val serverLocationManagerServiceClass = XposedHelpers.findClass("com.android.server.LocationManagerService", classLoader)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                XposedHelpers.findAndHookMethod(
                    serverLocationManagerServiceClass,
                    "getLastLocation",
                    LocationRequest::class.java,
                    String::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            super.beforeHookedMethod(param)
                            XposedBridge.log("$tag: System hook location")
                        //    XposedBridge.log("\t Request comes from: ${param.args[1] as String}")
                            val fakeLocation = LocationUtil.createFakeLocation()
                            param.result = fakeLocation
                            XposedBridge.log("\t Fake location: $fakeLocation")
                        }
                    })
            } else {
                XposedBridge.log("$tag versi Android OS terlalu rendah, minimal OS 12.")
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
                XposedHelpers.findClass("com.android.server.LocationManagerService\$Receiver", classLoader),
                "callLocationChangedLocked",
                Location::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)
                        XposedBridge.log("$tag: System hook callLocationChangedLocked")
                        val fakeLocation = LocationUtil.createFakeLocation(param.args[0] as? Location)
                        param.args[0] = fakeLocation
                        XposedBridge.log("\t Fake location: $fakeLocation")
                    }
                })
                    
            
        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking Location class - ${e.message}")
            }
    }

}
