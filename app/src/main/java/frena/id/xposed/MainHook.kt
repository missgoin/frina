// MainHook.kt
package frena.id.xposed

import android.app.Application
import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.Context
import android.content.Intent
import android.widget.Toast
import frena.id.data.MANAGER_APP_PACKAGE_NAME
import frena.id.xposed.hooks.LocationApiHooks
import frena.id.xposed.hooks.SystemServicesHooks
import frena.id.xposed.hooks.GojekApiHooks
//import frena.id.xposed.hooks.VersionCodeHooks
import frena.id.xposed.utils.PreferencesUtil
import frena.id.xposed.utils.NotificationUtils
import frena.id.xposed.utils.GojekUtil
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import java.lang.Exception
import java.io.File

class MainHook : IXposedHookLoadPackage {
    val tag = "[FRina]"
   
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
    
        //VersionCodeHooks().hookVersionCode(lpparam)       

        // Avoid hooking own app to prevent recursion
        //if (lpparam.packageName == MANAGER_APP_PACKAGE_NAME) return
        
        if ((lpparam.packageName == "com.gojek.partner") ||
        (lpparam.packageName == "com.grabtaxi.driver2")) ||
        (lpparam.packageName == "com.shopee.foody.driver.id")) ||
        (lpparam.packageName == "net.aleksandre.android.whereami"))
        {
        
            GojekApiHooks().hookBypassReguler(lpparam)
            GojekApiHooks().hookGojekLocation(lpparam)
            GojekApiHooks().hookGojekLocationManager(lpparam)
            
        //    initHooking(lpparam)
        
        } else {
            return
          }

    }
        

    private fun initHooking(lpparam: XC_LoadPackage.LoadPackageParam) {
        lateinit var context: Context
        private var locationApiHooks: LocationApiHooks? = null
        private var systemServicesHooks: SystemServicesHooks? = null       
        
        // If not playing or null, do not proceed with hooking
        if (PreferencesUtil.getIsPlaying() != true) return

        // Hook system services if user asked for system wide hooks
        if (lpparam.packageName == "android") {
            systemServicesHooks = SystemServicesHooks(lpparam).also { it.initHooks() }
        }
        
        XposedHelpers.findAndHookMethod(
            "android.app.Instrumentation",
            lpparam.classLoader,
            "callApplicationOnCreate",
            Application::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    context = (param.args[0] as Application).applicationContext.also {
                        XposedBridge.log("$tag Target App's context has been acquired successfully.")
                        Toast.makeText(it, "Fake Location Is Active!", Toast.LENGTH_SHORT).show()
                    }
                    locationApiHooks = LocationApiHooks(lpparam).also { it.initHooks() }
                }
            })

    }

}



        
        



