// GojekUtil.kt
package frena.id.xposed.utils

import android.os.Build
import frena.id.data.DEFAULT_GOJEK_BYPASS_REG
import frena.id.data.DEFAULT_GOJEK_BYPASS_ACE
import de.robv.android.xposed.XposedBridge

object GojekUtil {
    private const val TAG = "[GojekUtil]"

    @Synchronized
    fun bypassreguler() {
        try {
            PreferencesUtil.getGojekBypassReg()?.let {


                 if (PreferencesUtil.getGojekBypassReg() == true) {
                     gobypassreg = PreferencesUtil.gobypassreg() ?: DEFAULT_GOBYPASSREG
                }


            } ?: XposedBridge.log("$TAG Last clicked location is null")
        } catch (e: Exception) {
            XposedBridge.log("$TAG Error - ${e.message}")
        }
    }




}