// GojekUtil.kt
package frena.id.xposed.utils

import frena.id.data.DEFAULT_USE_GOJEK_BYPASS_REG
//import frena.id.data.DEFAULT_GOBYPASSREG
import frena.id.data.DEFAULT_USE_GOJEK_BYPASS_ACE
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
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import java.lang.Exception
import java.io.File

object GojekUtil {
    private const val tag = "[FRina Util]"
  //  private const val versiGopartner
  //  var versiGopartner : Int = 4186
    var versiGopartner : Int = 0
    
    @Synchronized
    fun gojekVersionCode(lpparam: XC_LoadPackage.LoadPackageParam): Int {
    
        try {
            if (lpparam.packageName == "com.gojek.partner") {
                //XposedBridge.log("$tag: Checking version code")
        
                val parserCls = XposedHelpers.findClass("android.content.pm.PackageParser", lpparam.classLoader)
                val parser = parserCls.newInstance()
                val apkPath = File(lpparam.appInfo.sourceDir)
                val pkg = XposedHelpers.callMethod(parser, "parsePackage", apkPath, 0)
                //val versionName = XposedHelpers.getObjectField(pkg, "mVersionName") as String
                val versionCode = XposedHelpers.getIntField(pkg, "mVersionCode")
                
                //val versiGopartner = versionCode
                //return versiGopartner
                versiGopartner = versionCode.versiGopartner
                return versiGopartner
           //     if (versiGopartner != 0) {
           //         versiGopartner = versionCode.versiGopartner
           //     }
                //versiGopartner = println("$VersiGopartner") as Int
                
                XposedBridge.log("$tag: gp version code $versionCode")
                
            //    return versionCode
                
            }
        } catch (e: Throwable) {
            XposedBridge.log("$tag: error finding version code")            
            }
        return 0
    }
    
     
   

    @Synchronized
    fun gojekbypassreguler() {
    
        try {       
            PreferencesUtil.getUseGojekBypassReg()?.let {

                if (PreferencesUtil.getUseGojekBypassReg() == true) {
                    PreferencesUtil.getUseGojekBypassReg() ?: DEFAULT_USE_GOJEK_BYPASS_REG
                }
                    XposedBridge.log("$tag: bypass activated")

            }
        } catch (e: Exception) {
            XposedBridge.log("$tag: Error - ${e.message}")
            }
    }


}