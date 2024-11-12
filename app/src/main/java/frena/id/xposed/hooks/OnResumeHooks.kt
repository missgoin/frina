package frena.id.xposed.hooks

import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class OnResumeHooks(val appLpparam: LoadPackageParam) {
    private val tag = "[f.Rina resume]"
    
    private val target1 = "com.gojek.partner"
    private val target2 = "com.grabtaxi.driver2"
    private val target3 = "com.shopeefood.driver.id"
    
    fun resumehooks() {
        hookonresume()
        XposedBridge.log("$tag successfully")
    }

    private fun hookonresume() {
        hookresume(appLpparam.classLoader)        
    }

    
    
    private fun hookresume(classLoader: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
               Activity::class.java, "onResume",
               class : XC_MethodHook() {
               @Throws(Throwable::class)
                  override fun beforeHookedMethod(param: MethodHookParam) {
                  rina1()
                  XposedBridge.log("$tag")
                  }
               }
            )
        } catch (e: Exception) {
            XposedBridge.log("$tag error")
            XposedBridge.log(e)
        }          
    }
                 
                        
    fun rina1() {
       // code
    }



}




