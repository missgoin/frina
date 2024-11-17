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
        val GojekBypassRegClass = XposedHelpers.findClass("android.app.Activity", classLoader)

    
        try {
        

        
                        XposedHelpers.findAndHookMethod(
                            GojekBypassRegClass,
                            "dark.BaseDeepLinkDelegate\$allDeepLinkEntries\$2",
                            "valueOf"::class.java,
                            String::class.java,
                            object : XC_MethodHook() {
                                override fun afterHookedMethod(param: MethodHookParam) {
                                    GojekUtil.gojekbypassreguler()
                                    XposedBridge.log("$tag: finding method")
                                    if (PreferencesUtil.getGojekBypassReg() == true) {
                                        param.result = true
                                        XposedBridge.log("$tag: success")
                                    }
                                    
                                }
                            }
                        )
                                
        
        

        } catch (e: Exception) {
            XposedBridge.log("$tag: error")
            XposedBridge.log(e)
          }
    
    
    }

}
