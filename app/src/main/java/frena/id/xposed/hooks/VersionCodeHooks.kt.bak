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
    var vcodekilat: Int = 0
    var vcodegrab: Int = 0
    var vcodeshope: Int = 0
    
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


}