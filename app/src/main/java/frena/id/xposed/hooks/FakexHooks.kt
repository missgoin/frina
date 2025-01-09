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
      //  if (PreferencesUtil.getIsPlaying() != true) return
        hookGojekLocation(appLpparam.classLoader)
        hookGojekLocationManager(appLpparam.classLoader)
    }

    private fun update() {
        try {
            mLastUpdated = System.currentTimeMillis()
            LocationUtil.latitude
            LocationUtil.longitude
            LocationUtil.accuracy

        } catch (e: Exception) {
            //Timber.tag("GPS Setter").e(e, "Failed to get XposedSettings for %s", context.packageName)
            XposedBridge.log("$tag error - ${e.message}")
          }
    }


    private fun hookGojekLocation(classLoader: ClassLoader) {          
      
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
                        
                        if (PreferencesUtil.getIsPlaying() == true) {
                            LocationUtil.updateLocation()
                            param.result = LocationUtil.latitude
                        }
                       // XposedBridge.log("\t $tag X-Location LAT : ${LocationUtil.latitude}")
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
                        
                        if (PreferencesUtil.getIsPlaying() == true) {
                            LocationUtil.updateLocation()
                            param.result =  LocationUtil.longitude
                        }
                        XposedBridge.log("\t $tag FX-Location LAT: ${LocationUtil.latitude} LON: ${LocationUtil.longitude}")
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
                        
                        if (PreferencesUtil.getIsPlaying() == true) {
                            LocationUtil.updateLocation()
                            if (PreferencesUtil.getUseAccuracy() == true) {
                                param.result =  LocationUtil.accuracy
                            }
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
                        
                        if (PreferencesUtil.getIsPlaying() == true) {
                            val fakeLocation = LocationUtil.createFakeLocation(param.args[0] as? Location)
                            param.args[0] = fakeLocation
                        }
                    }
                })

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking Location class - ${e.message}")
                }      

    }
    
    
    private fun hookGojekLocationManager(classLoader: ClassLoader) {

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
                        if (System.currentTimeMillis() - mLastUpdated > 200) {
                            update()
                        }
                        
                        if (PreferencesUtil.getIsPlaying() == true) {
                            val provider = param.args[0] as String
                            val fakeLocation =  LocationUtil.createFakeLocation(provider = provider)
                            param.result = fakeLocation
                        }
                    }
                })            

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking Location class - ${e.message}")
                }
    }


}
