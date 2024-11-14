// MainHook.kt
package frena.id.xposed.hooks

import android.app.Application
import android.content.Context
import android.widget.Toast
import frena.id.data.MANAGER_APP_PACKAGE_NAME
import frena.id.xposed.hooks.LocationApiHooks
import frena.id.xposed.hooks.SystemServicesHooks
import frena.id.xposed.hooks.OnResumeHooks
import frena.id.xposed.utils.PreferencesUtil
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class FakeGPS : IXposedHookLoadPackage, IXposedHookZygoteInit {

    val tag = "[f.Rina]"
    
    lateinit var context: Context

    private val targetPackageName1 = "com.gojek.partner"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        if (lpparam.packageName != targetPackageName1) {
            return
        }

        XposedHelpers.findAndHookMethod(
            "android.app.Instrumentation",
            lpparam.classLoader,
            "callApplicationOnCreate",
            Application::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    context = (param.args[0] as Application).applicationContext.also {
                        XposedBridge.log("$tag: loaded")
                    
                    }
                }
            }
        )
        
        
        val appActivityClass = XposedHelpers.findClass("android.app.Activity", lpparam.classLoader)
        XposedBridge.hookAllMethods(appActivityClass, "onStart", object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                Log.d(TAG, "onStart")
                val currActivity = param.thisObject as Activity
                    checkversion(currActivity)                
            }
        })
        
        
        fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
            
            CheckVersion(lpparam)
            
                val gop = a(lpparam, "com.gojek.partner") as Int
                val gra = a(lpparam, "com.grabtaxi.driver2") as Int
                val sho = a(lpparam, "com.shopee.foody.driver.id") as Int
                    
                    versioncodegop = gop
                    versioncodegra = gra
                    versioncodesho = sho
                    
                when {
                    gop != 0 -> {
                        val stringBuilder = "version code GoPartner: $versioncodegop"
                        log(stringBuilder)
                    }
                    gra != 0 -> {
                        val stringBuilder = "version code Grab Driver: $versioncodegra"
                        log(stringBuilder)
                    }
                    sho != 0 -> {
                        val stringBuilder = "version code Shopefood Driver: $versioncodesho"
                        log(stringBuilder)
                    }
                }
        }
        
        fun CheckVersion (lpparam: XC_LoadPackage.LoadPackageParam) {
            checkVersion(lpparam)
        }
        
        fun checkVersion(lpparam: XC_LoadPackage.LoadPackageParam) {
            if (lpparam.packageName == "com.gojek.partner" || lpparam.packageName == "com.grabtaxi.driver2") {
                XposedHelpers.findAndHookMethod(Activity::class.java, "onResume", i1())
            }
        }
        
            class i1 : XC_MethodHook() {
            @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                XposedBridge.log("$tag: target On Resume")
                }
            }






    }


}



