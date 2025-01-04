// SystemServicesHooks.kt
package frena.id.xposed.hooks

import android.location.Location
import android.location.LocationRequest

import frena.id.xposed.utils.LocationUtil
import frena.id.xposed.utils.GojekUtil
import frena.id.xposed.utils.PreferencesUtil
import android.os.Build
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class SystemServicesHooks(val appLpparam: LoadPackageParam) {
    private val tag = "[FRina SystemAPI]"

    fun initHooks() {
        if (PreferencesUtil.getIsPlaying() != true) return
        hookSystemServices(appLpparam.classLoader)
    //    XposedBridge.log("$tag Instantiated hooks successfully")
    }

    private fun hookSystemServices(classLoader: ClassLoader) {
      if (PreferencesUtil.getIsPlaying() == true) {
        try {
            val locationManagerServiceClass = XposedHelpers.findClass("com.android.server.LocationManagerService", classLoader)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                XposedHelpers.findAndHookMethod(
                    locationManagerServiceClass,
                    "getLastLocation",
                    LocationRequest::class.java,
                    String::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            XposedBridge.log("$tag: System hook location")
                        //    XposedBridge.log("\t Request comes from: ${param.args[1] as String}")
                            val fakeLocation = LocationUtil.createFakeLocation()
                            param.result = fakeLocation
                       //     XposedBridge.log("\t Fake location: $fakeLocation")
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
                    locationManagerServiceClass,
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
                    //    XposedBridge.log("$tag: System hook callLocationChangedLocked")
                        val fakeLocation = LocationUtil.createFakeLocation(param.args[0] as? Location)
                        param.args[0] = fakeLocation
                    //    XposedBridge.log("\t Fake location: $fakeLocation")
                    }
                })

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking system services")
            XposedBridge.log(e)
        }
        
      }
    }
}