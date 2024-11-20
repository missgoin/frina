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
import java.io.File

class MainHook : IXposedHookLoadPackage {
    val tag = "[F.Rina]"
    var vcodeptnr: Int = 0
    
    @SuppressLint("StaticFieldLeak")
    private lateinit var paramString: String
    

fun a(lpparam: XC_LoadPackage.LoadPackageParam, paramString: String): Int {
    if (lpparam.packageName == paramString) {
        try {
            return XposedHelpers.getIntField(
                XposedHelpers.callMethod(
                    XposedHelpers.findClass("android.content.pm.PackageParser", lpparam.classLoader).newInstance(),
                    "parsePackage",
                    File(lpparam.appInfo.sourceDir),
                    0
                ),
                "mVersionCode"
            )
        } catch (e: IllegalAccessException) {
            XposedBridge.log("Cek Version 2 : $e")
            throw IllegalAccessError(e.message)
        } catch (e: InstantiationException) {
            XposedBridge.log("Cek Version 1 : $e")
            throw e
        }
    }
    return 0
}

    
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
    
    // Version Code
    val i = a(lpparam, "com.gojek.partner")
    val j = a(lpparam, "com.gojek.driver.kilat")
    val k = a(lpparam, "com.shopee.foody.driver.id")
    val g = a(lpparam, "com.grabtaxi.driver2")

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
        if (lpparam.packageName != "com.gojek.partner") return
        
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



        
        



