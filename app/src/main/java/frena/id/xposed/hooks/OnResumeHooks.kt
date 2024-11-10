package frena.id.xposed.hooks

import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class OnResumeHooks: IXposedHookLoadPackage {
    private val tag = "[f.Rina resume]"
    
    private val target1 = "com.gojek.partner"
    private val target2 = "com.grabtaxi.driver2"
    private val target3 = "com.shopeefood.driver.id"
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.gojek.partner" || lpparam.packageName != "com.grabtaxi.driver2") 
           return
           
        hookresume(lpparam)
    }
    
    
    private fun hookresume(classLoader: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
               ActivityClass, "onResume",
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




