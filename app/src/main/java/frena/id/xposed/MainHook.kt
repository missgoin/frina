// MainHook.kt
package frena.id.xposed

import android.app.Application
import android.content.Context
import android.widget.Toast
import frena.id.data.MANAGER_APP_PACKAGE_NAME
import frena.id.xposed.hooks.LocationApiHooks
import frena.id.xposed.hooks.SystemServicesHooks
import frena.id.xposed.utils.PreferencesUtil
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MainHook : IXposedHookLoadPackage {

    val tag = "[f.Rina]"    
    lateinit var context: Context


    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (lpparam != null) {
            when (lpparam.packageName) {
                
                "com.gojek.partner" -> {
                    XposedBridge.log("$tag: Finding method")
                    
                    try {
                    
          XposedHelpers.findAndHookMethod(
            "android.app.Instrumentation",
            lpparam.classLoader,
            "callApplicationOnCreate",
            Application::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    context = (param.args[0] as Application).applicationContext.also {
                        XposedBridge.log("$tag: loaded")
                    
                    }
                }
            }
          )
                    } catch (e: Exception) {
                        XposedBridge.log("$tag: fuck with exceptions: $e")
                    }
                }
            
            }




        }
    }


}



        
        



