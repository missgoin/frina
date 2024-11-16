// SystemServicesHooks.kt
package frena.id.xposed.hooks

import frena.id.xposed.utils.GojekUtil
import frena.id.xposed.utils.PreferencesUtil

import android.os.Build
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class GojekBypassReg(val appLpparam: LoadPackageParam) {
    private val tag = "[Gobreg]"



    private fun GojekBypassReg(classLoader: ClassLoader) {
        val gojekbypassregulerClass = XposedHelpers.findClass("android.app.Activity", classLoader)

    
        try {
        

        
                        XposedHelpers.findAndHookMethod(
                            gojekbypassregulerClass,
                            lpparam.classLoader,
                            "dark.BaseDeepLinkDelegate\$allDeepLinkEntries\$2",
                            "valueOf"::class.java,
                            String::class.java,
                            object : XC_MethodHook() {
                                override fun afterHookedMethod(param: MethodHookParam) {
                                    (param.thisObject as Activity).finish()
                                    XposedBridge.log("$tag: success")
                                }
                            }
                        )
                                
        
        

        } catch (e: Exception) {
            XposedBridge.log("$tag: error")
            XposedBridge.log(e)
          }
    
    
    }

}
