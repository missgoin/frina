// GojekUtil.kt
package frena.id.xposed.utils

import android.os.Build
import frena.id.data.DEFAULT_GOJEK_BYPASS_REG
import frena.id.data.DEFAULT_GOBYPASSREG
import frena.id.data.DEFAULT_GOJEK_BYPASS_ACE
import frena.id.xposed.utils.PreferencesUtil
import de.robv.android.xposed.XposedBridge

object GojekUtil {
    private const val TAG = "[GojekUtil]"

    @Synchronized
    fun gojekbypassreguler() {
        try {
            PreferencesUtil.getGojekBypassReg()?.let {


                 if (PreferencesUtil.getGojekBypassReg() == true) {
                     PreferencesUtil.GojekBypassReg() ?: DEFAULT_GOJEK_BYPASS_REG
                }


            } ?: XposedBridge.log("$TAG bypass activated")
        } catch (e: Exception) {
            XposedBridge.log("$TAG Error - ${e.message}")
        }
    }




}