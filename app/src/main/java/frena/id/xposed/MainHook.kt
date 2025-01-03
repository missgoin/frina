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
//import frena.id.xposed.utils.NotificationUtils
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
  //  lateinit var context: Context
   
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // Avoid hooking own app to prevent recursion
        if (lpparam.packageName == MANAGER_APP_PACKAGE_NAME) return
        
        if (PreferencesUtil.getIsPlaying() != true) return
        
        if (lpparam != null) {
            when (lpparam.packageName) {
            
            "com.gojek.partner" -> {
                try {
                    XposedHelpers.findAndHookMethod(
                    "android.app.Application",
                    lpparam.classLoader,
                    "onCreate",
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                     //   context = (param.args[0] as Application).applicationContext.also {
                        //XposedBridge.log("$tag Target App's context has been acquired successfully.")
                     //   Toast.makeText(it, "Fake Location Is Active!", Toast.LENGTH_SHORT).show()
                   // }
                        GojekApiHooks().hookGojekLocation(lpparam)
                        GojekApiHooks().hookServerLocationManager(lpparam)
                        GojekApiHooks().autokillGojek(lpparam)
                        }
                    })
                
                } catch (e: Exception) {
                        XposedBridge.log("$tag: gojek error $e")
                    }
            }
            
            
            
            }
        }
            
    }
 
 


}



        
        



