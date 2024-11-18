// MainHook.kt
package frena.id.xposed

import android.app.Application
import android.content.Context
import android.widget.Toast
import frena.id.data.MANAGER_APP_PACKAGE_NAME
import frena.id.xposed.hooks.LocationApiHooks
import frena.id.xposed.hooks.SystemServicesHooks
import frena.id.xposed.hooks.GojekApiHooks
import frena.id.xposed.utils.PreferencesUtil
import frena.id.xposed.utils.NotificationUtils
import frena.id.xposed.utils.GojekUtil
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MainHook : IXposedHookLoadPackage {

    val tag = "[f.Rina]"    
    lateinit var context: Context
    
    private var gojekApiHooks: GojekApiHooks? = null
    private var locationApiHooks: LocationApiHooks? = null
    private var systemServicesHooks: SystemServicesHooks? = null

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
    
    
        if (lpparam != null) {
            when (lpparam.packageName) {
                
                "com.gojek.partner" -> {
                    XposedBridge.log("$tag: Finding method")                 
            
                    try {                    
                        XposedHelpers.findAndHookMethod(
                            "android.app.Instrumentation",
                            lpparam.classLoader,
                            "callApplicationOnCreate",
                            Application::class.java,
                            object : XC_MethodHook() {
                                override fun beforeHookedMethod(param: MethodHookParam) {
                                    context = (param.args[0] as Application).applicationContext.also {
                                        XposedBridge.log("$tag: target initialized")
                                    }
                                }
                            }
                        )
                    } catch (e: Exception) {
                        XposedBridge.log("$tag: fuck with exceptions: $e")
                      }
                        
                    try {
                        //PreferencesUtil.getGojekBypassReg() == true                        
                        //GojekBypassReg().gojekbypassreg(lpparam)
                        gojekApiHooks = GojekApiHooks(lpparam).also { it.initHooks() }
                      
                    } catch (e: Exception) {
                        XposedBridge.log("$tag: fuck exceptions: $e")
                      }
                      
                }
                
            
            }


        }    
    
    
         // Avoid hooking own app to prevent recursion
        if (lpparam.packageName == MANAGER_APP_PACKAGE_NAME) return

        // If not playing or null, do not proceed with hooking
        if (PreferencesUtil.getIsPlaying() != true) return

        // Hook system services if user asked for system wide hooks
        if (lpparam.packageName == "android") {
            systemServicesHooks = SystemServicesHooks(lpparam).also { it.initHooks() }
        }

        initHookingLogic(lpparam)
    }
    
    private fun initHookingLogic(lpparam: LoadPackageParam) {
    
        if (lpparam != null) {
            when (lpparam.packageName) {
                
                "com.gojek.partner" -> {                                      
                    
                    try {
                        XposedHelpers.findAndHookMethod(
                            "android.app.Instrumentation",
                            lpparam.classLoader,
                            "callApplicationOnCreate",
                            Application::class.java,
                            object : XC_MethodHook() {
                                override fun afterHookedMethod(param: MethodHookParam) {
                                    context = (param.args[0] as Application).applicationContext.also {
                                    XposedBridge.log("$tag target location loaded.")
                                    Toast.makeText(it, "Fake Location Is Active!", Toast.LENGTH_SHORT).show()
                                    }
                                locationApiHooks = LocationApiHooks(lpparam).also { it.initHooks() }
                                }
                            }
                        )
                                      
                    } catch (e: Exception) {
                        XposedBridge.log("$tag: fuck exceptions: $e")
                      }

                      
                }
                
                
                
                "net.aleksandre.android.whereami" -> {                
                    
                    try {
                        XposedHelpers.findAndHookMethod(
                            "android.app.Instrumentation",
                            lpparam.classLoader,
                            "callApplicationOnCreate",
                            Application::class.java,
                            object : XC_MethodHook() {
                                override fun afterHookedMethod(param: MethodHookParam) {
                                    context = (param.args[0] as Application).applicationContext.also {
                                    XposedBridge.log("$tag Target acquired successfully.")
                                    Toast.makeText(it, "Fake Location Is Active!", Toast.LENGTH_SHORT).show()
                                    }
                                locationApiHooks = LocationApiHooks(lpparam).also { it.initHooks() }
                                }
                            }
                        )
                                      
                    } catch (e: Exception) {
                        XposedBridge.log("$tag: fuck exceptions: $e")
                      }
            
                }
                
                
            
            }
    
    
        }


    }






}



        
        



