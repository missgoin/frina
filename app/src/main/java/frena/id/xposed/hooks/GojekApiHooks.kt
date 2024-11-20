// SystemServicesHooks.kt
package frena.id.xposed.hooks

import frena.id.xposed.utils.GojekUtil
import frena.id.xposed.utils.PreferencesUtil

import android.os.Build
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.Exception

class GojekApiHooks{
    private val tag = "[GoAPI]"
    
//    fun GojekAPI() {
//        hookBypassReguler(lpparam)
//    }
    
    fun hookBypassReguler(lpparam: XC_LoadPackage.LoadPackageParam) {
    
        try {
       // if (lpparam.packageName == "com.gojek.partner") {
            XposedBridge.log("$tag: finding bypass")

            val darkBaseDeepLinkDelegateClass = XposedHelpers.findClass("dark.BaseDeepLinkDelegate\$allDeepLinkEntries$2", lpparam.classLoader)

            XposedHelpers.findAndHookMethod(
                darkBaseDeepLinkDelegateClass,
                "valueOf",
                Boolean::class.java,
                object : XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam) {
                    GojekUtil.gojekbypassreguler()                
                        if (PreferencesUtil.getUseGojekBypassReg() == true) {
                        param.result = true
                        //return true
                        XposedBridge.log("$tag: success")
                        }
                    }
                })
      //  }
        } catch (e: Exception) {
            XposedBridge.log("$tag: error")
            }

    }
}
