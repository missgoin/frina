// GojekApiHooks.kt
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

class GojekApiHooks{
    private val tag = "[FR.ina-API]"    
    
    fun hookBypassReguler(lpparam: XC_LoadPackage.LoadPackageParam) {
                
        try {
            if (lpparam.packageName == "com.gojek.partner") {
            XposedBridge.log("$tag: initializing bypass")

            val darkBaseDeepLinkDelegateClass = XposedHelpers.findClass("dark.BaseDeepLinkDelegate\$allDeepLinkEntries$2", lpparam.classLoader)
            
            XposedHelpers.findAndHookMethod(
                darkBaseDeepLinkDelegateClass,
                "valueOf",
                //Boolean::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                      //  GojekUtil.gojekbypassreguler()        
                      //  if (PreferencesUtil.getUseGojekBypassReg() == true) {                                
                            param.result = true
                            XposedBridge.log("$tag: reguler bypassed")
                      //  }
                    }
                })
            }
        } catch (e: Exception) {
                XposedBridge.log("$tag: error")
                }
    }
    
}
