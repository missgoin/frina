// MainHook.kt
package frena.id.xposed

import android.app.Application
import android.app.Instrumentation
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
import frena.id.xposed.hooks.FakexHooks
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
    lateinit var context: Context
    
    private var fakexHooks: FakexHooks? = null
    private var systemServicesHooks: SystemServicesHooks? = null
   
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // Avoid hooking own app to prevent recursion
        if (lpparam.packageName == MANAGER_APP_PACKAGE_NAME) return
        
        //if (PreferencesUtil.getIsPlaying() != true) return
       
       // Hook system services if user asked for system wide hooks
      //  if (lpparam.packageName == "android") {
      //      systemServicesHooks = SystemServicesHooks(lpparam).also { it.initHooks() }
      //  }
        
        if (lpparam != null) {
            when (lpparam.packageName) {
            
            "com.gojek.partner" -> {
                try {
                    XposedHelpers.findAndHookMethod(
                    "android.app.Instrumentation",
                    lpparam.classLoader,
                    "callApplicationOnCreate",
                    Application::class.java,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            context = (param.args[0] as Application).applicationContext
                            fakexHooks = FakexHooks(lpparam).also { it.fakexLocationAPI() }
                            //FakexHooks().hookServerLocationManager(lpparam)
                            GojekApiHooks().autokillGojek(lpparam)
                            //GojekApiHooks().hookGojekVirtual(lpparam)
                        }
                    })
                
                } catch (e: Exception) {
                        XposedBridge.log("$tag: Hook gojek error $e")
                    }
            }
                        
            
            "android" -> {

                try {
                systemServicesHooks = SystemServicesHooks(lpparam).also { it.initHooks() }
                
                } catch (e: Exception) {
                        XposedBridge.log("$tag: Hook system error $e")
                    }
            }
            
            
            "net.aleksandre.android.whereami" -> {
                try {
                    XposedHelpers.findAndHookMethod(
                    "android.app.Instrumentation",
                    lpparam.classLoader,
                    "callApplicationOnCreate",
                    Application::class.java,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            context = (param.args[0] as Application).applicationContext
                            fakexHooks = FakexHooks(lpparam).also { it.fakexLocationAPI() }
                            //FakexHooks().hookServerLocationManager(lpparam)
                            //GojekApiHooks().autokillGojek(lpparam)
                            //GojekApiHooks().hookGojekVirtual(lpparam)
                        }
                    })
                
                } catch (e: Exception) {
                        XposedBridge.log("$tag: Hook gojek error $e")
                    }
            }
            
            
            }
        }
            
    }
 
 


}



        
        



