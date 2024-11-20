package frena.id.xposed.hooks

import frena.id.xposed.utils.GojekUtil
import frena.id.xposed.utils.PreferencesUtil

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
import java.lang.Exception
import java.io.File

class VersionCodeHooks{
    private val tag = "[VersionCode]"
    var vcodeptnr: Int = 0
    
    @SuppressLint("StaticFieldLeak")
    private lateinit var paramString: String
        

    fun hookVersionCode(lpparam: XC_LoadPackage.LoadPackageParam, paramString: String): Int {
        
        if (lpparam.packageName == paramString) {
            try {
                return XposedHelpers.getIntField(
                XposedHelpers.callMethod(
                    XposedHelpers.findClass(
                        "android.content.pm.PackageParser", 
                        lpparam.classLoader).newInstance(),
                        "parsePackage",
                        File(lpparam.appInfo.sourceDir),
                        0),
                        "mVersionCode"
                )
            } catch (e: IllegalAccessException) {
                XposedBridge.log("$tag 2: $e")
                throw IllegalAccessError(e.message)
            } catch (e: InstantiationException) {
                XposedBridge.log("$tag 1: $e")
                throw e
            }
            
        }
    }


    // Version Code
    val i = hookVersionCode(lpparam, "com.gojek.partner")
    val j = hookVersionCode(lpparam, "com.gojek.driver.kilat")
    val k = hookVersionCode(lpparam, "com.shopee.foody.driver.id")
    val g = hookVersionCode(lpparam, "com.grabtaxi.driver2")

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



}