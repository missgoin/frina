// MainHook.kt
package frena.id.xposed

import android.app.Application
import android.content.Context
import android.widget.Toast
import frena.id.data.MANAGER_APP_PACKAGE_NAME
import frena.id.xposed.hooks.LocationApiHooks
import frena.id.xposed.hooks.SystemServicesHooks
import frena.id.xposed.hooks.GojekApiHooks
import frena.id.xposed.utils.PreferencesUtil
import frena.id.xposed.utils.NotificationUtils
import frena.id.xposed.utils.GojekUtil
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.Exception

class MainHook : IXposedHookLoadPackage {
    val tag = "[FR.ina]"    
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        // Avoid hooking own app to prevent recursion
        //if (lpparam.packageName == MANAGER_APP_PACKAGE_NAME) return
        if (lpparam.packageName != "com.gojek.partner") return
        
        GojekApiHooks(lpparam)

        initHooking(lpparam)
    }
    
    //private var gojekApiHooks: GojekApiHooks? = null
    private var locationApiHooks: LocationApiHooks? = null
    private var systemServicesHooks: SystemServicesHooks? = null
    
    lateinit var context: Context

    private fun initHooking(lpparam: XC_LoadPackage.LoadPackageParam) {

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



        
        



