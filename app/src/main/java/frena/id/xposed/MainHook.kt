// MainHook.kt
package frena.id.xposed

import android.app.Application
import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.Context
import android.widget.Toast
import frena.id.data.MANAGER_APP_PACKAGE_NAME
import frena.id.xposed.hooks.LocationApiHooks
import frena.id.xposed.hooks.SystemServicesHooks
import frena.id.xposed.hooks.GojekApiHooks
import frena.id.xposed.hooks.VersionCodeHooks
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
    val tag = "[F.Rina]"
    


    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
    
        VersionCodeHooks().hookVersionCode(lpparam)
        
    // Version Code
    val i = VersionCodeHooks().hookVersionCode(lpparam, "com.gojek.partner")
    val j = VersionCodeHooks().hookVersionCode(lpparam, "com.gojek.driver.kilat")
    val k = VersionCodeHooks().hookVersionCode(lpparam, "com.shopee.foody.driver.id")
    val g = VersionCodeHooks().hookVersionCode(lpparam, "com.grabtaxi.driver2")

    vcodekilat = j
    vcodeptnr = i
    vcodeshope = k
    vcodegrab = g

    when {
        i != 0 -> XposedBridge.log("GoPartner Code : $vcodeptnr")
        j != 0 -> XposedBridge.log("GoKilat Code : $vcodekilat")
        k != 0 -> XposedBridge.log("Shope Code : $vcodeshope")
        g != 0 -> XposedBridge.log("Grab Code : $vcodegrab")
    }        

        // Avoid hooking own app to prevent recursion
        //if (lpparam.packageName == MANAGER_APP_PACKAGE_NAME) return
        
//        if (lpparam.packageName != "com.gojek.partner") return
        
        GojekApiHooks().hookBypassReguler(lpparam)

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



        
        



