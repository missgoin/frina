// GojekApiHooks.kt
package frena.id.xposed.hooks

import android.location.Location
import android.location.LocationRequest

import frena.id.xposed.utils.LocationUtil
import frena.id.xposed.utils.GojekUtil
import frena.id.xposed.utils.PreferencesUtil

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

class GojekApiHooks{
    private val tag = "[FRina API.gp]"
    var versiGopartner : Int = 4186
            
    fun hookBypassReguler(lpparam: XC_LoadPackage.LoadPackageParam) {
        
        if (versiGopartner == 4186) {
            try {
                if (lpparam.packageName == "com.gojek.partner") {
                    XposedBridge.log("$tag: initializing bypass")
                    
                    val darkBaseDeepLinkDelegateClass = XposedHelpers.findClass("dark.BaseDeepLinkDelegate\$Companion", lpparam.classLoader)
                    XposedHelpers.findAndHookMethod(
                    darkBaseDeepLinkDelegateClass,
                    "setAutoFocusDisable",
                    //Boolean::class.java,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            val bypassreguler = param.args[0] as Boolean
                            param.args[0] = true
                            param.result = bypassreguler
                            //param.args[0] = true
                            //GojekUtil.gojekbypassreguler()        
                            //if (PreferencesUtil.getUseGojekBypassReg() == true) {                             
                                //param.result = true
                                XposedBridge.log("$tag: reguler bypassed")
                        }
                    })
                }
            } catch (e: Exception) {
                XposedBridge.log("$tag: error")
                }
        }
        
        if (versiGopartner == 4185) {
            try {
                if (lpparam.packageName == "com.gojek.partner") {       
                    XposedBridge.log("$tag: initializing bypass")
                    
                    val darkBaseDeepLinkDelegateClass = XposedHelpers.findClass("dark.BaseDeepLinkDelegate\$allDeepLinkEntries\$2", lpparam.classLoader)
                    XposedHelpers.findAndHookMethod(
                    darkBaseDeepLinkDelegateClass,
                    "valueOf",
                    //Boolean::class.java,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            val bypassreguler = param.args[0] as Boolean
                            param.args[0] = true
                            param.result = bypassreguler
                            //param.args[0] = true
                            //GojekUtil.gojekbypassreguler()        
                            //if (PreferencesUtil.getUseGojekBypassReg() == true) {                             
                                //param.result = true
                                XposedBridge.log("$tag: reguler bypassed")
                        }
                    })
                }
            } catch (e: Exception) {
                XposedBridge.log("$tag: error")
                }
        }
        
    }


    fun hookGojekVirtual(lpparam: XC_LoadPackage.LoadPackageParam) {

        try {
            if (lpparam.packageName == "com.gojek.partner") {
            
            if (PreferencesUtil.getIsPlaying() != true) return
            XposedBridge.log("$tag: initializing virtual location")

            val gojekvirtualClass = XposedHelpers.findClass("dark.onConnectFailed", lpparam.classLoader)            
            
            XposedBridge.hookAllMethods(
                gojekvirtualClass,
                "<init>",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                    
                        Object O0OO0oOo = param.args[1]
                        for (Method method : O0OO0oOo.getClass().getDeclaredMethods()) {
                            if (method.getParameterTypes().length == 10) {
                                XposedBridge.hookAllMethods(O0OO0oOo.getClass(), method.getName(), new XC_MethodHook() {
                                    override fun beforeHookedMethod(param: MethodHookParam) {
                                        if LocationUtil.updateLocation() {
                                            param.args[1] = Double.valueOf(LocationUtil.latitude)
                                        }
                                        super.beforeHookedMethod(param)
                                            XposedBridge.log("\t LAT modified to: ${LocationUtil.latitude}")
                                    })
                                }
                            }
                        }
                    }
                })

            XposedBridge.hookAllMethods(
                gojekvirtualClass,
                "<init>",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val lon = param.thisObject.javaClass.getDeclaredField("O0OO0oOo0")
                        lon.isAccessible = true                        
                       // val g = d4[param.thisObject] as Double
                       // d4.set(param.thisObject, g)                        
                        val j = LocationUtil.longitude as Double
                            LocationUtil.updateLocation()
                    //    XposedBridge.log("$tag Leaving method getLatitude()")
                    //    XposedBridge.log("\t Original latitude: ${param.result as Double}")
                    //    param.result = LocationUtil.latitude
                        lon.set(param.thisObject, j)
                        XposedBridge.log("\t LON modified to: ${LocationUtil.longitude}")
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
